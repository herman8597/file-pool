package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("币种行情列表对象")
public class CurrencyPriceListVo {
//    @ApiModelProperty("币种ID")
//    private Long currencyId;
    @ApiModelProperty("价格")
    private String price;
    @ApiModelProperty("涨跌幅")
    private String percent;
    @ApiModelProperty("人民币价格")
    private BigDecimal priceCNY;
    @ApiModelProperty("币种名称")
    private String symbol;
    @ApiModelProperty("币种图片")
    private String image;
}
