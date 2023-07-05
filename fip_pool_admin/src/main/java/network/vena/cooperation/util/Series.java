package network.vena.cooperation.util;

/**
 * 币种系列枚举
 */
public enum Series {
    BTC("BTC"),
    ETH("ETH"),
    ;
    private String name;

    Series(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
