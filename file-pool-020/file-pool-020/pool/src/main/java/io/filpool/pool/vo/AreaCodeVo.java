package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("国家地区返回结构提")
public class AreaCodeVo {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("区号")
    private String code;

    @ApiModelProperty("名称")
    private String name;
}
