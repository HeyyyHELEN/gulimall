package cn.edu.hjnu.gulimall.member.dao;

import cn.edu.hjnu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:52:41
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
