package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("获取资产记录请求")
@Data
public class AssetRecordsRequest extends PageRequest{
    @ApiModelProperty("记录的类型：1充值 2提现 3平台充值 4平台扣除 5补单gas费 6购买矿机 7购买云算力 8购买矿机集群 9奖励返佣 10当日挖矿首次释放 11当日线性释放")
    private Integer type;

    @ApiModelProperty("币种单位")
    private Long currencyId;
}
