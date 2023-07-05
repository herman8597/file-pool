package io.filpool.pool.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("系统后台收益记录")
public class SysIncomeRecordVo {
    @ApiModelProperty("收益记录id")
    private Long id;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("当日矿池有效算力")
    private BigDecimal systemPower;
    @ApiModelProperty("当日用户有效算力")
    private BigDecimal userPower;
    @ApiModelProperty("当日平台挖矿收益")
    private BigDecimal totalMiningAmount;
    @ApiModelProperty("当日用户挖矿收益")
    private BigDecimal userMiningAmount;
    @ApiModelProperty("剩余冻结收益")
    private BigDecimal frozenAmount;
    @ApiModelProperty("已释放收益")
    private BigDecimal releaseAmount;
    @ApiModelProperty("首次释放收益")
    private BigDecimal firstAmount;
    @ApiModelProperty("服务费率")
    private BigDecimal serviceFee;
    @ApiModelProperty("挖矿币种单位")
    private String symbol;
    @ApiModelProperty("用户账号")
    private String account;
    @ApiModelProperty("发放日期")
    private Date createTime;

}
