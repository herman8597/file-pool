package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("修改手机号请求")
@Data
public class ModifyMobileRequest {
    @ApiModelProperty("旧手机验证token")
    private String oldMobileToken;
    @ApiModelProperty("新手机验证token")
    private String newMobileToken;
    @ApiModelProperty("新手机号码")
    private String newMobile;
    @ApiModelProperty("新手机区号")
    private String areaCode;
}
