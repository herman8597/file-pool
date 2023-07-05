package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("资产划转请求")
@Data
public class TransferRequest {
    @ApiModelProperty("划转类型: 1可用转质押 2质押转可用")
    private int type;
    @ApiModelProperty("划转金额")
    private BigDecimal amount;
    @ApiModelProperty("交易密码")
    private String payPwd;
}
