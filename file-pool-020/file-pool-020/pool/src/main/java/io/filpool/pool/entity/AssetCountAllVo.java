package io.filpool.pool.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetCountAllVo {

    @ApiModelProperty("币种名称")
    private String symbol;

    @ApiModelProperty("币种数量")
    private BigDecimal quantity;

}
