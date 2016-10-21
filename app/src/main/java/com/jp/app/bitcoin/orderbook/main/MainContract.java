package com.jp.app.bitcoin.orderbook.main;

import android.content.Context;
import android.content.SharedPreferences;

import com.jp.app.bitcoin.orderbook.BasePresenter;
import com.jp.app.bitcoin.orderbook.BaseView;
import com.jp.app.bitcoin.orderbook.orderbook.OrderbookFragment;

import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 21..
 */
public interface MainContract {
    interface View extends BaseView<MainContract.Presenter> {


        void drawOrderbook(int marketType, Orderbooks orderbooks);

        void setTextMaxBuy(String marketName, long price);

        void setTextAvgBuy(long price);

        void setTextMinSell(String marketName, long price);

        void setTextAvgSell(long price);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {


        OrderbookFragment[] generateFragments();

        boolean isFirstTime(SharedPreferences pref);

        void setFirstTime(SharedPreferences pref);

        void getOrderbook(Context context);

        void getCoinoneOrderbook(Context context);

        void getBithumbOrderbook(Context context);

        void getKorbitOrderbook(Context context);

        void calSummary();

    }

}
