package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "图片验证码校验参数")
public class VerifyImageCodeParam {
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("校验token")
    private String verifyToken;
}
