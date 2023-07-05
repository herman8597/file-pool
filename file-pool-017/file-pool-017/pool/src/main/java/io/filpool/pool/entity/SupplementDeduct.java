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
 * 算力补单扣除记录
 *
 * @author filpool
 * @since 2021-04-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_supplement_deduct")
@ApiModel(value = "SupplementDeduct对象")
public class SupplementDeduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("补单编号")
    private Long orderId;

    @ApiModelProperty("订单原有算力")
    private BigDecimal tbSum;

    @ApiModelProperty("扣除算力")
    private BigDecimal deductSum;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;

    @ApiModelProperty("实际算力")
    @TableField(exist = false)
    private BigDecimal actualTB;


}
