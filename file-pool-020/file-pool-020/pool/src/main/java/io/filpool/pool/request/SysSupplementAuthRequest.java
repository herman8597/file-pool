package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("系统用户补单审核请求")
@Data
public class SysSupplementAuthRequest implements Serializable {
    @ApiModelProperty("补单id")
    private Long supplementId;
    @ApiModelProperty("是否通过")
    private Boolean isPass;
    @ApiModelProperty("审核备注")
    private String remark;
}
