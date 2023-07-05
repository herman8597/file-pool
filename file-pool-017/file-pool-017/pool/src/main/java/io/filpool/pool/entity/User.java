package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.filpool.framework.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.filpool.framework.core.validator.groups.Update;

/**
 * 用户表
 *
 * @author filpool
 * @since 2021-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_user")
@ApiModel(value = "User对象")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("手机区号")
    private String areaCode;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("密码盐")
    private String salt;

    @ApiModelProperty("支付密码")
    private String payPassword;

    @ApiModelProperty("有效的token")
    private String token;

    @ApiModelProperty("注册时间")
    private Date createTime;

    @ApiModelProperty("谷歌验证码是否开启")
    @TableField("googleAuthIsOpen")
    private Boolean googleAuthIsOpen;

    @ApiModelProperty("谷歌验证码密钥")
    @TableField("googleSecretKey")
    private String googleSecretKey;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("实名认证状态: 0:未提交，1已提交，2审核失败 3审核通过")
    @TableField("realNameStatus")
    private Integer realNameStatus;

    @ApiModelProperty("用户等级: 0青铜 1白银 2黄金 3铂金 4钻石")
    @TableField(exist = false)
    private Integer levelId;

    @ApiModelProperty("用户类型: 0普通用户 1代理商 ")
    private Integer type;

    @ApiModelProperty("质押要求")
    private BigDecimal pledgeRequire;

    @ApiModelProperty("技术服务费")
    private BigDecimal serverCharge;

    @ApiModelProperty("经验值")
    private BigDecimal experience;

    @ApiModelProperty("邀请海报")
    private String invitePoster;
}
