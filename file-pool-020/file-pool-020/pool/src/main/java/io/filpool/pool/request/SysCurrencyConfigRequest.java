package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("获得币种配置请求")
public class SysCurrencyConfigRequest {
    @ApiModelProperty("配置类型:1经验值比例 2技术服务费")
    private Integer type;
}
