package io.filpool.pool.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import io.filpool.framework.common.entity.BaseEntity;
import io.filpool.framework.common.exception.FILPoolException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.filpool.framework.core.validator.groups.Update;

/**
 * 账户资产表
 *
 * @author filpool
 * @since 2021-03-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_asset_account")
@ApiModel(value = "AssetAccount对象")
public class AssetAccount extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("币种id")
    private Long currencyId;

    @ApiModelProperty("可用")
    private BigDecimal available;

    public void setAvailable(BigDecimal available) throws FILPoolException {
        if (available.compareTo(BigDecimal.ZERO) < 0)
            throw new FILPoolException("transfer.account.balance-low");
        this.available = available;
    }

    @ApiModelProperty("冻结")
    private BigDecimal frozen;

    @ApiModelProperty("质押(FIL)")
    private BigDecimal pledge;

    @ApiModelProperty("待释放数量,挖矿冻结")
    @TableField(exist = false)
    private BigDecimal minerFrozen;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("地址id")
    private Long addressId;

    @ApiModelProperty("币种符号")
    private String symbol;

    @ApiModelProperty("币种图标")
    private String img;

    @ApiModelProperty("总资产")
    @TableField(exist = false)
    private BigDecimal totalAssets;
}
