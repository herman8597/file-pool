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

import javax.validation.constraints.NotNull;

/**
 * 算力补单
 *
 * @author filpool
 * @since 2021-03-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_supplement")
@ApiModel(value = "Supplement对象")
public class Supplement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty("id,补单编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long uId;

    @ApiModelProperty("补单类型 1云算力 2矿机 3矿机集群")
    private Integer type;

    @ApiModelProperty("算力数量")
    private BigDecimal tbSum;

    @ApiModelProperty("支付方式(支付币种类型||币种id)")
    private Long assetId;

    @ApiModelProperty("支付金额")
    private BigDecimal amountPrice;

    @ApiModelProperty("质押金额(FIL)")
    private BigDecimal pledgePrice;

    @ApiModelProperty("gas费数量")
    private BigDecimal gasPrice;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("完成时间")
    private Date updateTime;

    @ApiModelProperty("状态:待审核0，通过1，拒绝2")
    private Integer status;

    @TableField(exist = false)
    @ApiModelProperty("用户账号")
    private String account;

    @TableField(exist = false)
    @ApiModelProperty("币种名称")
    private String assetName;

    @ApiModelProperty("合约天数")
    private Integer contractDays;

    @ApiModelProperty("补单审核备注")
    private String remark;
}
