package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("社区奖励查询请求")
public class SysCommunityRewardLogDescRequest extends PageRequest{
    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("查询条件的用户账号")
    private String searchAccount;

}
