package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("挖矿收益详情返回")
public class IncomeInfoVo {
    @ApiModelProperty("币种单位")
    private String symbol;
    @ApiModelProperty("累计挖矿收益")
    BigDecimal totalAmount;
    @ApiModelProperty("已冻结总金额")
    BigDecimal totalFrozenAmount;
    @ApiModelProperty("已释放总金额")
    BigDecimal totalReleaseAmount;
}
