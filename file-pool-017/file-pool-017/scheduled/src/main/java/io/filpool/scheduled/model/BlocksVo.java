package io.filpool.scheduled.model;

import lombok.Data;

/**
 * 出块排名
 */
@Data
public class BlocksVo {

    /**
     * address : f024563
     * tag : {"name":"Butterflychain","signed":true}
     * blocksMined : 103
     * weightedBlocksMined : 106
     * totalRewards : 1476931195109675969136
     * rawBytePower : 10158353569284096
     * qualityAdjPower : 10158353569284096
     * luckyValue : 0.8952203579706381
     */

    private String address;
    private long blocksMined;
    private long weightedBlocksMined;
    private String totalRewards;
    private String rawBytePower;
    private String qualityAdjPower;
    private String luckyValue;
}
