package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("邀请好友详情")
public class InviteInfoVo {
    @ApiModelProperty("累计邀请人数")
    private Integer totalInviteCount;
    @ApiModelProperty("累计购买人数")
    private Integer totalBuyCount;
    @ApiModelProperty("累计推广数量")
    private BigDecimal totalPromotionAmount;
    @ApiModelProperty("累计获取金额")
    private BigDecimal totalRewardAmount;
    @ApiModelProperty("累计获取算力")
    private BigDecimal totalRewardPower;
    @ApiModelProperty("邀请链接")
    private String inviteUrl;
    @ApiModelProperty("邀请码")
    private String inviteCode;
    @ApiModelProperty("邀请海报")
    private String invitePoster;
}
