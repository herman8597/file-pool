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

import javax.naming.Name;

/**
 * @Description: balance_modify_pipeline
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("balance_modify_pipeline")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BalanceModifyPipeline implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**被划转的资产*/
	@Excel(name = "被划转的资产", width = 15)
    private String asset;
	/**划转数量*/
	@Excel(name = "划转数量", width = 15)
    private BigDecimal quantity;
	/**1=购买算力，3=充值，4=提现，5=提现中,8=内部转出,9=内部转入(34589不能占用),10=邀请奖励（锁定），11=邀请奖励（解锁），12=返佣, 13系统充币，14=活动奖励，15=系统提币，16=兑换*/
	@Excel(name = "类型", width = 15,dicCode = "balance_modify_pipeline_type")
    private Integer type;
	/**createTime*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**updateTime*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**0提现中，1成功, 2拒绝*/
	@Excel(name = "提醒状态", width = 15 ,dicCode = "balance_modify_pipeline_status")
	@TableField(value = "`status`")
    private Integer status;
	/**标记说明等*/
	@Excel(name = "标记说明等", width = 15)
    private String remark;
	/**手续费*/
	@Excel(name = "手续费", width = 15)
    private BigDecimal serviceCharge;
	/**手续费币种*/
	@Excel(name = "手续费币种", width = 15)
    private String chargeAsset;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    private String operator;
	/**流水id*/
	@Excel(name = "流水id", width = 15)
    private String pid;
	/**from*/
	@Excel(name = "转出地址", width = 15)
	@TableField(value = "`from`")
    private String from;
	/**to*/
	@Excel(name = "转入地址", width = 15)
	@TableField(value = "`to`")
    private String to;
}
