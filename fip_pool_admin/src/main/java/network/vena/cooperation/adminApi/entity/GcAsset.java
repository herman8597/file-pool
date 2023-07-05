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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: gc_asset
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("gc_asset")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GcAsset implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**asset*/
	@Excel(name = "货币", width = 15)
    private String asset;
	/**blockChain*/
	@Excel(name = "主链", width = 15)
    private String blockChain;
	/**decimals*/
    private Integer decimals;
	/**baseContractAssetId*/
    private String baseContractAssetId;
	/**类型：BASE(该链的基本币),OMNI,ERC10,ERC20,TRX,TRC10,TRC20*/
	@Excel(name = "类型", width = 15)
    private String type;
	/**0=未锁定，1=锁定*/
	@Excel(name = "是否锁定", width = 15 ,dicCode = "yes_or_no")
    private Integer locked;
	/**图标*/
	@Excel(name = "图标", width = 15)
    private String icon;
	/**英文名*/
	@Excel(name = "英文名", width = 15)
    private String enName;
	/**中文名*/
	@Excel(name = "中文名", width = 15)
    private String chName;
	/**createTime*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**withdrawalFeeRate*/
	@Excel(name = "提现费用", width = 15)
    private BigDecimal withdrawalFeeRate;
	/**minFee*/
	@Excel(name = "最小提现费用", width = 15)
    private BigDecimal minFee;
	/**区块确认块数*/
    private Integer height;
	/**minWithdraw*/
	@Excel(name = "最小提现数", width = 15)
    private BigDecimal minWithdraw;
	/**updateTime*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**0=不可充值，1=可充值*/
	@Excel(name = "是否可充值", width = 15,dicCode = "yes_or_no")
    private Integer deposit;
	/**0=不可提现，1=可提现*/
	@Excel(name = "是否可提现", width = 15,dicCode = "yes_or_no")
    private Integer withdraw;
	/**contractInfo*/
    private String contractInfo;
	/**支持的交易的精度,小数点后几位*/
    private Integer exchangeDecimals;
	/**fiat*/
    private Integer fiat;
	/**spot*/
    private Integer spot;
	/**withdrawalTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date withdrawalTime;
	/**chargeTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date chargeTime;
	/**1-支持基础货币 0-不支持*/
	@Excel(name = "是否支持基础货币", width = 15)
    private Integer baseCurrency;
	/**1-支持计价货币 0-不支持*/
	@Excel(name = "是否支持计价货币", width = 15)
    private Integer quoteCurrency;
	/**全称*/
	@Excel(name = "全称", width = 15)
    private String name;
	/**minDeposit*/
	@Excel(name = "最小充值", width = 15)
    private BigDecimal minDeposit;
	/**体现手续费币种*/
	@Excel(name = "提现手续费币种", width = 15)
    private String feeAsset;
	/**提现免审金额，-1=必须审核*/
    private BigDecimal withdrawalPassAmount;
	/**internalTransferFee*/
    private BigDecimal internalTransferFee;
	/**是否允许购买使用*/
	@Excel(name = "是否允许购买使用", width = 15,dicCode = "yes_or_no")
    private Integer purchaseFlag;
	/**前端展示顺序*/
	@Excel(name = "前端展示顺序", width = 15)
	@TableField("`index`")
    private Integer index;

}
