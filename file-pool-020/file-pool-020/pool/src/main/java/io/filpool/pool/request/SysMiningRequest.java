package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("新增平台挖矿记录请求")
@Data
public class SysMiningRequest implements Serializable {
    @ApiModelProperty("矿池总收益")
    private BigDecimal amount;
    @ApiModelProperty("发放时间")
    private Date createTime;
    //    @ApiModelProperty("订单挖矿开始时间")
//    private Date startTime;
//    @ApiModelProperty("订单挖矿结束时间")
//    private Date endTime;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否代理商分币")
    private Boolean isAgent;
    @ApiModelProperty("代理商账号,非必填")
    private String agentAccount;
    @ApiModelProperty("挖矿币种单位")
    private String symbol;
}
