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
 * 充值记录
 *
 * @author filpool
 * @since 2021-03-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_recharge_record")
@ApiModel(value = "RechargeRecord对象")
public class RechargeRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("充值金额")
    private BigDecimal amount;

    @ApiModelProperty("交易哈希")
    private String transHash;

    @ApiModelProperty("状态：1成功，2 失败，3 等待确认")
    private Integer type;

    @ApiModelProperty("转账地址")
    private String fromAddress;

    @ApiModelProperty("收款地址")
    private String toAddress;

    @ApiModelProperty("区块号")
    private Long blockNum;

    @ApiModelProperty("币种")
    private String symbol;

    @ApiModelProperty("币种系列")
    private String series;

    @ApiModelProperty("币种系列id")
    private Long seriesId;

    @ApiModelProperty("区块哈希")
    private String blockHash;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;
}
