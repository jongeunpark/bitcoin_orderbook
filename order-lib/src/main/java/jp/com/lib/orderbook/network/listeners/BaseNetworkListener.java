package jp.com.lib.orderbook.network.listeners;

/**
 * Created by jp on 16. 10. 12..
 */
public interface BaseNetworkListener {
    void onFailure(int httpStatusCode, Throwable throwable);
}
