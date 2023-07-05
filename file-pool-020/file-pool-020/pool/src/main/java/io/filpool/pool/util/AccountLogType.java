package io.filpool.pool.util;

public class AccountLogType {
    //充值记录
    public static final Integer TYPE_RECHARGE = 1;
    //提币记录
    public static final Integer TYPE_WITHDRAW = 2;
    //系统充值
    public static  final Integer TYPE_SYSTEM_RECHARGE = 3;
    //系统扣除
    public static final Integer TYPE_SYSTEM_DELETE = 4;
    //补单GAS费
    public static final Integer TYPE_GAS = 5;
    //划转记录
    public static final Integer TYPE_TRANSFER = 6;
    //系统划转
    public static final Integer TYPE_SYSTEM_TRANSFER = 7;
    //购买矿机
    public static final Integer TYPE_BUY_MACHINE = 8;
    //购买云算力
    public static final Integer TYPE_BUY_POWER = 9;
    //购买BZZ
    public static final Integer TYPE_BUY_POWER_BZZ = 14;
    //购买矿机集群
    public static final Integer TYPE_BUY_MACHINE_GROUP = 10;
    //返佣奖励
    public static final Integer TYPE_INVITE_REWARD = 11;
    //当日挖矿首次释放收益
    public static final Integer TYPE_MINING_RELEASE_FIRST = 12;
    //当日线性释放收益
    public static final Integer TYPE_MINING_RELEASE_LINE= 13;
    //极差奖励
    public static final Integer TYPE_REWARD_LEVEL_DIFF = 20;
    //平级奖励
    public static final Integer TYPE_REWARD_LEVEL_SAME = 21;
}
