package io.filpool.pool.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.filpool.framework.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 算力订单表
 *
 * @author filpool
 * @since 2021-04-01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_power_order")
@ApiModel(value = "PowerOrder对象")
public class PowerOrderVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("算力数量")
    private BigDecimal amount;

    @ApiModelProperty("订单类型: 1购买云算力 2补单矿机 3补单云算力 4返佣奖励 5补单矿机集群")
    private Integer type;

    @ApiModelProperty("订单记录id")
    private Long recordId;

    @ApiModelProperty("是否有效")
    private Boolean isEffect;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("失效时间")
    private Date invalidTime;

    @ApiModelProperty("算力单位")
    private String powerSymbol;

    @ApiModelProperty("Gas费")
    private BigDecimal gasAmount;

    @ApiModelProperty("质押金额")
    private BigDecimal pledgeAmount;
}
