package network.vena.cooperation.adminApi.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceKouChu {
    private String apiKey;

    private BigDecimal subTotal;

}
