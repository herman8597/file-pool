package network.vena.cooperation.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import network.vena.cooperation.util.ObjectUtils;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class SalesVo {
    private String asset;
    private BigDecimal amount;

    public String getAmount() {
        if (ObjectUtils.isEmpty(amount)) {
            return "0";
        }
        return amount.toPlainString();
    }
}
