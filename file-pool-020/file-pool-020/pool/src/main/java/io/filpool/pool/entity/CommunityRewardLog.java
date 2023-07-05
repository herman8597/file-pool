package io.filpool.pool.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

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
 * 社区(市场)奖励记录表
 *
 * @author filpool
 * @since 2021-06-01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_community_reward_log")
@ApiModel(value = "CommunityRewardLog对象")
public class CommunityRewardLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("购买人的用户id")
    private Long buyerUserId;

    @ApiModelProperty("拿奖励的用户id")
    private Long rewardUserId;

    @ApiModelProperty("拿奖励用户当前社区等级")
    private Integer rewardUserLevel;

    @ApiModelProperty("拿奖励用户的下级用户id")
    private Long downUserId;

    @ApiModelProperty("拿奖励用户的下级用户当前社区等级")
    private Integer downUserLevel;

    @ApiModelProperty("社区usdt极差奖励")
    private BigDecimal usdtRangeReward;

    @ApiModelProperty("社区usdt平级奖励")
    private BigDecimal usdtLevelLeward;

    @ApiModelProperty("算力单位（购买xch算力还是fil算力）算力奖励类型")
    private String powerSymbol;

    @ApiModelProperty("社区算力极差奖励")
    private BigDecimal powerRangeReward;

    @ApiModelProperty("社区算力平级奖励")
    private BigDecimal powerLevelReward;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String rewardUserAccount;

    @ApiModelProperty("下级账号")
    @TableField(exist = false)
    private String downUserAccount;

    @ApiModelProperty("购买人账号")
    @TableField(exist = false)
    private String buyerUserAccount;

    @ApiModelProperty("极差算力xch")
    @TableField(exist = false)
    private BigDecimal powerRangeXchReward;

    @ApiModelProperty("订单流水")
    private String orderNumber;

    @ApiModelProperty("奖励USDT总额")
    @TableField(exist = false)
    private BigDecimal usdtReward;

    @ApiModelProperty("奖励算力总额")
    @TableField(exist = false)
    private BigDecimal powerReward;

    @ApiModelProperty("奖励类型：1:购买商品的奖励，2:补单时候的奖励")
    private Integer rewardType;

}
