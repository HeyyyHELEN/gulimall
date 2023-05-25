package cn.edu.hjnu.gulimall.product.web;

import cn.edu.hjnu.gulimall.product.entity.CategoryEntity;
import cn.edu.hjnu.gulimall.product.service.CategoryService;
import cn.edu.hjnu.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        //查出所有的1级分类
        List<CategoryEntity> categoryEntites = categoryService.getLevel1Categorys();
        model.addAttribute("categorys",categoryEntites);
        return "index";
    }

    //index/catalog.json
    /**
     * 查出三级分类
     * 1级分类作为key，2级引用List
     */
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> map = categoryService.getCatalogJsonBySpringCache();
        return map;
    }


    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        //获取一把锁，只要锁的名字一样，就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        //加锁
        lock.lock(20, TimeUnit.SECONDS);
        try {
            System.out.println("加锁成功，执行业务" + Thread.currentThread().getId());
            Thread.sleep(10000);
        }catch (Exception e){

        }finally {
            //解锁
            System.out.println("释放锁..." + Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";

    }


    @RequestMapping("/write")
    @ResponseBody
    public String writeValue(){
        RReadWriteLock rw_lock = redisson.getReadWriteLock("rw_lock");
        String s = "";
        RLock rLock = rw_lock.writeLock();
        try {
            rLock.lock();
            System.out.println("写锁加锁成功"+Thread.currentThread().getId());
            s = UUID.randomUUID().toString();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e){
                e.printStackTrace();
            }
            redisTemplate.opsForValue().set("writeValue",s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
            System.out.println("写锁释放"+Thread.currentThread().getId());
        }
        return s;
    }


    @RequestMapping("/read")
    @ResponseBody
    public String readValue(){
        RReadWriteLock rw_lock = redisson.getReadWriteLock("rw_lock");
        String s = "";
        RLock rLock = rw_lock.readLock();
        try {
            rLock.lock();
            System.out.println("读锁加锁成功"+Thread.currentThread().getId());
            s = (String) redisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
            System.out.println("读锁释放"+Thread.currentThread().getId());
        }
        return s;
    }

    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();
        return "放假了";
    }

    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id){
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();


        return id + "班的人走完了";
    }


}
