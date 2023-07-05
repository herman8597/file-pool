package io.filpool.pool.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.pool.entity.AssetAccount;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class UserVo implements Serializable {
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

    @ApiModelProperty("注册时间")
    private Date createTime;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("上级用户Id")
    private Long inviterUserId;
    @ApiModelProperty("上级用户账号")
    private String inviterAccount;

    @ApiModelProperty("用户等级: 0青铜 1白银 2黄金 3铂金 4钻石")
    private Integer levelId;

    @ApiModelProperty("用户类型: 0普通用户 1代理商 ")
    private Integer type;

    @ApiModelProperty("算力总额")
    private BigDecimal totalPower;

    @ApiModelProperty("经验值")
    private BigDecimal experience;


    //统计用户资产信息
    @ApiModelProperty
    private List<AssetAccount> assetAccount;


}
