package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("邮箱发送短信请求")
@Data
public class SmsEmailCodeRequest {
    @ApiModelProperty("邮箱,旧邮箱不必传")
    private String email;
}
