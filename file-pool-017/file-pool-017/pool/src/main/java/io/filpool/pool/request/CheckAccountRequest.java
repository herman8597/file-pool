package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("校验账号请求")
@Data
public class CheckAccountRequest {
    @ApiModelProperty("注册账号")
    private String account;
}
