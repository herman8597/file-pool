package io.filpool.pool.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 奖励等级配置
 *
 * @author filpool
 * @since 2021-04-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_reward_config")
@ApiModel(value = "RewardConfig对象")
public class RewardConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("等级名称")
    private String name;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("最小团队购买算力(1TB=1经验值)")
    private Integer minSize;

    @ApiModelProperty("算力直推usdt返佣比例")
    private BigDecimal powerOneUsdtRate;

    @ApiModelProperty("算力直推算力返佣比例")
    private BigDecimal powerOnePowerRate;

    @ApiModelProperty("算力间推算力返佣比例")
    private BigDecimal powerTwoPowerRate;

    @ApiModelProperty("算力间推usdt返佣比例")
    private BigDecimal powerTwoUsdtRate;

    @ApiModelProperty("矿机直推usdt返佣比例")
    private BigDecimal minerOneUsdtRate;

    @ApiModelProperty("矿机直推算力返佣比例")
    private BigDecimal minerOnePowerRate;

    @ApiModelProperty("矿机间推算力返佣比例")
    private BigDecimal minerTwoPowerRate;

    @ApiModelProperty("矿机间推usdt返佣比例")
    private BigDecimal minerTwoUsdtRate;

    @ApiModelProperty("集群直推usdt返佣比例")
    private BigDecimal groupOneUsdtRate;

    @ApiModelProperty("集群直推算力返佣比例")
    private BigDecimal groupOnePowerRate;

    @ApiModelProperty("集群间推算力返佣比例")
    private BigDecimal groupTwoPowerRate;

    @ApiModelProperty("集群间推usdt返佣比例")
    private BigDecimal groupTwoUsdtRate;

}
