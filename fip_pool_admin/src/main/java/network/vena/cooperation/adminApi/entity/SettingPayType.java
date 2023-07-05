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
 * @Description: setting_pay_type
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("setting_pay_type")
public class SettingPayType implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**BANK=银行卡，ALIPAY=支付宝，WECHAT=微信支付*/
	@Excel(name = "BANK=银行卡，ALIPAY=支付宝，WECHAT=微信支付", width = 15)
    private String type;
	/**账号：银行卡账号，微信账号，支付宝账号*/
	@Excel(name = "账号：银行卡账号，微信账号，支付宝账号", width = 15)
    private String accountNo;
	/**开户支行*/
	@Excel(name = "开户支行", width = 15)
    private String bankBranchName;
	/**银行名*/
	@Excel(name = "银行名", width = 15)
    private String bankName;
	/**alipayImg*/
	@Excel(name = "alipayImg", width = 15)
    private String alipayImg;
	/**wechatImg*/
	@Excel(name = "wechatImg", width = 15)
    private String wechatImg;
	/**0=未删除，1=已删除*/
	@Excel(name = "0=未删除，1=已删除", width = 15)
    private Integer deleted;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**name*/
	@Excel(name = "name", width = 15)
    private String name;
}
