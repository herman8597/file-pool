package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("获取账户充值地址")
@Data
public class GetRechargeAddrRequest implements Serializable {
    @ApiModelProperty("币种名称")
    private String symbol;
    @ApiModelProperty("链名称:erc20/trc20,默认为erc20")
    private String chain;
}
