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
public class AwardVo {
    private Integer totalCount=0;
    private BigDecimal award=BigDecimal.ZERO;
    private BigDecimal distribution=BigDecimal.ZERO;
    private BigDecimal hashrateTotal=BigDecimal.ZERO;
    private BigDecimal childrenPurchase=BigDecimal.ZERO;
    private BigDecimal grandchildrenPurchase=BigDecimal.ZERO;

}
