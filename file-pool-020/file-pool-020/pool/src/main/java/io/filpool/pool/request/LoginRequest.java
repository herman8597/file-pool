package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("登录")
@Data
public class LoginRequest {
    @ApiModelProperty("手机或邮箱")
    private String account;
    @ApiModelProperty("密码")
    private String pwd;
//    @ApiModelProperty("区号")
//    private String areaCode;
}
