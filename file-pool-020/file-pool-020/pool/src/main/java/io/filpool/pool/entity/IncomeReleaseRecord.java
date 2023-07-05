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
 * 待释放记录表
 *
 * @author filpool
 * @since 2021-03-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_income_release_record")
@ApiModel(value = "IncomeReleaseRecord对象")
public class IncomeReleaseRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("挖矿收益总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("单次释放金额")
    private BigDecimal onceAmount;

    @ApiModelProperty("剩余释放天数")
    private Integer days;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("资金账户id")
    private Long assetAccountId;

    @ApiModelProperty("剩余冻结金额")
    private BigDecimal frozenAmount;

    @ApiModelProperty("已释放总金额")
    private BigDecimal releaseAmount;

    @ApiModelProperty("平台挖矿记录id")
    private Long miningRecordId;

    @ApiModelProperty("挖矿币种")
    private String symbol;

    @ApiModelProperty("订单释放天数")
    private Integer totalDays;

    @ApiModelProperty("首次释放金额")
    private BigDecimal firstAmount;

    @ApiModelProperty("技术服务费")
    private BigDecimal serviceFee;

    @ApiModelProperty("挖矿类型 1普通用户 2代理商")
    private Integer miningType;

    @ApiModelProperty("用户可用算力")
    private BigDecimal userPower;
}
