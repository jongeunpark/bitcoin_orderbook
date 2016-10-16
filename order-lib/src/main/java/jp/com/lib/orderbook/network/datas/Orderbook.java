package jp.com.lib.orderbook.network.datas;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jp on 16. 10. 12..
 */
public class Orderbook{
    private BigDecimal price;
    private BigDecimal qty;


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
