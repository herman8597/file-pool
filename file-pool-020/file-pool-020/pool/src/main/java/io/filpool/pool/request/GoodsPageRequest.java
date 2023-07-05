package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取商品分页请求")
public class GoodsPageRequest extends PageRequest{
    @ApiModelProperty("商品类型: 1矿机 2云算力 3矿机集群不能为空")
    private Integer type;
    @ApiModelProperty("是否为首页商品")
    private Boolean isHome;
    @ApiModelProperty("挖矿币种")
    private Integer currencyId;

    @ApiModelProperty("022新需求，商品是否专题显示（0：不显示，1显示）")
    private Integer specialShow;
}
