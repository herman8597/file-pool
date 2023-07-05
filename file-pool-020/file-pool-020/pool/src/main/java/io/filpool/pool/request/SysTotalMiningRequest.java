package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("收益管理请求")
@Data
public class SysTotalMiningRequest extends PageRequest{
    @ApiModelProperty("用户账号")
    private String account;
    @ApiModelProperty("是否代理商分币")
    private Boolean isAgent;
}
