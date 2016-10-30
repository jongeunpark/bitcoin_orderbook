package jp.com.lib.orderbook.network.services.exteral;


import jp.com.lib.orderbook.network.datas.Orderbooks;
import jp.com.lib.orderbook.network.listeners.OrderBooksListener;

/**
 * Created by jp on 16. 10. 12..
 */
public interface PublickApi {
    void getInteralOrderbook(OrderBooksListener listener) throws Exception;
    void getOrderbook(OrderBooksListener listener) throws Exception;

}
