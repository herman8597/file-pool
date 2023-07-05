package network.vena.cooperation.base.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalance {

    private String apiKey;

    private String asset="XCH";

    private BigDecimal available;

    private Integer type=13;

}
