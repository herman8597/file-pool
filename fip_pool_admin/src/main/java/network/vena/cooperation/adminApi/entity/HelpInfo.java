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
 * @Description: help_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("help_info")
public class HelpInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**0-挖矿配置，1-新手引导*/
	@Excel(name = "0-挖矿配置，1-新手引导", width = 15)
    private Integer type;
	/**title*/
	@Excel(name = "title", width = 15)
    private String title;
	/**enTitle*/
	@Excel(name = "enTitle", width = 15)
    private String enTitle;
	/**description*/
	@Excel(name = "description", width = 15)
    private String description;
	/**enDescription*/
	@Excel(name = "enDescription", width = 15)
    private String enDescription;
	/**image*/
	@Excel(name = "image", width = 15)
    private String image;
	/**content*/
	@Excel(name = "content", width = 15)
    private String content;
	/**rank*/
	@Excel(name = "rank", width = 15)
    private Integer rank;
	/**ifUse*/
	@Excel(name = "ifUse", width = 15)
    private Integer ifUse;
	/**updateTime*/
	@Excel(name = "updateTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**clickCount*/
	@Excel(name = "clickCount", width = 15)
    private Integer clickCount;
}
