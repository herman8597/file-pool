package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("根据算力单位分页请求")
@Data
public class SymbolRequest{
    @ApiModelProperty("算力单位")
    private String symbol;
}
