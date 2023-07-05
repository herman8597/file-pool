package io.filpool.pool.model;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 币种资产总和
 */
@Data
public class AssetsSumVo {
    //币种名称
    private String currencyName;
    //可用总和
    private BigDecimal available;
    //冻结总和
    private BigDecimal frozen;
}
