package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("资金请求")
@Data
public class AssetAccountReq extends PageRequest{
    @ApiModelProperty("用户id")
    private Long userId;
}
