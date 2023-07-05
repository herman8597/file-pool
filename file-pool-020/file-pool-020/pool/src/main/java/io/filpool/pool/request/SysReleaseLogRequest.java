package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("系统查询释放请求")
public class SysReleaseLogRequest extends PageRequest{
    @ApiModelProperty("用户待释放记录id")
    private Long releaseId;
}
