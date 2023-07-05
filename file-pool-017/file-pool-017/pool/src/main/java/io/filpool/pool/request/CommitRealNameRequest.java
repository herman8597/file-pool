package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("提交实名认证请求")
public class CommitRealNameRequest {
    @ApiModelProperty("姓名")
    private String realName;
    @ApiModelProperty("身份证号")
    private String cardNumber;
    @ApiModelProperty("区号")
    private String areaCode;
    @ApiModelProperty("国家地区名称")
    private String areaName;
    @ApiModelProperty("正面照片")
    private String cardFront;
    @ApiModelProperty("反面照片")
    private String cardSide;
//    @ApiModelProperty("手持签名照片")
//    private String cardHolding;
}
