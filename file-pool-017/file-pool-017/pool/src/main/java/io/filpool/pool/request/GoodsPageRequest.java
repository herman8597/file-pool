package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取商品分页请求")
public class GoodsPageRequest extends PageRequest{
    @ApiModelProperty("商品类型: 1矿机 2云算力 3矿机集群不能为空")
    private Integer type;
    @ApiModelProperty("挖矿币种")
    private Integer currencyId;
}
