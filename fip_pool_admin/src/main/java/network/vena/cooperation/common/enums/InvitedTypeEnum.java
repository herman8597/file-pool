package network.vena.cooperation.common.enums;

public enum InvitedTypeEnum {

    FRIEND("1", "好友申请"),
    TEAM("2", "团队邀请");

    private String value;
    private String desc;

    InvitedTypeEnum(String value, String desc) {
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
