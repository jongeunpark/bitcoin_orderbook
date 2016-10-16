package jp.com.lib.orderbook.network.services;


import jp.com.lib.orderbook.network.datas.Orderbooks;
import jp.com.lib.orderbook.network.listeners.OrderBookArrayListener;

/**
 * Created by jp on 16. 10. 12..
 */
public interface PublickApi {
    Orderbooks getOrderbook(OrderBookArrayListener listener) throws Exception;
}
