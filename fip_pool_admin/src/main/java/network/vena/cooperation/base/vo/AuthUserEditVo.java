package network.vena.cooperation.base.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: auth_user
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("auth_user")
public class AuthUserEditVo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    private String phone;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    private String email;
	/**账号*/
	@Excel(name = "账号", width = 15)
    private String account;
	/**昵称*/
	@Excel(name = "昵称", width = 15)
    private String nickname;
	/**用户来源:OFFICIAL=官方节点*/
	@Excel(name = "用户来源:OFFICIAL=官方节点", width = 15)
    private String nodeSource;

    private String invitationCode;
    private String inviter;
    private Integer level;
	/*createTime*/
	@Excel(name = "createTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

	@Excel(name = "登录锁定0=不锁，1=锁", width = 15)
	private Integer lock;
	@Excel(name = "头像", width = 15)
	private String image;


	/**加秘密码*/
	/*@Excel(name = "加秘密码", width = 15)
    private String saltPwd;
	*//**盐*//*
	@Excel(name = "盐", width = 15)
    private String salt;
	*//**唯一api_key*/
	@Excel(name = "唯一api_key", width = 15)
    private String apiKey;
	/**0=解锁，1=已锁定*/
	@Excel(name = "0=解锁，1=已锁定", width = 15)
    private Integer blocked;
	private String gaSecret;
	/**0=未删除，1=已删除*//*
	@Excel(name = "0=未删除，1=已删除", width = 15)
    private Integer deleted;
	*//**区号*//*
	@Excel(name = "区号", width = 15)
    private String areaCode;
	*//**createTime*//*
	@Excel(name = "createTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	*//**updateTime*//*
	@Excel(name = "updateTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
	*//**支付密码*//*
	@Excel(name = "支付密码", width = 15)
    private String payPwd;
	*//**账号*//*
	@Excel(name = "账号", width = 15)
    private String account;
	*//**1=个人用户，2=商户*//*
	@Excel(name = "1=个人用户，2=商户", width = 15)
    private Integer roleType;
	*//**0=不启用谷歌验证，1=谷歌验证*//*
	@Excel(name = "0=不启用谷歌验证，1=谷歌验证", width = 15)
    private Integer ga;
	*//**资金密码有效期：0=每笔交易均验证,1=每小时验证一次,2=每天验证一次*//*
	@Excel(name = "资金密码有效期：0=每笔交易均验证,1=每小时验证一次,2=每天验证一次", width = 15)
    private Integer payPwdType;
	*//**使用vean作为手续费配置项 0=否，1=是，默认1*//*
	@Excel(name = "使用vean作为手续费配置项 0=否，1=是，默认1", width = 15)
    private Integer venaFee;
	*//**手续费使用账户余额扣除 0=否，1=是，默认0*//*
	@Excel(name = "手续费使用账户余额扣除 0=否，1=是，默认0", width = 15)
    private Integer balanceFee;
	*//**0=手机号，1=email*//*
	@Excel(name = "0=手机号，1=email", width = 15)
    private Integer defaultAccount;
	*//**0=女，1=男*//*
	@Excel(name = "0=女，1=男", width = 15)
    private Integer gender;
	*//**头像*//*
	@Excel(name = "头像", width = 15)
    private String image;
	*//**gaSecret*//*
	@Excel(name = "gaSecret", width = 15)
    private String gaSecret;
	*//**ipCount*//*
	@Excel(name = "ipCount", width = 15)
    private Integer ipCount;
	*//**deviceCount*//*
	@Excel(name = "deviceCount", width = 15)
    private Integer deviceCount;
	*//**登录锁定0=不锁，1=锁*//*
	@Excel(name = "登录锁定0=不锁，1=锁", width = 15)
    private Integer lock;
	*//**defaultAccountNo*//*
	@Excel(name = "defaultAccountNo", width = 15)
    private String defaultAccountNo;
	*//**0=简体汉字，1=繁体汉字，2=英语*//*
	@Excel(name = "0=简体汉字，1=繁体汉字，2=英语", width = 15)
    private Integer language;
	*//**红涨绿跌的颜色*//*
	@Excel(name = "红涨绿跌的颜色", width = 15)
    private String color;
	*//**currency*//*
	@Excel(name = "currency", width = 15)
    private String currency;
	*//**countryLanguage*//*
	@Excel(name = "countryLanguage", width = 15)
    private String countryLanguage;
	*//**isOtc*//*
	@Excel(name = "isOtc", width = 15)
    private Integer isOtc;
	*//**0未抢购1抢购过*//*
	@Excel(name = "0未抢购1抢购过", width = 15)
    private Integer effective;
	*//**每轮抢购上限*//*
	@Excel(name = "每轮抢购上限", width = 15)
    private BigDecimal purchaseLimit;*/
}
