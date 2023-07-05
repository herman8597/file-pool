package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("平台信息请求")
@Data
public class AboutRequest {
    @ApiModelProperty("类型: 1关于我们  2用户协议")
    private Integer type;
}
