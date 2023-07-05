package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("划转分页请求")
@Data
public class TransferPageRequest extends PageRequest{
    @ApiModelProperty("类型:0全部 1可用转质押 2质押转可用 3云算力增加质押")
    private Integer type;
}
