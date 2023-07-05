package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("充提币记录分页请求")
public class RechargeListRequest extends PageRequest{
    @ApiModelProperty("币种ID,为空时获取全部币种的记录")
    private Long currencyId;
}
