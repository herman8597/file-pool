package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("获取商品详情请求")
public class GoodDetailRequest implements Serializable {
    @ApiModelProperty("商品id")
    private Long id;
}
