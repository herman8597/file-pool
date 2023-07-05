package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("添加算力质押配置请求")
public class SysPowerPledgeRequest {
    @ApiModelProperty("每TB质押金额")
    private BigDecimal tbNeedAmount;
    @ApiModelProperty("每TB消耗GAS费用")
    private BigDecimal tbGasFee;
}
