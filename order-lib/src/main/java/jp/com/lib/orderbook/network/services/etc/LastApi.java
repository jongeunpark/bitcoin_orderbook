package jp.com.lib.orderbook.network.services.etc;

import jp.com.lib.orderbook.network.listeners.LastPriceListener;
import jp.com.lib.orderbook.network.listeners.OrderBooksListener;

/**
 * Created by jp on 2017. 5. 15..
 */
public interface LastApi {
    void getListPrice(String currency, LastPriceListener listener) throws Exception;
}
