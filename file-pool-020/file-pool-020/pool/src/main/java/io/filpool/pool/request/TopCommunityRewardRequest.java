package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("aaa")
public class TopCommunityRewardRequest extends PageRequest {
    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("用戶id")
    private Long id;
}
