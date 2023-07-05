package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("算力记录返回")
public class UserPowerVo {
    @ApiModelProperty("类型:2:fil算力，1:企亚算力")
    private Integer powerType;

    @ApiModelProperty("算力数量")
    private BigDecimal amount;

}
