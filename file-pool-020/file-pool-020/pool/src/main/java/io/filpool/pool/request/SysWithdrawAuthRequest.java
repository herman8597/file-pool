package io.filpool.pool.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("系统用户审核提币请求")
@Data
public class SysWithdrawAuthRequest implements Serializable {
    @ApiModelProperty("提币id")
    private Long withdrawId;
    @ApiModelProperty("是否通过")
    private Boolean isPass;

    @ApiModelProperty("提币数量")
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("备注")
    private String remark;
}
