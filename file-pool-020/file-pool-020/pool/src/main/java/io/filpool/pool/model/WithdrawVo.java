package io.filpool.pool.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawVo {
    //交易哈希
    private String transHash;
    private BigDecimal fee;
}
