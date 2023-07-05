package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("后台分页请求")
@Data
public class SysPageRequest extends PageRequest {
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("语言类型： zh-CN 中文，zh-TW 繁体，en-US 英文")
    private String language;
    @ApiModelProperty("是否启用")
    private Boolean isEnable;
}
