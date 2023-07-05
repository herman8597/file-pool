package io.filpool.scheduled.model;

import lombok.Data;

@Data
public class OverviewVo {

    /**
     * height : 266498
     * timestamp : 1606301340
     * totalRawBytePower : 1231723262451384320
     * totalQualityAdjPower : 1231302527455068160
     * totalRawBytePowerDelta : 16177526896328704
     * totalQualityAdjPowerDelta : 16167528212463616
     * accounts : 85476
     * activeMiners : 741
     * totalMaxSupply : 2000000000000000000000000000
     * totalSupply : 625925384037048846932257233
     * circulatingSupply : 44212040713052723709387057
     * burntSupply : 4468873365182266389724247
     * totalPledgeCollateral : 18821252639642127689812800
     * totalMultisigLockedBalance : 540621382746832005700326909
     * totalMarketPledge : 6636575482286503250534
     * blockReward : 13965134750967079404
     * dailyMessages : 633943
     * dailyCoinsMined : 193001895958106916178729
     * averageTipsetlongerval : 30.16759776536313
     * averageTipsetBlocks : 4.657472067039106
     * averageTipsetWeightedBlocks : 4.837290502793296
     * baseFee : 100
     * averageRewardPerByte : 54.863376136086735
     * estimatedInitialPledgeCollateral : 6889175.050216505
     * price : 30.45
     * priceChangePercentage : 0.0332697
     */
    //节点高度
    private long height;
    //时间
    private long timestamp;
    //全网算力
    private String totalRawBytePower;
    //全网原值算力
    private String totalQualityAdjPower;
    private String totalRawBytePowerDelta;
    private String totalQualityAdjPowerDelta;
    //总账户数
    private long accounts;
    //活跃矿工数
    private long activeMiners;
    //FIL总供给量
    private String totalMaxSupply;
    private String totalSupply;
    //FIL流通量
    private String circulatingSupply;
    //FIL销毁量
    private String burntSupply;
    //FIL质押量
    private String totalPledgeCollateral;
    private String totalMultisigLockedBalance;
    private String totalMarketPledge;
    //每区块奖励
    private String blockReward;
    //24h消息数
    private long dailyMessages;
    //24H产出FIL
    private String dailyCoinsMined;
    private String averageTipsetlongerval;
    //平均每高度区块数量
    private String averageTipsetBlocks;
    private String averageTipsetWeightedBlocks;
    //当前基础费率
    private String baseFee;
    private String averageRewardPerByte;
    private String estimatedInitialPledgeCollateral;
    //价格
    private String price;
    //24小时涨跌幅
    private String priceChangePercentage;
}
