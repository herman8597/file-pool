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
 * 平台挖矿记录表
 *
 * @author filpool
 * @since 2021-03-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_mining_record")
@ApiModel(value = "MiningRecord对象")
public class MiningRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("挖矿数量")
    private BigDecimal amount;

    @ApiModelProperty("矿池算力,代理商类型为代理商算力")
    private BigDecimal power;

    @ApiModelProperty("参与订单开始时间")
    private Date startTime;

    @ApiModelProperty("参与订单结束时间")
    private Date endTime;

    @ApiModelProperty("币种id")
    private Long currencyId;

    @ApiModelProperty("币种单位")
    private String symbol;

    @ApiModelProperty("用户类型：1普通用户 2代理商")
    private Integer type;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("代理商账号id")
    private Long agentUserId;

    @ApiModelProperty("代理商账号")
    private String agentAccount;

    @ApiModelProperty("参与用户人数")
    private Integer userNumber;
}
