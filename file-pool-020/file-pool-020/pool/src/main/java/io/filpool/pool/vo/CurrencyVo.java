package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 币种表
 *
 * @author filpay
 * @since 2020-08-19
 */
@Data
@ApiModel(value = "Currency对象")
public class CurrencyVo implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("币种名称")
    private String symbol;

    @ApiModelProperty("币种系列名称")
    private String series;

    @ApiModelProperty("币种系列id")
    private Long seriesId;

    @ApiModelProperty("合约地址")
    private String contractAddr;

    @ApiModelProperty("币种精度")
    private Integer number;

    @ApiModelProperty("币种图标")
    private String img;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("排序参数")
    private Integer orderParams;

    @ApiModelProperty("最小充值数量")
    private BigDecimal minRechargeAmount;

    @ApiModelProperty("提币手续费")
    private BigDecimal withdrawFee;

    @ApiModelProperty("是否开启充值")
    private Boolean rechargeStatus;

    @ApiModelProperty("是否开启充值")
    private Boolean withdrawStatus;
}
