package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("发送忘记密码短信")
public class ForgetSmsCodeRequest {
//    @ApiModelProperty("区号")
//    private String areaCode;
    @ApiModelProperty("手机号或邮箱")
    private String account;
}
