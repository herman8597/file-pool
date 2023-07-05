package io.filpool.pool.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class InviteRecordRequest extends PageRequest {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("推荐关系")
    private Integer relation;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

}
