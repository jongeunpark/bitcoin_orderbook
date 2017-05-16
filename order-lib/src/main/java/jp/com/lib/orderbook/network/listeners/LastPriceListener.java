package jp.com.lib.orderbook.network.listeners;

import jp.com.lib.orderbook.network.datas.LastPrice;
import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 12..
 */
public interface LastPriceListener extends BaseNetworkListener{
    void onSuccess(LastPrice lastPrice);

}
