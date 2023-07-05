package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import network.vena.cooperation.util.BigDecimalUtil;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("balance")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Balance implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**用户*/
	@Excel(name = "用户", width = 15)
    private String apiKey;
	/**资产类型*/
	@Excel(name = "资产类型", width = 15)
    private String asset;
	/**可用余额*/
	@Excel(name = "总资产", width = 15)
	@TableField(exist = false)
    private BigDecimal total;
	@Excel(name = "可用余额", width = 15)
    private BigDecimal available;
	/**冻结余额*/
	@Excel(name = "冻结余额", width = 15)
    private BigDecimal frozen;
	@TableField(exist = false)
    private BigDecimal pledge;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

	public void setAvailable(BigDecimal available) {
		this.available = available;
		//2021 08.03  004操作数据表扣除用户资产扣成了负数，在此将其给注释掉
//		if (BigDecimalUtil.less(this.available,BigDecimal.ZERO)){
//			throw new RuntimeException("用户资产不能为负数");
//		}
	}

	public void setFrozen(BigDecimal frozen) {
		this.frozen = frozen;
//		if (BigDecimalUtil.less(this.frozen,BigDecimal.ZERO)){
//			throw new RuntimeException("用户冻结资产不能为负数");
//		}
	}
}
