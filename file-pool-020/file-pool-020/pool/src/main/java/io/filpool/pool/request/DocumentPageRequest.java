package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文档中心分页请求")
public class DocumentPageRequest extends PageRequest{
    @ApiModelProperty("类型:1进阶小课堂 2帮助中心 3项目动态 4公告")
    private Integer type;
}
