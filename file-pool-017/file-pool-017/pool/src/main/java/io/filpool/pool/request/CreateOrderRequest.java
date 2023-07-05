package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建订单请求")
public class CreateOrderRequest {
    @ApiModelProperty("商品id")
    private Long goodId;
    @ApiModelProperty("购买数量")
    private Integer quantity;
}
