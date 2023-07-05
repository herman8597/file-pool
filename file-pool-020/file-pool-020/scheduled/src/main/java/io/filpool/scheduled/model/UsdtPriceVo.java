package io.filpool.scheduled.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UsdtPriceVo {


    /**
     * data : [{"current_price":80859.04,"current_price_usd":11680.95,"code":"bitcoin","name":"BTC","fullname":"比特币","logo":"https://s2.feixiaoquan.com/logo/1/bitcoin.png?x-oss-process=style/coin_36_webp","change_percent":-1.3,"market_value":1493038155347,"vol":83137505801,"supply":18464693,"rank":1,"star_level":1,"kline_data":"11542.50,11489.33,11511.79,11753.66,11703.73,11708.43,11775.90,11750.62,11854.65,11874.90,11861.61,11838.98,11875.75,11768.50,11837.18,11897.69,11809.77,11875.49,12350.33,12367.01,12242.25,12236.02,11985.38,12028.80,11784.93,11802.34,11753.08,11724.10","market_value_usd":215685271564,"vol_usd":12010098638,"marketcap":215685271564,"high_price":20089,"drop_ath":-41.85,"low_price":65.526,"high_time":"2017-12-17","low_time":"2013-07-05","isifo":0,"ismineable":1,"ads":["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":["","",""],"turnoverrate":5.57,"changerate_utc":-0.35,"changerate_utc8":-0.39},{"current_price":2806.23,"current_price_usd":405.39,"code":"ethereum","name":"ETH","fullname":"以太坊","logo":"https://s2.feixiaoquan.com/logo/1/ethereum.png?x-oss-process=style/coin_36_webp","change_percent":-2.37,"market_value":314974026116,"vol":50468214992,"supply":112238172,"rank":2,"star_level":1,"kline_data":"391.54,392.36,392.13,422.36,423.86,423.25,438.30,438.37,437.72,435.65,435.92,430.75,427.22,421.93,433.16,432.36,423.09,425.70,438.95,437.70,427.75,429.92,422.32,423.97,412.80,411.09,407.47,406.22","market_value_usd":45501354480,"vol_usd":7290671452,"marketcap":45501354480,"high_price":1432.88,"drop_ath":-71.71,"low_price":0.4208,"high_time":"2018-01-13","low_time":"2015-10-21","isifo":0,"ismineable":1,"ads":["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":16.02,"changerate_utc":-0.12,"changerate_utc8":-0.98},{"current_price":1.9811,"current_price_usd":0.2862,"code":"ripple","name":"XRP","fullname":"瑞波币","logo":"https://s2.feixiaoquan.com/logo/1/ripple.png?x-oss-process=style/coin_36_webp","change_percent":-2.56,"market_value":86548448318,"vol":17901044561,"supply":43685558183,"rank":3,"star_level":1,"kline_data":"0.281427,0.279399,0.280787,0.290794,0.293149,0.293475,0.298811,0.300770,0.297696,0.297209,0.301606,0.298720,0.300875,0.296844,0.299490,0.300094,0.300201,0.302853,0.320742,0.320962,0.315648,0.312263,0.303679,0.303062,0.292487,0.291825,0.290471,0.288610","market_value_usd":12502845632,"vol_usd":2585996643,"marketcap":28617541350,"high_price":3.8419,"drop_ath":-92.55,"low_price":0.002802,"high_time":"2018-01-04","low_time":"2014-07-07","isifo":0,"ismineable":0,"ads":["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":20.68,"changerate_utc":-0.87,"changerate_utc8":-1.21},{"current_price":6.9278,"current_price_usd":1.0008,"code":"tether","name":"USDT","fullname":"泰达币","logo":"https://s2.feixiaoquan.com/logo/1/tether.png?x-oss-process=style/coin_36_webp","change_percent":0.01,"market_value":69272835894,"vol":210325017889,"supply":9998221723,"rank":4,"star_level":1,"kline_data":"1.0005,1.0002,0.999460,0.999313,1.0005,1.0011,1.0003,1.0005,0.999936,0.999900,0.998935,0.998758,0.998866,0.999312,0.999406,0.999216,0.999908,1.0001,1.0011,1.0004,0.999663,1.0008,1.0008,1.0011,1.0011,1.0017,1.0004,1.0009","market_value_usd":10007199326,"vol_usd":30383690087,"marketcap":10290604354,"high_price":1.1059,"drop_ath":-9.5,"low_price":0.9251,"high_time":"2017-11-12","low_time":"2018-02-02","isifo":0,"ismineable":1,"ads":["zbg;https://www.zbg.com/;","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":303.69,"changerate_utc":0,"changerate_utc8":0},{"current_price":111.92,"current_price_usd":16.1693,"code":"chainlink","name":"LINK","fullname":"ChainLink","logo":"https://s2.feixiaoquan.com/logo/1/chainlink.png?x-oss-process=style/coin_36_webp","change_percent":3.67,"market_value":39175150412,"vol":15898744075,"supply":350000000,"rank":5,"star_level":1,"kline_data":"16.3028,16.9199,17.8960,17.1144,17.2891,16.9223,16.7774,16.8640,16.7486,17.0807,18.9991,18.7839,18.9946,19.2363,19.0395,18.9175,19.1410,19.0858,18.7401,18.4846,16.6865,16.7536,16.1785,16.2514,15.6167,16.3337,16.9136,16.4422","market_value_usd":5659267933,"vol_usd":2296743001,"marketcap":16169336950,"high_price":19.9121,"drop_ath":-18.8,"low_price":0.1262,"high_time":"2020-08-16","low_time":"2017-09-23","isifo":0,"ismineable":1,"ads":["MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":40.54,"changerate_utc":-0.17,"changerate_utc8":-2.79},{"current_price":2001.23,"current_price_usd":289.1,"code":"bitcoin-cash","name":"BCH","fullname":"比特现金","logo":"https://s2.feixiaoquan.com/logo/1/bitcoin-cash.png?x-oss-process=style/coin_36_webp","change_percent":-2.8,"market_value":36571076957,"vol":18970535599,"supply":18274075,"rank":6,"star_level":1,"kline_data":"283.23,283.22,282.78,292.86,291.87,291.56,294.88,294.29,296.23,295.12,305.41,301.60,308.56,305.02,302.17,307.19,304.30,315.32,319.72,323.92,317.83,317.21,306.34,306.15,296.65,295.31,293.37,290.92","market_value_usd":5283081773,"vol_usd":2740496020,"marketcap":5283081773,"high_price":4355.62,"drop_ath":-93.36,"low_price":75.0753,"high_time":"2017-12-20","low_time":"2018-12-15","isifo":1,"ismineable":1,"ads":["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":51.84,"changerate_utc":-0.99,"changerate_utc8":-1.05},{"current_price":427.68,"current_price_usd":61.7836,"code":"litecoin","name":"LTC","fullname":"莱特币","logo":"https://s2.feixiaoquan.com/logo/1/litecoin.png?x-oss-process=style/coin_36_webp","change_percent":-1.67,"market_value":27370569424,"vol":15285365441,"supply":63997010,"rank":7,"star_level":1,"kline_data":"54.3252,53.7031,54.2973,56.7890,56.3127,56.1318,57.2402,56.7601,57.2672,57.2484,60.2112,59.6328,61.3443,61.3390,62.1532,63.8315,62.7551,64.3232,66.0333,67.7823,66.4188,66.8430,66.9105,66.1634,63.1631,62.6346,61.2422,61.3805","market_value_usd":3953970418,"vol_usd":2208133921,"marketcap":3953970418,"high_price":375.28,"drop_ath":-83.54,"low_price":1.1137,"high_time":"2017-12-19","low_time":"2015-01-14","isifo":0,"ismineable":1,"ads":["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":55.73,"changerate_utc":-0.01,"changerate_utc8":0.35},{"current_price":1418.72,"current_price_usd":204.95,"code":"bitcoin-cash-sv","name":"BSV","fullname":"比特币SV","logo":"https://s2.feixiaoquan.com/logo/1/bitcoin-cash-sv.png?x-oss-process=style/coin_36_webp","change_percent":-3.51,"market_value":25923535985,"vol":9638189803,"supply":18271627,"rank":8,"star_level":1,"kline_data":"205.36,204.83,204.89,212.35,211.91,210.86,212.30,212.49,215.12,214.63,226.31,224.53,223.94,221.54,218.27,222.59,218.31,224.75,227.67,229.38,229.52,227.74,219.99,219.55,210.10,211.66,208.15,206.22","market_value_usd":3744931018,"vol_usd":1392339223,"marketcap":4304135115,"high_price":445.82,"drop_ath":-54.03,"low_price":36.8696,"high_time":"2020-01-15","low_time":"2018-11-23","isifo":1,"ismineable":1,"ads":["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":37.13,"changerate_utc":-0.66,"changerate_utc8":-1.55},{"current_price":0.8881,"current_price_usd":0.1283,"code":"cardano","name":"ADA","fullname":"艾达币","logo":"https://s2.feixiaoquan.com/logo/1/cardano.png?x-oss-process=style/coin_36_webp","change_percent":-3.33,"market_value":23033613607,"vol":1860849730,"supply":25927070538,"rank":9,"star_level":1,"kline_data":"0.138141,0.136292,0.136833,0.139406,0.138729,0.138438,0.139016,0.138725,0.136618,0.137288,0.139733,0.137759,0.138915,0.136845,0.138042,0.137833,0.137978,0.142698,0.141968,0.141648,0.141111,0.139954,0.137094,0.137410,0.132401,0.132734,0.131162,0.130068","market_value_usd":3327450935,"vol_usd":268819573,"marketcap":3992941007,"high_price":1.3272,"drop_ath":-90.33,"low_price":0.017354,"high_time":"2018-01-04","low_time":"2017-10-01","isifo":0,"ismineable":0,"ads":["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"],"adpairs":[],"turnoverrate":8.08,"changerate_utc":-0.83,"changerate_utc8":-2.66},{"current_price":153.42,"current_price_usd":22.1636,"code":"binance-coin","name":"BNB","fullname":"币安币","logo":"https://s2.feixiaoquan.com/logo/1/binance-coin.png?x-oss-process=style/coin_36_webp","change_percent":-1.85,"market_value":22889060055,"vol":2206689748,"supply":149188549,"rank":10,"star_level":1,"kline_data":"21.3516,21.2397,21.2087,21.7715,21.4146,22.5562,22.9214,22.9355,23.4163,23.2938,23.3036,22.8903,22.9781,23.0559,23.2607,23.3863,23.0576,23.1576,23.6474,23.7508,23.3638,23.4853,23.0379,23.0950,22.6135,22.6285,22.4589,22.3340","market_value_usd":3306568634,"vol_usd":318779849,"marketcap":3909821079,"high_price":39.5665,"drop_ath":-43.98,"low_price":0.096109,"high_time":"2019-06-22","low_time":"2017-08-01","isifo":0,"ismineable":1,"ads":["MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png"],"adpairs":[],"turnoverrate":9.64,"changerate_utc":-0.65,"changerate_utc8":-1.26}]
     * maxpage : 644
     * currpage : 1
     * code : 200
     * msg : success
     */

    private int maxpage;
    private int currpage;
    private int code;
    private String msg;
    private List<DataBean> data;

    @Data
    public static class DataBean {
        /**
         * current_price : 80859.04
         * current_price_usd : 11680.95
         * code : bitcoin
         * name : BTC
         * fullname : 比特币
         * logo : https://s2.feixiaoquan.com/logo/1/bitcoin.png?x-oss-process=style/coin_36_webp
         * change_percent : -1.3
         * market_value : 1493038155347
         * vol : 83137505801
         * supply : 18464693
         * rank : 1
         * star_level : 1
         * kline_data : 11542.50,11489.33,11511.79,11753.66,11703.73,11708.43,11775.90,11750.62,11854.65,11874.90,11861.61,11838.98,11875.75,11768.50,11837.18,11897.69,11809.77,11875.49,12350.33,12367.01,12242.25,12236.02,11985.38,12028.80,11784.93,11802.34,11753.08,11724.10
         * market_value_usd : 215685271564
         * vol_usd : 12010098638
         * marketcap : 215685271564
         * high_price : 20089
         * drop_ath : -41.85
         * low_price : 65.526
         * high_time : 2017-12-17
         * low_time : 2013-07-05
         * isifo : 0
         * ismineable : 1
         * ads : ["zbg;https://www.zbg.com/;","MXC;https://www.mxc.io/;https://s1.bqiapp.com/image/20190308/mexc_mid.png","A网;https://aofex.com/;https://s1.bqiapp.com/image/20190611/aofex_mid.png"]
         * adpairs : ["","",""]
         * turnoverrate : 5.57
         * changerate_utc : -0.35
         * changerate_utc8 : -0.39
         */

        private BigDecimal current_price;
        private BigDecimal current_price_usd;
        private String code;
        private String name;
        private String fullname;
        private String logo;
        private BigDecimal change_percent;
        private long market_value;
        private long vol;
        private BigDecimal supply;
        private BigDecimal rank;
        private BigDecimal star_level;
        private String kline_data;
        private long market_value_usd;
        private long vol_usd;
        private long marketcap;
        private BigDecimal high_price;
        private BigDecimal drop_ath;
        private BigDecimal low_price;
        private String high_time;
        private String low_time;
        private BigDecimal isifo;
        private BigDecimal ismineable;
        private BigDecimal turnoverrate;
        private BigDecimal changerate_utc;
        private BigDecimal changerate_utc8;
        private List<String> ads;
        private List<String> adpairs;
    }
}
