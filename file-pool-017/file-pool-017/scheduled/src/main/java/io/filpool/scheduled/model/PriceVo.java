package io.filpool.scheduled.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceVo {

    /**
     * status : true
     * code : 200
     * msg :
     * keyword :
     * coin_curpage : 1
     * exchange_curpage : 1
     * coin_totalsize : 13
     * exchange_totalsize : 0
     * coin_maxpage : 13
     * exchange_maxpage : 1
     * wallet_curpage : 1
     * wallet_totalsize : 0
     * wallet_maxpage : 0
     * coinlist : [{"coincode":"filecoin","coinname":"Filecoin","coinlogo":"https://s2.bqiapp.com/logo/1/filecoin.png?x-oss-process=style/coin_36","coinurl":"","market_value":0,"price":18.3917,"change_percent":0.71,"status":0,"symbol":"FIL","volume":1.29024633E8,"circulatingsupply":0,"logo_webp":"https://s2.bqiapp.com/logo/1/filecoin.png?x-oss-process=style/coin_36_webp","score":100,"rank_no":1001847,"native_name":"","circulationrate":0}]
     * exchangelist : []
     * walletlist : []
     */

    private String status;
    private Integer code;
    private String msg;
    private String keyword;
    private Integer coin_curpage;
    private Integer exchange_curpage;
    private Integer coin_totalsize;
    private Integer exchange_totalsize;
    private Integer coin_maxpage;
    private Integer exchange_maxpage;
    private Integer wallet_curpage;
    private Integer wallet_totalsize;
    private Integer wallet_maxpage;
    private List<CoinlistBean> coinlist;
    private List<CoinlistBean> coinlistX;
    private List<?> exchangelist;
    private List<?> walletlist;



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CoinlistBean {
        /**
         * coincode : filecoinnew
         * coinname : Filecoin
         * coinlogo : https://s2.feixiaoquan.com/logo/1/filecoinnew.png?x-oss-process=style/coin_36&v=1602728806
         * coinurl :
         * market_value : 5.85667485E8
         * price : 33.6129
         * change_percent : 10.62
         * status : 0
         * symbol : FIL
         * volume : 3.21809008E8
         * circulatingsupply : 1.7423847E7
         * logo_webp : https://s2.feixiaoquan.com/logo/1/filecoinnew.png?x-oss-process=style/coin_36_webp&v=1602728806
         * score : 100
         * rank_no : 1000012
         * native_name :
         * circulationrate : 0.87
         */

        private String coincode;
        private String coinname;
        private String coinlogo;
        private String coinurl;
        private BigDecimal market_value;
        private BigDecimal market_valueX;
        private BigDecimal price;
        private BigDecimal priceX;
        private BigDecimal change_percent;
        private BigDecimal change_percentX;
        private Integer statusX;
        private String symbol;
        private BigDecimal volume;
        private BigDecimal volumeX;
        private BigDecimal circulatingsupply;
        private String logo_webp;
        private Integer score;
        private Integer rank_no;
        private String native_name;
        private BigDecimal circulationrate;

    }


}
