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
 * 账户资产变化表
 *
 * @author filpool
 * @since 2021-03-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_asset_account_log")
@ApiModel(value = "AssetAccountLog对象")
public class AssetAccountLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("注册日志")
    private Date createTime;

    @ApiModelProperty("操作金额")
    private BigDecimal operationAmount;

    @ApiModelProperty("操作后可用")
    private BigDecimal available;

    @ApiModelProperty("操作后冻结金额")
    private BigDecimal frozen;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("资产账户id")
    private Long assetAccountId;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("说明")
    private String remark;

    @ApiModelProperty("操作后质押金额")
    private BigDecimal pledge;

    @ApiModelProperty("单位")
    @TableField(exist = false)
    private String symbol;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;



    @ApiModelProperty("记录id")
    private Long recordId;
}
