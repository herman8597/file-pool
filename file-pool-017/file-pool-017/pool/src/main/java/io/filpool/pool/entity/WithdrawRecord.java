package io.filpool.pool.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import io.filpool.framework.common.entity.BaseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.filpool.framework.core.validator.groups.Update;

/**
 * 提币记录
 *
 * @author filpool
 * @since 2021-03-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_withdraw_record")
@ApiModel(value = "WithdrawRecord对象")
public class WithdrawRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("币种id")
    private Long currencyId;

    @ApiModelProperty("币种符号")
    private String symbol;

    @ApiModelProperty("提币数量")
    private BigDecimal amount;

    @ApiModelProperty("提币地址")
    private String toAddress;

    @ApiModelProperty("交易哈希")
    private String txHash;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("审核时间")
    private Date authTime;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("状态：1待审核 2审核通过 3审核拒绝 4提币中  5提币成功  6提币失败")
    private Integer status;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;

    @ApiModelProperty("币种系列")
    private String series;
}
