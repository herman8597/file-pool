package io.filpool.scheduled.model;

import lombok.Data;

@Data
public class XchDesc {
    //区块高度
    private String height;
    //全网有效算力
    private String netSpace;
    //XCH总量
    private String supply;
    //每日单T产量
    private String dayIncome;
    //费用/硬币（XCH）
    private String averageFees;
    //养殖XCH
    private String uniqueCoins;
}
