package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("获取算力记录请求")
@Data
public class PowerRecordsRequest extends PageRequest{
    @ApiModelProperty("类型:1补单(云算力) 2补单(矿机) 3购买云算力 4平台充值 5平台扣除 6邀请奖励")
    private Integer type;
}
