package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("验证码返回")
public class ImageVo {
    @ApiModelProperty("验证码token")
    private String verifyToken;
    @ApiModelProperty("验证码图片")
    private String image;
}
