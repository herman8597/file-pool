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
import network.vena.cooperation.util.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: auth_user
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("auth_user")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
    /**
     * 手机号
     */
    @Excel(name = "手机号", width = 15)
    private String phone;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱", width = 15)
    private String email;
    /**
     * 昵称
     */
    @Excel(name = "昵称", width = 15)
    private String nickname;
    /**
     * 用户来源:OFFICIAL=官方节点
     */
    @Excel(name = "用户来源:OFFICIAL=官方节点", width = 15)
    private String nodeSource;
    /**
     * 加秘密码
     */
    private String saltPwd;
    /**
     * 盐
     */
    private String salt;
    /**
     * 唯一api_key
     */
    @Excel(name = "唯一api_key", width = 15)
    private String apiKey;
    /**
     * 0=解锁，1=已锁定
     */
    @Excel(name = "是否锁定", width = 15, dicCode = "yes_or_no")
    private Integer blocked;
    /**
     * 0=未删除，1=已删除
     */
    @Excel(name = "是否删除", width = 15, dicCode = "yes_or_no")
    private Integer deleted;
    /**
     * 区号
     */
    @Excel(name = "区号", width = 15)
    private String areaCode;
    /**
     * createTime
     */
    @Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss ")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * updateTime
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 支付密码
     */
    private String payPwd;
    /**
     * 账号
     */
    @Excel(name = "账号", width = 15)
    private String account;
    /**
     * 1=个人用户，2=商户
     */
    @Excel(name = "角色类型", width = 15, dicCode = "role_type")
    private Integer roleType;
    /**
     * 0=不启用谷歌验证，1=谷歌验证
     */
    @Excel(name = "是否启用谷歌验证", width = 15, dicCode = "yes_or_no")
    private Integer ga;
    /**
     * 资金密码有效期：0=每笔交易均验证,1=每小时验证一次,2=每天验证一次
     */
    private Integer payPwdType;
    /**
     * 使用vean作为手续费配置项 0=否，1=是，默认1
     */
    @Excel(name = "使用vean作为手续费配置项", width = 15, dicCode = "yes_or_no")
    private Integer venaFee;
    /**
     * 手续费使用账户余额扣除 0=否，1=是，默认0
     */
    @Excel(name = "手续费使用账户余额扣除", width = 15, dicCode = "yes_or_no")
    private Integer balanceFee;
    /**
     * 0=手机号，1=email
     */
    @Excel(name = "账号类型", width = 15,dicCode = "default_account")
    private Integer defaultAccount;
    /**
     * 0=女，1=男
     */
    private Integer gender;
    /**
     * 头像
     */
    private String image;
    /**
     * gaSecret
     */
    private String gaSecret;
    /**
     * ipCount
     */
    private Integer ipCount;
    /**
     * deviceCount
     */
    private Integer deviceCount;
    /**
     * 登录锁定0=不锁，1=锁
     */
    @Excel(name = "登录锁定", width = 15, dicCode = "yes_or_no")
    @TableField(value = "`lock`")
    private Integer lock;
    /**
     * defaultAccountNo
     */
    private String defaultAccountNo;
    /**
     * 0=简体汉字，1=繁体汉字，2=英语
     */
    @Excel(name = "语言类型", width = 15,dicCode = "language_type")
    @TableField(value = "`language`")
    private Integer language;
    /**
     * 红涨绿跌的颜色
     */
    private String color;
    /**
     * currency
     */
    private String currency;
    /**
     * countryLanguage
     */
    private String countryLanguage;
    /**
     * isOtc
     */
    private Integer isOtc;
    /**
     * 0未抢购1抢购过
     */
    @Excel(name = "是否抢购过", width = 15,dicCode = "yes_or_no")
    private Integer effective;
    /**
     * 每轮抢购上限
     */
    @Excel(name = "每轮抢购上限", width = 15)
    private BigDecimal purchaseLimit;

    @TableField(exist = false,value = "level")
    @Excel(name = "等级", width = 15, dicCode = "level")
    private String level;
    @TableField(exist = false)
    @Excel(name = "邀请码", width = 15)
    private String invitationCode;

    @TableField(exist = false)
    @Excel(name = "邀请人", width = 15)
    private String inviter;

    @TableField(exist = false)
    @Excel(name = "总算力", width = 15)
    private BigDecimal hashrateTotal;
    @Excel(name = "累计人数", width = 15)
    @TableField(exist = false)
    private Integer inviteCount; //累计人数 -
    @Excel(name = "奖励", width = 15)
    @TableField(exist = false)
    private BigDecimal award; //奖励 -
    @Excel(name = "累计算力", width = 15)
    @TableField(exist = false)
    private BigDecimal distributionHashrate; //累计分销算力
    @Excel(name = "一级分销奖励", width = 15)
    @TableField(exist = false)
    private BigDecimal childrenPurchase; //一级分销奖励
    @Excel(name = "二级分销奖励", width = 15)
    @TableField(exist = false)
    private BigDecimal grandchildrenPurchase; //二级分销奖励
    @Excel(name = "市场等级", width = 15)
    @TableField(exist = false)
    private String marketLevel ;
    @Excel(name = "运营等级", width = 15)
    @TableField(exist = false)
    private String operatingLevel ;

}
