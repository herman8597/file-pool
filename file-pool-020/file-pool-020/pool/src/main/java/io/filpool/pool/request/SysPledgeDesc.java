package io.filpool.pool.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("质押详情实体类")
public class SysPledgeDesc extends PageRequest {
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("开始时间")
    private Date startDate;
    @ApiModelProperty("结束时间")
    private Date endDate;
    @ApiModelProperty("用户账号")
    private String account;

}
