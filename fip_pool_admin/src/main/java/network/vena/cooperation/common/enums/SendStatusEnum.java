package network.vena.cooperation.common.enums;

public enum SendStatusEnum {
    UNPUBLISHED("0", "未发布"),
    PUBLISHED("1", "已发布"),
    HADWITHDRAWN("2", "已撤销");

    private String value;
    private String desc;

    SendStatusEnum(String value, String desc) {
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
