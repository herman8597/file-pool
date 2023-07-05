package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: banner_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("banner_info")
public class BannerInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**description*/
	@Excel(name = "说明", width = 15)
    private String description;
	/**image*/
	@Excel(name = "图片", width = 15)
    private String image;
	/**webImage*/
	@Excel(name = "pc端图片", width = 15)
    private String webImage;
	/**ifButton*/
	@Excel(name = "ifButton", width = 15)
    private Integer ifButton;
	/**按钮颜色*/
	@Excel(name = "按钮颜色", width = 15)
    private String buttonColor;
	/**按钮文案*/
	@Excel(name = "按钮文案", width = 15)
    private String buttonText;
	/**跳转链接*/
	@Excel(name = "跳转链接", width = 15)
    private String content;
	/**是否置顶*/
	@Excel(name = "是否置顶", width = 15,dicCode = "yes_or_no")
    private Integer ifTop;
	/**banner顺序--1,2,3,4,5,6,7*/
	@Excel(name = "banner顺序", width = 15)
    private Integer rank;
	/**生效时间*/
	@Excel(name = "生效时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**是否启用--0不启用/1启用；同一位置同一顺序，只有一个1*/
	@Excel(name = "是否启用", width = 15 ,dicCode = "yes_or_no")
    private Integer ifUse;
	/**updateTime*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
