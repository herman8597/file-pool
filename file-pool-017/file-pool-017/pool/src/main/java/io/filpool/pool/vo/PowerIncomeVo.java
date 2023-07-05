package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("算力收益返回")
@Data
public class PowerIncomeVo {
    @ApiModelProperty("有效总算力")
    private BigDecimal totalPower;
    @ApiModelProperty("累计收益")
    private BigDecimal totalIncome;
    @ApiModelProperty("今日收益")
    private BigDecimal todayIncome;
    @ApiModelProperty("今日释放")
    private BigDecimal todayRelease;
}
