package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("今日收益详情返回")
public class TodayIncomeVo {
    @ApiModelProperty("累计挖矿收益")
    BigDecimal totalAmount;
    @ApiModelProperty("今天收益")
    BigDecimal todayIncomeAmount;
    @ApiModelProperty("今天释放")
    BigDecimal todayReleaseAmount;
}
