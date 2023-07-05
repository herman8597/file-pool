package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("修改邮箱号请求")
@Data
public class ModifyEmailRequest {
    @ApiModelProperty("旧邮箱验证token")
    private String oldEmailToken;
    @ApiModelProperty("新邮箱验证token")
    private String newEmailToken;
    @ApiModelProperty("新邮箱")
    private String newEmail;
}
