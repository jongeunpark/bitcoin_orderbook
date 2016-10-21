package com.jp.app.bitcoin.orderbook.data;

import java.math.BigDecimal;

/**
 * Created by jp on 16. 10. 13..
 */
public class OrderItem {

    private BigDecimal price;
    private BigDecimal qty;
    private ORDER_TYPE order_type;
    public static enum ORDER_TYPE{
        BID, ASK
    };


    public OrderItem(BigDecimal price, BigDecimal qty, ORDER_TYPE order_type){
        this.price = price;
        this.qty = qty;
        this.order_type = order_type;
    }
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

    public ORDER_TYPE getOrder_type() {
        return order_type;
    }

    public void setOrder_type(ORDER_TYPE order_type) {
        this.order_type = order_type;
    }






}
