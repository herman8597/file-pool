package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("添加技术服务费请求")
public class SysServerFeeRequest {
    @ApiModelProperty("技术服务费率")
    private BigDecimal fee;
}
