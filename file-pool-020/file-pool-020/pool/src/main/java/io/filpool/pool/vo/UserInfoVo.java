package io.filpool.pool.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.filpool.framework.core.validator.groups.Update;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserInfoVo implements Serializable {
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
    @ApiModelProperty("谷歌验证码是否开启")
    @TableField("googleAuthIsOpen")
    private Boolean googleAuthIsOpen;
    @ApiModelProperty("实名认证状态: 0:未提交，1已提交，2审核失败 3审核通过")
    @TableField("realNameStatus")
    private Integer realNameStatus;
    @ApiModelProperty("是否设置交易密码")
    private Boolean havePayPwd;
    @ApiModelProperty("有效的token")
    private String token;
    @ApiModelProperty("用户等级: 0青铜 1白银 2黄金 3铂金 4钻石")
    private Integer levelId;
    @ApiModelProperty("用户类型: 0普通用户 1代理商 ")
    private Integer type;
    @ApiModelProperty("经验值")
    private BigDecimal experience;
    @ApiModelProperty("社区等级经验值")
    private BigDecimal communityExperience;
    @ApiModelProperty("社区等级")
    private Integer communityLevelId;
}
