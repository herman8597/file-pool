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
import network.vena.cooperation.util.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: weight
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("weight")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Weight implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**从哪来的id*/
	@Excel(name = "从哪来的id", width = 15)
    private Integer relatedId;
	/**从哪来的名字*/
	@Excel(name = "从哪来的名字", width = 15)
    private String relatedName;
	/**购买的币种*/
	@Excel(name = "购买的币种", width = 15)
    private String asset;
	/**支付数量*/
	@Excel(name = "支付数量", width = 15)
    private BigDecimal paymentQuantity;
	/**算力数量*/
	@Excel(name = "算力数量", width = 15)
    private BigDecimal quantity;
	/**单价*/
	@Excel(name = "单价", width = 15)
    private BigDecimal price;
	/**1=购买，2=赠送，3=兑换*/
	@Excel(name = "来源类型", width = 15,dicCode = "weight_type")
    private Integer type;
	/**真的剩余天数*/
	@Excel(name = "真的剩余天数", width = 15)
    private Integer leftDays;
	/**初始剩余天数*/
	@Excel(name = "初始剩余天数", width = 15)
    private Integer initDays;
	/**技术服务手续费*/
	@Excel(name = "技术服务手续费", width = 15)
    private BigDecimal serviceChargeRate;
	/**createTime*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**updateTime*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**状态：0=未付款，1=已付款，2=未付款，3=超时*/
	@Excel(name = "付款状态", width = 15,dicCode = "weight_status")
    private Integer status;
	/**标记说明等*/
	@Excel(name = "标记说明等", width = 15)
    private String remark;
	/**relatedEnName*/
	@Excel(name = "relatedEnName", width = 15)
    private String relatedEnName;
	/**unit*/
	@Excel(name = "单位", width = 15)
    private String unit;

	@TableField( exist = false)
	@Excel(name = "邀请人", width = 15)
	private String inviter;
	@TableField( exist = false)
	@Excel(name = "账号", width = 15)
	private String account;
	@TableField( exist = false)
	@Excel(name = "用户名", width = 15)
	private String username;
	@TableField( exist = false)
	@Excel(name = "手机号", width = 15)
	private String phone;

	@Excel(name = "支付类型：1 混合支付，2 USDT支付")
	private Integer payType;

	private Integer powerType;

	public void setPaymentQuantity(BigDecimal paymentQuantity) {
		this.paymentQuantity = paymentQuantity;
		if (BigDecimalUtil.less(this.paymentQuantity , BigDecimal.ZERO)) {
			throw new RuntimeException("支付数量不能为负数");
		}

	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
		if (BigDecimalUtil.less(this.quantity , BigDecimal.ZERO)) {
			throw new RuntimeException("算力数量不能为负数");
		}
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
		if (BigDecimalUtil.less(this.price , BigDecimal.ZERO)) {
			throw new RuntimeException("单价不能为负数");
		}
	}
}
