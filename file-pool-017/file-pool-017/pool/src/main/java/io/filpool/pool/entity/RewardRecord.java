package io.filpool.pool.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 订单奖励记录
 *
 * @author filpool
 * @since 2021-03-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_reward_record")
@ApiModel(value = "RewardRecord对象")
public class RewardRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("奖励usdt数量")
    private BigDecimal usdtAmount;

    @ApiModelProperty("奖励算力数量")
    private BigDecimal powerAmount;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("下级用户id(购买的用户)")
    private Long downUserId;

    @ApiModelProperty("状态: 0未处理 1已处理")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("奖励类型: 1邀请用户订单返佣  2补单返佣")
    private Integer type;

    @ApiModelProperty("邀请关系: 1直推 2间推")
    private Integer inviteType;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("订单id")
    private Long orderId;
}
