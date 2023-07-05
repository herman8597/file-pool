package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单分页请求")
public class OrderPageRequest extends PageRequest{
    @ApiModelProperty("订单类型 :1待付款 2已完成 3已取消")
    private Integer status;
}
