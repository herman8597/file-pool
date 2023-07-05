package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("验证邮箱请求")
@Data
public class VerifyEmailRequest implements Serializable {
    @ApiModelProperty("邮箱验证码,旧手机号码不必传")
    private String emailCode;
    @ApiModelProperty("邮箱")
    private String email;
}
