package com.jp.app.bitcoin.orderbook.orderbook;

import com.jp.app.bitcoin.orderbook.BasePresenter;
import com.jp.app.bitcoin.orderbook.BaseView;

import java.util.List;

import com.jp.app.bitcoin.orderbook.data.OrderItem;

import jp.com.lib.orderbook.network.datas.LastPrice;
import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 21..
 */
public interface OrderbookContract {
    interface View extends BaseView<OrderbookContract.Presenter> {

        boolean isActive();

        void clearData();

        void drawList(List<OrderItem> orderItemList);

        void drawError(int erroCode);

        void clearDataPrice();

        void drawDataPrice(List<LastPrice> priceItemList);


    }

    interface Presenter extends BasePresenter {
        void setOrderbook(Orderbooks orderbooks);
        void setPrice(List<LastPrice> priceItemList);
        void setError(int erroCode);

        void clearData();
        void clearPriceData();


        void drawListAvailable();
        void drawPriceAvailable();
    }
}
