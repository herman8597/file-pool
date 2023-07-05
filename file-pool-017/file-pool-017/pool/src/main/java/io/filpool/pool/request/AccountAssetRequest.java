package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取用户资产请求")
public class AccountAssetRequest {
    @ApiModelProperty("币种id")
    private Long currencyId;
}
