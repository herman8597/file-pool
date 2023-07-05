package network.vena.cooperation.adminApi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WeightDTO {

    @ApiModelProperty
    private String apiKey;

    @ApiModelProperty
    private Integer type;

    @ApiModelProperty
    private BigDecimal quantity;
    @ApiModelProperty("1充值,2扣除")
    private Integer operation;


}
