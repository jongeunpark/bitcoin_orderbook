package jp.com.lib.orderbook.network.datas;

import java.math.BigDecimal;

/**
 * Created by jp on 2017. 5. 15..
 */
public class LastPrice {
    public static int CODE_POLONIEX = 0;
    public static int CODE_KRAKEN = 1;
    public static int CODE_BITFLYER = 2;
    public static int CODE_BTC38 = 3;

    public static int CURRENCY_USD = 11;
    public static int CURRENCY_EUR = 12;
    public static int CURRENCY_JPY = 13;
    public static int CURRENCY_CNY = 14;

    private int code;
    private BigDecimal price;
    private int currency;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }


}
