package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("发送验证码")
@Data
public class SendRegisterCodeRequest {
    @ApiModelProperty("区号")
    private String areaCode;
    @ApiModelProperty("注册账号")
    private String account;
    @ApiModelProperty("图片验证码")
    private String imgCode;
    @ApiModelProperty("验证token")
    private String verifyToken;
}
