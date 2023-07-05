package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("算力管理有效返回")
public class TotalPowerVo {
    @ApiModelProperty("算力单位")
    private String symbol;
    @ApiModelProperty("有效总算力")
    private BigDecimal totalPower;
}
