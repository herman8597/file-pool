package network.vena.cooperation.adminApi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@ApiModel("算力质押设置")
@Data
@Accessors(chain = true)
public class WeightPledgeVo {
    @ApiModelProperty("质押币数量")
    private BigDecimal pledgeAmount;
    @ApiModelProperty("gas数量")
    private BigDecimal gas;
}
