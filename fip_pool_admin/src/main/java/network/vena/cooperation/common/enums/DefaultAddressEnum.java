package network.vena.cooperation.common.enums;

/**
 * 是否默认收货地址
 */
public enum DefaultAddressEnum {
    DEFAULT_TURE(1, "默认收货地址"),
    DEFAULT_FALSE(0, "非默认收货地址");

    private Integer value;
    private String desc;

    DefaultAddressEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
