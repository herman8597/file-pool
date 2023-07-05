package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

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
 * 邀请关系表
 *
 * @author filpool
 * @since 2021-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_invite_record")
@ApiModel(value = "InviteRecord对象")
public class InviteRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("邀请人id")
    private Long inviteUserId;

    @ApiModelProperty("邀请日期")
    private Date createTime;

    @ApiModelProperty("邀请码")
    @TableField(exist = false)
    private String inviteCode;

    @ApiModelProperty("直推人数")
    @TableField(exist = false)
    private Integer directPush;

    @ApiModelProperty("间推人数")
    @TableField(exist = false)
    private Integer interposition;

    @ApiModelProperty("返佣金额奖励")
    @TableField(exist = false)
    private BigDecimal fyUsdt;

    @ApiModelProperty("返佣算力奖励(FIL)")
    @TableField(exist = false)
    private BigDecimal fyTbFil;

    @ApiModelProperty("返佣算力奖励(XCH)")
    @TableField(exist = false)
    private BigDecimal fyTbXch;

    @ApiModelProperty("返佣算力奖励(BZZ)")
    @TableField(exist = false)
    private BigDecimal fyTbBzz;

    @ApiModelProperty("邀请关系")
    @TableField(exist = false)
    private Integer relation;

    @ApiModelProperty("订单编号")
    @TableField(exist = false)
    private String orderNumber;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;

    @ApiModelProperty("上级邀请人账号")
    @TableField(exist = false)
    private String superiorAccount;

}
