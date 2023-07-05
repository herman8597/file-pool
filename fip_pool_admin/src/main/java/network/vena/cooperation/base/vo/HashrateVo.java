package network.vena.cooperation.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class HashrateVo {
    private BigDecimal hashrate =BigDecimal.ZERO;
    private Integer type;
}
