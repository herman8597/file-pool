package network.vena.cooperation.common.enums;

public enum  DealStatusEnum {

    AGREE("1", "同意"),

    REFUSE("2", "拒绝");


    private String value;
    private String desc;

    DealStatusEnum(String value, String desc) {
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
