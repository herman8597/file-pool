package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("质押列表")
@Data
public class PledgeListRequest {
    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("质押币种")
    private String symbol;

    @ApiModelProperty("质押要求")
    private BigDecimal pledgeRequire;

    @ApiModelProperty("质押账户总金额")
    private BigDecimal amount;

    @ApiModelProperty("消费GAS总金额")
    private BigDecimal gasSum;

}
