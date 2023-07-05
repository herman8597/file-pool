package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("谷歌绑定请求")
public class GoogleBindRequest {
    @ApiModelProperty("新谷歌密钥")
    private String googleSecretKey;
    @ApiModelProperty("新谷歌验证码")
    private String googleCode;
    @ApiModelProperty("旧谷歌校验码,修改时要传")
    private String verityToken;
}
