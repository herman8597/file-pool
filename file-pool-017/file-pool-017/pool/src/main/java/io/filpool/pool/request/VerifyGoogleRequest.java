package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("校验谷歌验证码请求")
public class VerifyGoogleRequest {
    @ApiModelProperty("谷歌验证码")
    private String googleCode;
}
