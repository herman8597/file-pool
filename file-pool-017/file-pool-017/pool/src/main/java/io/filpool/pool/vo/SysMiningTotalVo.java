package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("系统收益管理")
public class SysMiningTotalVo implements Serializable {
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("用户账号")
    private String account;
    @ApiModelProperty("累计挖矿收益")
    private BigDecimal totalMiningAmount;
    @ApiModelProperty("累计冻结收益")
    private BigDecimal totalFrozenAmount;
    @ApiModelProperty("累计释放收益")
    private BigDecimal totalReleaseAmount;
    @ApiModelProperty("挖矿币种id")
    private Long currencyId;
    @ApiModelProperty("挖矿币种单位")
    private String symbol;
}
