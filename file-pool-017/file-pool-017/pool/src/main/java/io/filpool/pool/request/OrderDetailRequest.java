package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("订单详情请求")
public class OrderDetailRequest implements Serializable {
    @ApiModelProperty("订单id")
    private Integer orderId;
}
