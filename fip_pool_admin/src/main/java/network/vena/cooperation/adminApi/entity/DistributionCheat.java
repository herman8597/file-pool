package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: distribution_cheat
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("distribution_cheat")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DistributionCheat implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**可以到的等级上限3=铂金，4=钻石*/
	@Excel(name = "可以到的等级上限3=铂金，4=钻石", width = 15)
    private Integer levelLimit;
	/**直接指定等级，无视所有规则 0-铜牌，1-银牌，2-金牌，3-铂金，4-钻石*/
	@Excel(name = "直接指定等级，无视所有规则 0-铜牌，1-银牌，2-金牌，3-铂金，4-钻石", width = 15)
    private Integer levelDirect;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
