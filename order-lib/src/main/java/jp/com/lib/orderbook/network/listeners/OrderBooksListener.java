package jp.com.lib.orderbook.network.listeners;

import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 12..
 */
public interface OrderBooksListener extends BaseNetworkListener{
    void onSuccess(Orderbooks orderbooks);

}
