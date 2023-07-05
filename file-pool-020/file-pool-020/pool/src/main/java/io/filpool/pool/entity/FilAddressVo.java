package io.filpool.pool.entity;

import lombok.Data;

@Data
public class FilAddressVo {

    /**
     * id : f024563
     * robust : f2qzunot5o7fzncfiitxvkp4ri75fmifousghxx2a
     * tag : {"name":"Butterflychain","signed":true}
     * actor : storageminer
     * createHeight : 121948
     * createTimestamp : 1601964840
     * lastSeenHeight : 266554
     * lastSeenTimestamp : 1606303020
     * balance : 86617519700367539469416
     * messageCount : 603758
     * timestamp : 1606303020
     * miner : {"owner":{"address":"f3vgrp6y5iqp7df6jc7ibzzythlkluei5x3vivwajsko3k6za3utrr5ajly4mhgiv3xggox62nmbb4odfqih3q","balance":"1505065761115117061741"},"worker":{"address":"f3vgrp6y5iqp7df6jc7ibzzythlkluei5x3vivwajsko3k6za3utrr5ajly4mhgiv3xggox62nmbb4odfqih3q","balance":"1505065761115117061741"},"controlAddresses":[{"address":"f3u5ew6btmw2y76cvob6nmryrdj2h3dqkifqqgjyi5tjwvck24yztfu46gv64vfhpzdih7qm4ccsbcwsjnewrq","balance":"100950709785907362575"}],"peerId":"12D3KooWMkHSBqjAHDvbqL1rtUp39ewpCssdNQhBuQ5GxC5uDx2U","multiAddresses":[],"sectorSize":34359738368,"rawBytePower":"10158353569284096","qualityAdjPower":"10158353569284096","networkRawBytePower":"1232100979055263744","networkQualityAdjPower":"1231681206131621888","blocksMined":2321,"weightedBlocksMined":2355,"totalRewards":"30189537948817675007439","sectors":{"live":296745,"active":295647,"faulty":106,"recovering":17},"preCommitDeposits":"107384380680419828226","vestingFunds":"20895364653075906059259","initialPledgeRequirement":"60861612998302410332096","availableBalance":"4753157668308803249835","pledgeBalance":"81864362032058736219581","rawBytePowerRank":29,"qualityAdjPowerRank":29,"miningStats":{"rawBytePowerGrowth":"45011257262080","qualityAdjPowerGrowth":"45011257262080","rawBytePowerDelta":"60232621359104","qualityAdjPowerDelta":"60232621359104","blocksMined":104,"weightedBlocksMined":107,"totalRewards":"1490817471008304507866","networkTotalRewards":"193050727304273076401586","equivalentMiners":300.20833333333337,"rewardPerByte":51.131869277833836,"luckyValue":0.9036172335415728,"durationPercentage":1},"location":null}
     * ownedMiners : []
     * workerMiners : []
     * address : f024563
     */

    private String id;
    private String robust;
    private TagBean tag;
    private String actor;
    private long createHeight;
    private long createTimestamp;
    private long lastSeenHeight;
    private long lastSeenTimestamp;
    //余额
    private String balance;
    private long messageCount;
    private long timestamp;
    private MinerBean miner;
    private String address;


    @Data
    public class TagBean {
        /**
         * name : Butterflychain
         * signed : true
         */

        private String name;
        private boolean signed;
    }

    @Data
    public class MinerBean {
        /**
         * owner : {"address":"f3vgrp6y5iqp7df6jc7ibzzythlkluei5x3vivwajsko3k6za3utrr5ajly4mhgiv3xggox62nmbb4odfqih3q","balance":"1505065761115117061741"}
         * worker : {"address":"f3vgrp6y5iqp7df6jc7ibzzythlkluei5x3vivwajsko3k6za3utrr5ajly4mhgiv3xggox62nmbb4odfqih3q","balance":"1505065761115117061741"}
         * controlAddresses : [{"address":"f3u5ew6btmw2y76cvob6nmryrdj2h3dqkifqqgjyi5tjwvck24yztfu46gv64vfhpzdih7qm4ccsbcwsjnewrq","balance":"100950709785907362575"}]
         * peerId : 12D3KooWMkHSBqjAHDvbqL1rtUp39ewpCssdNQhBuQ5GxC5uDx2U
         * multiAddresses : []
         * sectorSize : 34359738368
         * rawBytePower : 10158353569284096
         * qualityAdjPower : 10158353569284096
         * networkRawBytePower : 1232100979055263744
         * networkQualityAdjPower : 1231681206131621888
         * blocksMined : 2321
         * weightedBlocksMined : 2355
         * totalRewards : 30189537948817675007439
         * sectors : {"live":296745,"active":295647,"faulty":106,"recovering":17}
         * preCommitDeposits : 107384380680419828226
         * vestingFunds : 20895364653075906059259
         * initialPledgeRequirement : 60861612998302410332096
         * availableBalance : 4753157668308803249835
         * pledgeBalance : 81864362032058736219581
         * rawBytePowerRank : 29
         * qualityAdjPowerRank : 29
         * miningStats : {"rawBytePowerGrowth":"45011257262080","qualityAdjPowerGrowth":"45011257262080","rawBytePowerDelta":"60232621359104","qualityAdjPowerDelta":"60232621359104","blocksMined":104,"weightedBlocksMined":107,"totalRewards":"1490817471008304507866","networkTotalRewards":"193050727304273076401586","equivalentMiners":300.20833333333337,"rewardPerByte":51.131869277833836,"luckyValue":0.9036172335415728,"durationPercentage":1}
         * location : null
         */

        private String peerId;
        private long sectorSize;
        //原值算力
        private String rawBytePower;
        //有效算力
        private String qualityAdjPower;
        private String networkRawBytePower;
        private String networkQualityAdjPower;
        private long blocksMined;
        //累计出块数
        private long weightedBlocksMined;
        //累计出块奖励
        private String totalRewards;
        //扇区状态
        private SectorsBean sectors;
        private String preCommitDeposits;
        private String vestingFunds;
        private String initialPledgeRequirement;
        //可用余额
        private String availableBalance;
        //质押余额
        private String pledgeBalance;
        //官方排名
        private long rawBytePowerRank;
        //官方排名
        private long qualityAdjPowerRank;
        //出块排名
        private long blockRank;
        //算力增速排名
        private long powerGorwthRank;
        //挖矿统统记
        private MiningStatsBean miningStats;

        @Data
        public class SectorsBean {
            /**
             * live : 296745
             * active : 295647
             * faulty : 106
             * recovering : 17
             */

            private long live;
            private long active;
            private long faulty;
            private long recovering;
        }

        @Data
        public class MiningStatsBean {
            /**
             * rawBytePowerGrowth : 45011257262080
             * qualityAdjPowerGrowth : 45011257262080
             * rawBytePowerDelta : 60232621359104
             * qualityAdjPowerDelta : 60232621359104
             * blocksMined : 104
             * weightedBlocksMined : 107
             * totalRewards : 1490817471008304507866
             * networkTotalRewards : 193050727304273076401586
             * equivalentMiners : 300.20833333333337
             * rewardPerByte : 51.131869277833836
             * luckyValue : 0.9036172335415728
             * durationPercentage : 1
             */

            private String rawBytePowerGrowth;

            private String qualityAdjPowerGrowth;
            //算力增量
            private String rawBytePowerDelta;
            //算力增速
            private String qualityAdjPowerDelta;
            //出块数量
            private long blocksMined;
            //出块份数
            private long weightedBlocksMined;
            //出块奖励
            private String totalRewards;
            private String networkTotalRewards;
            //矿机当量
            private double equivalentMiners;
            private double rewardPerByte;
            //幸运值
            private double luckyValue;
            private long durationPercentage;


        }
    }
}
