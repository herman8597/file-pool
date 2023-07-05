package network.vena.cooperation.common.enums;

public enum UserStatusEnum {

    NORMAL("1", "正常"),
    FREEZE("2", "冻结");

    private String value;
    private String desc;

    UserStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue(){
        return this.value;
    }
    public String getDesc(){
        return this.desc;
    }
}
