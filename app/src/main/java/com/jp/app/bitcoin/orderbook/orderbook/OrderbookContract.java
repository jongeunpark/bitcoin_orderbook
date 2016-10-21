package com.jp.app.bitcoin.orderbook.orderbook;

import com.jp.app.bitcoin.orderbook.BasePresenter;
import com.jp.app.bitcoin.orderbook.BaseView;
import com.jp.app.bitcoin.orderbook.data.OrderItem;

import java.util.List;

import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 21..
 */
public interface OrderbookContract {
    interface View extends BaseView<OrderbookContract.Presenter>{

        boolean isActive();
        void clearData();
        void drawList(List<OrderItem> orderItemList);

    }
    interface Presenter extends BasePresenter{
        void setOrderbook(Orderbooks orderbooks);
        void clearData();
        void drawListAvailable();
    }
}
