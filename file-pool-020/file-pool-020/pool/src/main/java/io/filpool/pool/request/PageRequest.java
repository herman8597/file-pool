package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("分页请求")
@Data
public class PageRequest implements Serializable {
    @ApiModelProperty("页数")
    private Long pageIndex;
    @ApiModelProperty("每页数量")
    private Long pageSize;

    public Long getPageIndex() {
        if (pageIndex == null && pageIndex < 1)
            return 1L;
        return pageIndex;
    }

    public Long getPageSize() {
        if (pageSize == null && pageSize < 1)
            return 10L;
        return pageSize;
    }
}
