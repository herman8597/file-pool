package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("手机发送短信请求")
@Data
public class SmsMobileCodeRequest {
    @ApiModelProperty("手机号码,旧手机号码不必传")
    private String mobile;
    @ApiModelProperty("区号,旧手机号码不必传")
    private String areaCode;
}
