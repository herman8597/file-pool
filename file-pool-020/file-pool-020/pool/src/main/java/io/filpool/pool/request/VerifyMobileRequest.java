package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("验证手机请求")
@Data
public class VerifyMobileRequest implements Serializable {
    @ApiModelProperty("手机验证码")
    private String mobileCode;
    @ApiModelProperty("手机号码,旧手机号码不必传")
    private String mobile;
    @ApiModelProperty("区号")
    private String areaCode;
}
