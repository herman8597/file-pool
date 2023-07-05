package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("谷歌密钥二维码返回")
public class GoogleSecretVo {
    @ApiModelProperty("二维码")
    private String qrCode;
    @ApiModelProperty("密钥")
    private String secret;
}
