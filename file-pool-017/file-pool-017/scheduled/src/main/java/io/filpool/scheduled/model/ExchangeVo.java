package io.filpool.scheduled.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeVo {
    /**
     * data : {"codetotal":2549,"tokentotal":3773,"exchangetotal":584,"vol24h":6.92851488990407E10,"marketcapvol":3.81159282286E11,"risenum":1557,"fallnum":1385,"walletcount":118,"dappcount":863,"websitecount":187,"offmarketprice":0.99683544,"exchangerate":0.1438435,"usdt_price_cny":6.93,"xauprice":2008.22}
     * code : 200
     * msg : success
     */

    private DataBean data;
    private Integer code;
    private String msg;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataBean {
        /**
         * codetotal : 2549
         * tokentotal : 3773
         * exchangetotal : 584
         * vol24h : 6.92851488990407E10
         * marketcapvol : 3.81159282286E11
         * risenum : 1557
         * fallnum : 1385
         * walletcount : 118
         * dappcount : 863
         * websitecount : 187
         * offmarketprice : 0.99683544
         * exchangerate : 0.1438435
         * usdt_price_cny : 6.93
         * xauprice : 2008.22
         */

        private Integer codetotal;
        private Integer tokentotal;
        private Integer exchangetotal;
        private BigDecimal vol24h;
        private BigDecimal marketcapvol;
        private Integer risenum;
        private Integer fallnum;
        private Integer walletcount;
        private Integer dappcount;
        private Integer websitecount;
        private BigDecimal offmarketprice;
        private BigDecimal exchangerate;
        private BigDecimal usdt_price_cny;
        private BigDecimal xauprice;

    }
}
