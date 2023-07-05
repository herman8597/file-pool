package network.vena.cooperation.common.enums;

/**
 * 性别
 */
public enum SexEnum {

    MAN("1", "男"),
    WOMAN("2", "女"),
    SECRECY("3", "保密");

    private String value;
    private String desc;

    SexEnum(String value, String desc) {
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
