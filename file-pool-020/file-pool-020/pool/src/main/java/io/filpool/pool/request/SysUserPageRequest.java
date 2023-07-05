package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("获取系统用户分页请求")
public class SysUserPageRequest extends PageRequest {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("是否启用")
    private Boolean isEnable;
    @ApiModelProperty("开始时间")
    private Date startDate;
    @ApiModelProperty("结束时间")
    private Date endDate;
    @ApiModelProperty("用户账号")
    private String account;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty
    private String email;



}
