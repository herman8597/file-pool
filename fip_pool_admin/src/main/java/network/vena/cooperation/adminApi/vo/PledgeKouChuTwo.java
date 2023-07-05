package network.vena.cooperation.adminApi.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PledgeKouChuTwo {
    private String account;
    private String apiKey;
    private String powerAsset;
    private String sumTotal;
    private String daikouchu;
    private String zhiyazongshu;
    private String daikouchuzongshu;
    private String kouchuhoushengyu;
}
