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
 * @Description: deposit
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("deposit")
public class Deposit implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**iden*/
	@Excel(name = "iden", width = 15)
    private Integer iden;
	/**address*/
	@Excel(name = "address", width = 15)
    private String address;
	/**amount*/
	@Excel(name = "amount", width = 15)
    private BigDecimal amount;
	/**txHash*/
	@Excel(name = "txHash", width = 15)
    private String txHash;
	/**txIndex*/
	@Excel(name = "txIndex", width = 15)
    private Integer txIndex;
	/**outputIndex*/
	@Excel(name = "outputIndex", width = 15)
    private Integer outputIndex;
	/**blockHeight*/
	@Excel(name = "blockHeight", width = 15)
    private Integer blockHeight;
	/**txTime*/
	@Excel(name = "txTime", width = 15)
    private BigDecimal txTime;
	/**asset*/
	@Excel(name = "asset", width = 15)
    private String asset;
	/**到账状态：1=已发现，2=已通知给交易所待入账*/
	@Excel(name = "到账状态：1=已发现，2=已通知给交易所待入账", width = 15)
    private Integer status;
	/**createTime*/
	@Excel(name = "createTime", width = 15)
    private BigDecimal createTime;
	/**blockChain*/
	@Excel(name = "blockChain", width = 15)
    private String blockChain;
	/**contractAddress*/
	@Excel(name = "contractAddress", width = 15)
    private String contractAddress;
	/**createTimestamp*/
	@Excel(name = "createTimestamp", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTimestamp;
	/**completedTimestamp*/
	@Excel(name = "completedTimestamp", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date completedTimestamp;
	/**collectHash*/
	@Excel(name = "collectHash", width = 15)
    private String collectHash;
	/**1=未归集，2=归集已上链，3=归集已确认成功，0=归集失败*/
	@Excel(name = "1=未归集，2=归集已上链，3=归集已确认成功，0=归集失败", width = 15)
    private Integer collectStatus;
	/**collectTime*/
	@Excel(name = "collectTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date collectTime;
	/**collectCompleteTime*/
	@Excel(name = "collectCompleteTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date collectCompleteTime;
}
