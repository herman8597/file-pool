package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("收益管理请求")
@Data
public class SysIncomeRecordRequest extends PageRequest{
    @ApiModelProperty("是否代理商分币")
    private Boolean isAgent;
    @ApiModelProperty("用户账号")
    private String account;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
}
