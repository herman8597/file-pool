package io.filpool.scheduled.model;

import lombok.Data;

/**
 * 算力增速排名模型
 */
@Data
public class PowerGorwthVo {

    /**
     * address : f02769
     * rawBytePowerGrowth : 605693467951104
     * qualityAdjPowerGrowth : 605693467951104
     * rawBytePowerDelta : 528074818977792
     * qualityAdjPowerDelta : 528074818977792
     * equivalentMiners : 4039.75
     * rawBytePower : 1198948710612992
     * qualityAdjPower : 1198948710612992
     * blocksMined : 12
     * weightedBlocksMined : 12
     * totalRewards : 167405832887924112869
     */

    private String address;
    private String rawBytePowerGrowth;
    private String qualityAdjPowerGrowth;
    private String rawBytePowerDelta;
    private String qualityAdjPowerDelta;
    private String equivalentMiners;
    private String rawBytePower;
    private String qualityAdjPower;
    private long blocksMined;
    private long weightedBlocksMined;
    private String totalRewards;

}
