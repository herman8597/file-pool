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
 * @Description: gc_deposit_address
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("gc_deposit_address")
public class GcDepositAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**address*/
	@Excel(name = "address", width = 15)
    private String address;
	/**asset*/
	@Excel(name = "asset", width = 15)
    private String asset;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**inUse*/
	@Excel(name = "inUse", width = 15)
    private Integer inUse;
	/**blockChain*/
	@Excel(name = "blockChain", width = 15)
    private String blockChain;

	private String secret;
}
