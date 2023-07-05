package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("扣除算力补单请求")
public class SysDeductSupRequest {
    @ApiModelProperty("用户账号")
    private String account;
    @ApiModelProperty("订单编号")
    private Long id;
    @ApiModelProperty("扣除算力数量")
    private BigDecimal amount;
}
