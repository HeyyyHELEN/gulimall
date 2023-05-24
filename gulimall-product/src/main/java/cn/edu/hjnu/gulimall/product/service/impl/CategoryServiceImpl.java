package cn.edu.hjnu.gulimall.product.service.impl;

import cn.edu.hjnu.gulimall.product.service.CategoryBrandRelationService;
import cn.edu.hjnu.gulimall.product.vo.Catelog2Vo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.common.utils.Query;

import cn.edu.hjnu.gulimall.product.dao.CategoryDao;
import cn.edu.hjnu.gulimall.product.entity.CategoryEntity;
import cn.edu.hjnu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        baseMapper.deleteBatchIds(catIds);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        ArrayList<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    //级联更新所有关联的数据
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));

        return categoryEntities;
    }

    /**
     * 查询出父ID为 parent_cid的List集合
     */
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntities,Long parentId) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(item -> item.getParentCid().equals(parentId)
        ).collect(Collectors.toList());
        return collect;
    }


    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        //1、从缓存中获取
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        //2、缓存中没有
        if (StringUtils.isEmpty(catalogJson)){
            System.out.println("没有缓存数据，需要查询数据库！");
            //2.1、从数据库中获取
            Map<String, List<Catelog2Vo>> catalogFromDb = getCatalogFromDb();
            //2.2、转换为json字符串存入缓存中
            String jsonString = JSON.toJSONString(catalogFromDb);

            return catalogFromDb;
        }
        System.out.println("缓存中存在数据，直接返回");
        //3、缓存中有数据解析为java对象后返回
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>(){
        });
        return result;
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJsonAddCache() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)){
            Map<String, List<Catelog2Vo>> categoryLevel2 = getCategoryWithRedisLock();
            return categoryLevel2;
        }
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>(){});
        return result;

    }

    //加分布式锁：去数据库查询
    public Map<String, List<Catelog2Vo>> getCategoryWithRedisLock(){
        //1、生成锁的唯一标识
        String token = UUID.randomUUID().toString();
        //2、加过期锁，原子锁
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", token, 300, TimeUnit.SECONDS);
        //3、加锁成功，查询数据库
        if (lock){
            Map<String, List<Catelog2Vo>> catalogFromDb = null;
            try {
                //3.1、查询数据库
                catalogFromDb = getCatalogFromDb();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //3.2、释放锁：原子操作
                String script="if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                redisTemplate.execute(new DefaultRedisScript<Long>(script,Long.class), Arrays.asList("lock"),token);
                //3.3、返回数据
                return catalogFromDb;
            }
        }else {
            //4、加锁失败，重试，添加延迟时间
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
            return getCategoryWithRedisLock();
        }

    }

    //加本地锁：去数据库查询
    public Map<String, List<Catelog2Vo>> getCategoryWithLocalLock(){
        //加锁查询数据库
        synchronized (this){
            return getCatalogFromDb();
        }
    }


    private Map<String, List<Catelog2Vo>> getCatalogFromDb() {

        //双重验证，如果前面的线程已经查询数据了而且放到缓存中了，那么查缓存后直接返回
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)){
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>(){});
            return result;
        }
        System.out.println("查询数据库");
        // 一次性获取所有 数据
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        System.out.println("调用了 getCatalogJson  查询了数据库........【三级分类】");
        // 1）、所有1级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        Map<String, List<Catelog2Vo>> collect = null;
        if (level1Categorys != null){
            // 2）、封装数据
            collect = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), level1 -> {
                // 查到当前1级分类的2级分类
                List<CategoryEntity> category2level = getParent_cid(selectList, level1.getCatId());
                List<Catelog2Vo> catalog2Vos = null;
                if (category2level != null) {
                    catalog2Vos = category2level.stream().map(level12 -> {
                        // 查询当前2级分类的3级分类
                        List<CategoryEntity> category3level = getParent_cid(selectList, level12.getCatId());
                        List<Catelog2Vo.Catelog3Vo> catalog3Vos = null;
                        if (category3level != null) {
                            catalog3Vos = category3level.stream().map(level13 -> {
                                return new Catelog2Vo.Catelog3Vo(level12.getCatId().toString(), level13.getCatId().toString(), level13.getName());
                            }).collect(Collectors.toList());
                        }
                        return new Catelog2Vo(level1.getCatId().toString(), catalog3Vos, level12.getCatId().toString(), level12.getName());
                    }).collect(Collectors.toList());
                }
                return catalog2Vos;
            }));
        }
        String jsonString = JSON.toJSONString(collect);
        //解决缓存穿透
        if (StringUtils.isEmpty(jsonString)){
            //解决缓存雪崩
            redisTemplate.opsForValue().set("catalogJson","isNull",new Random().nextInt(180)+60, TimeUnit.SECONDS);
        }else {
            redisTemplate.opsForValue().set("catalogJson",jsonString,new Random().nextInt(18)+60, TimeUnit.HOURS);
        }
        return collect;

    }



    public List<Long> findParentPath(Long catelogId, ArrayList<Long> paths) {
        //1、收集当前节点id
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0){
            findParentPath(byId.getParentCid(), paths);
        }
        paths.add(catelogId);
        return paths;
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构

        //2.1 找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map((menu)->{
                    // 设置一级分类的子分类
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                }).sorted((menu1, menu2) -> {
                    //排序
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                })
                .collect(Collectors.toList());


        return level1Menus;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream()
                .filter(CategoryEntity -> CategoryEntity.getParentCid().equals(root.getCatId()))
                .map(categoryEntity -> {
                    //递归查找
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                })
                .collect(Collectors.toList());
        return children;
    }

}