package cn.edu.hjnu.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * å•†å“ä¸‰çº§åˆ†ç±»
 * 
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 13:07:46
 */
@Data
@TableName("pms_category")
public class PmsCategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * åˆ†ç±»id
	 */
	@TableId
	private Long catId;
	/**
	 * åˆ†ç±»åç§°
	 */
	private String name;
	/**
	 * çˆ¶åˆ†ç±»id
	 */
	private Long parentCid;
	/**
	 * å±‚çº§
	 */
	private Integer catLevel;
	/**
	 * æ˜¯å¦æ˜¾ç¤º[0-ä¸æ˜¾ç¤ºï¼Œ1æ˜¾ç¤º]
	 */
	private Integer showStatus;
	/**
	 * æŽ’åº
	 */
	private Integer sort;
	/**
	 * å›¾æ ‡åœ°å€
	 */
	private String icon;
	/**
	 * è®¡é‡å•ä½
	 */
	private String productUnit;
	/**
	 * å•†å“æ•°é‡
	 */
	private Integer productCount;

}
