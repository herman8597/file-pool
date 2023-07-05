package io.filpool.pool.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 币种表
 *
 * @author filpool
 * @since 2021-03-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Currency对象")
public class Currency extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("币种名称")
    private String symbol;

    @ApiModelProperty("币种系列名称")
    private String series;

    @ApiModelProperty("币种全称")
    private String name;

    @ApiModelProperty("币种系列id")
    private Long seriesId;

    @NotNull(message = "最小充值数量不能为空")
    @ApiModelProperty("最小充值数量")
    private BigDecimal minRechargeAmount;

    @ApiModelProperty("最小归集数")
    private BigDecimal minCollectAmount;

    @ApiModelProperty("归集地址（ETH系列设置）")
    private String collectAddr;

    @NotNull(message = "提币手续费不能为空")
    @ApiModelProperty("提币手续费")
    private BigDecimal withdrawFee;

    @ApiModelProperty("合约地址")
    private String contractAddr;

    @ApiModelProperty("币种精度")
    private Integer number;

    @ApiModelProperty("币种图标")
    private String img;

    @ApiModelProperty("排序参数")
    private Integer orderParams;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("是否可以充值")
    private Boolean rechargeStatus;

    @ApiModelProperty("是否可以提现")
    private Boolean withdrawStatus;

}
