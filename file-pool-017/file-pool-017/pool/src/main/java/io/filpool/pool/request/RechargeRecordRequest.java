package io.filpool.pool.request;

import io.filpool.framework.core.pagination.BasePageOrderParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("充值记录")
@Data
public class RechargeRecordRequest extends BasePageOrderParam {
    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("币种")
    private String symbol;

    @ApiModelProperty("发送地址")
    private BigDecimal fromAddress;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

}
