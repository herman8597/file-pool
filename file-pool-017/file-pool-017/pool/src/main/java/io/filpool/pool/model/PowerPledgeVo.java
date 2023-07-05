package io.filpool.pool.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 云算力质押要求详情
 * */
@Data
public class PowerPledgeVo {
    private BigDecimal tbNeedAmount;
    private BigDecimal tbGasFee;
    private String symbol;
    private Long currencyId;
}
