package network.vena.cooperation.common.enums;

/**
 * 申请状态
 */
public enum SettleStatusEnum {
    NOSETTLE("1", "未结算"),

    FORWITHDRAWAL("2", "待提现"),

    HAVEWITHDRAWAL("3", "已提现"),

    APPLYING("4","申请中");



    private String value;
    private String desc;

    SettleStatusEnum(String value, String desc) {
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
