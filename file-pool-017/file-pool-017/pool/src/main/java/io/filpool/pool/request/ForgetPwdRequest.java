package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("忘记密码请求")
public class ForgetPwdRequest {
//    @ApiModelProperty("区号")
//    private String areaCode;
    @ApiModelProperty("手机号或邮箱地址")
    private String account;
    @ApiModelProperty("手机验证码")
    private String smsCode;
    @ApiModelProperty("新密码")
    private String pwd;
}
