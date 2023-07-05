package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: distribution_config
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("distribution_config")
public class DistributionConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**key*/
	@Excel(name = "key", width = 15)
	@TableField("`key`")
    private String key;
	/**description*/
	@Excel(name = "description", width = 15)
    private String description;
	/**value1*/
	@Excel(name = "value1", width = 15)
    private String value1;
	/**value2*/
	@Excel(name = "value2", width = 15)
    private String value2;
	/**value3*/
	@Excel(name = "value3", width = 15)
    private String value3;
	/**value4*/
	@Excel(name = "value4", width = 15)
    private String value4;
	/**value5*/
	@Excel(name = "value5", width = 15)
    private String value5;
	/**value6*/
	@Excel(name = "value6", width = 15)
    private String value6;
	/**updateTime*/
	@Excel(name = "updateTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
