package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("用户注册请求")
@Data
public class UserRegisterRequest implements Serializable {
    @ApiModelProperty("区号")
    private String areaCode;
    @ApiModelProperty("手机号")
    private String account;
    @ApiModelProperty("密码")
    private String pwd;
    @ApiModelProperty("图片验证码")
    private String imgCode;
    @ApiModelProperty("验证码")
    private String smsCode;
    @ApiModelProperty("邀请码")
    private String inviteCode;
    @ApiModelProperty("验证token")
    private String verifyToken;
//    @ApiModelProperty("交易密码")
//    private String payPwd;
}
