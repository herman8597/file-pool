package network.vena.cooperation.common.enums;

/**
 * 申请状态
 */
public enum ApplyStatusEnum {
    AGREE("1", "同意"),

    REFUSE("2", "拒绝"),

    APPLYING("3", "申请中");

    private String value;
    private String desc;

    ApplyStatusEnum(String value, String desc) {
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
