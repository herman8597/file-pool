package network.vena.cooperation.common.enums;


public enum ReadFlagEnum {
    READ("1", "已读"),

    UNREAD("0", "未读");

    private String value;
    private String desc;

    ReadFlagEnum(String value, String desc) {
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
