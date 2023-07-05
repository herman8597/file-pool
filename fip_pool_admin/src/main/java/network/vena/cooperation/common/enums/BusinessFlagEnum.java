package network.vena.cooperation.common.enums;


public enum BusinessFlagEnum {
    MERCHANT("1", "商家"),

    UNMERCHANTS("2", "非商家"),

    APPLYING("3", "申请中");

    private String value;
    private String desc;

    BusinessFlagEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
