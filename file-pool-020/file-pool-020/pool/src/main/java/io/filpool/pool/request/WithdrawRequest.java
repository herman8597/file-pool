package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("外部提现请求")
@Data
public class WithdrawRequest {
    @ApiModelProperty("提现数量")
    private BigDecimal amount;
    @ApiModelProperty("提现币种id")
    private Long currencyId;
    @ApiModelProperty("提现地址")
    private String address;
    @ApiModelProperty("支付密码")
    private String payPwd;
    @ApiModelProperty("链名称：TRC20/ERC20")
    private String chain;
    @ApiModelProperty("谷歌验证码")
    private String googleCode;
}
