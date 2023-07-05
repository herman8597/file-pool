package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("修改密码请求")
public class UpdatePwdRequest {
    @ApiModelProperty("旧密码")
    private String oldPwd;
    @ApiModelProperty("新密码")
    private String newPwd;
}
