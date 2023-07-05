package io.filpool.scheduled.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CurrencyPriceVo {

    /**
     * status : true
     * code : 200
     * msg :
     * keyword :
     * coin_curpage : 1
     * exchange_curpage : 1
     * coin_totalsize : 88
     * exchange_totalsize : 2
     * coin_maxpage : 88
     * exchange_maxpage : 2
     * wallet_curpage : 1
     * wallet_totalsize : 1
     * wallet_maxpage : 1
     * coinlist : [{"coincode":"ethereum","coinname":"Ethereum","coinlogo":"https://s2.feixiaoquan.com/logo/1/ethereum.png?x-oss-process=style/coin_36","coinurl":"","market_value":45639703291,"price":406.63,"change_percent":-1.58,"status":0,"symbol":"ETH","volume":7194988331,"circulatingsupply":112238172,"logo_webp":"https://s2.feixiaoquan.com/logo/1/ethereum.png?x-oss-process=style/coin_36_webp","score":100,"rank_no":2,"native_name":"以太坊","circulationrate":100}]
     * exchangelist : [{"platform":"ethfinex","platform_name":"Ethfinex","platform_logo":"https://s2.feixiaoquan.com/logo/2/ethfinex.png?x-oss-process=style/coin_36","platform_url":"","volum_24h":241445914,"pairs":173,"area":"阿富汗","labels":"","labels_id":"spot","star":"2","status":0,"platform_name_zh":"Ethfinex","focus_num":71,"logo_webp":"/logo/2/ethfinex.png?x-oss-process=style/coin_36_webp","score":90,"rank_no":1000098,"exrank":0,"tradeurl":"","arealogo":"https://s2.feixiaoquan.com/images/nationalflag/ic_afghanistan@3x.png","hotindex":2,"assets":18007.9797,"risk_level":0}]
     * walletlist : [{"wallet_id":549,"wallet_name":"ClassicEtherWallet CX","wallet_url":"https://www.cryptocompare.com/wallets/classicetherwallet-cx/","wallet_logo":"https://s2.feixiaoquan.com/image/20181123/493b0c8d_2941_40ad_a87d_c17da4cfb125.png","security":2,"easy_using":4,"chains":"ETC","siteurl":"https://www.cryptocompare.com/wallets/classicetherwallet-cx/","native_name":"","types":"pc","is_decentration":0,"is_multichain":0,"star":1}]
     */

    private String status;
    private int code;
    private List<CoinlistBean> coinlist;

    @Data
    public static class CoinlistBean {
        /**
         * coincode : ethereum
         * coinname : Ethereum
         * coinlogo : https://s2.feixiaoquan.com/logo/1/ethereum.png?x-oss-process=style/coin_36
         * coinurl :
         * market_value : 45639703291
         * price : 406.63
         * change_percent : -1.58
         * status : 0
         * symbol : ETH
         * volume : 7194988331
         * circulatingsupply : 112238172
         * logo_webp : https://s2.feixiaoquan.com/logo/1/ethereum.png?x-oss-process=style/coin_36_webp
         * score : 100
         * rank_no : 2
         * native_name : 以太坊
         * circulationrate : 100
         */

        private String coincode;
        private String coinname;
        private String coinlogo;
        private String coinurl;
        private BigDecimal market_value;
        private BigDecimal price;
        private BigDecimal change_percent;
        private int status;
        private String symbol;
        private BigDecimal volume;
        private String native_name;
        private BigDecimal circulationrate;
        private int rank_no;
    }
}
