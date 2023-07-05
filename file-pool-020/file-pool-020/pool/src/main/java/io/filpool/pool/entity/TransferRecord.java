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
 * 用户划转记录
 *
 * @author filpool
 * @since 2021-03-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_transfer_record")
@ApiModel(value = "TransferRecord对象")
public class TransferRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("操作类型: 1系统划转 2用户划转")
    private Integer operationType;

    @ApiModelProperty("质押金额")
    private BigDecimal amount;

    @ApiModelProperty("币种id")
    private Long currencyId;

    @ApiModelProperty("划转类型: 1可用转质押 2质押转可用 3购买商品划转质押 4补单可用转质押")
    private Integer type;

    @ApiModelProperty("币种名称")
    private String symbol;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;

    @ApiModelProperty("质押要求")
    @TableField(exist = false)
    private BigDecimal pledgeRequire;

    @ApiModelProperty("云算力订单id")
    private Long orderId;
}
