package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("确认订单请求")
public class ConfirmOrderRequest {
    @ApiModelProperty("订单number")
    private String orderNumber;
    @ApiModelProperty("支付密码")
    private String payPwd;
}
