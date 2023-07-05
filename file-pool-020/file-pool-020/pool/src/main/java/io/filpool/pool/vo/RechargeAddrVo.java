package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("充值地址")
@Data
public class RechargeAddrVo implements Serializable {
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("币种")
    private String symbol;
    @ApiModelProperty("最小充值数")
    private BigDecimal minRechargeAmount;
    @ApiModelProperty("确认数")
    private Integer confirmNumber;
}
