package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("算力单位请求")
@Data
public class SymbolPageRequest extends PageRequest{
    @ApiModelProperty("算力单位")
    private String symbol;
}
