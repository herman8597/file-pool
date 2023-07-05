package network.vena.cooperation.util;

/**
 * 币种类型
 */
public enum CoinType {
    BTC("BTC", Series.BTC, "", 8),
    ETH("ETH", Series.ETH, "", 18),
    USDT("USDT", Series.ETH, "0xdac17f958d2ee523a2206206994597c13d831ec7", 6),
    FILP("FILP", Series.ETH, "0x54619a633df4e8D0E4588AA7b267eb3FDb1dE803", 18),
    ;
    //币种
    private String symbol;
    //系列
    private Series series;
    //合约地址，如果有
    private String contractAddr;
    //币种精度
    private Integer decimal;

    CoinType(String symbol, Series series, String contractAddr, Integer decimal) {
        this.symbol = symbol;
        this.series = series;
        this.contractAddr = contractAddr;
        this.decimal = decimal;
    }

    public String getSymbol() {
        return symbol;
    }

    public Series getSeries() {
        return series;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public Integer getDecimal() {
        return decimal;
    }

    /**
     * 根据币种名称获取类型
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    public static CoinType valueBySymbol(String symbol)  {
        symbol = symbol.toUpperCase();
        if (BTC.getSymbol().equals(symbol)) {
            return BTC;
        } else if (ETH.getSymbol().equals(symbol)) {
            return ETH;
        } else if (USDT.getSymbol().equals(symbol)) {
            return USDT;
        }
        throw new NullPointerException("不支持该币种");
    }

}
