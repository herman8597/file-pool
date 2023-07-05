package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("币种列表请求")
@Data
public class CurrencyListRequest implements Serializable {
    @ApiModelProperty("是否为提币")
    private Boolean isWithdraw;
}
