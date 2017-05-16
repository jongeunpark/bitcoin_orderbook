package com.jp.app.bitcoin.orderbook.main;

import android.content.Context;
import android.content.SharedPreferences;

import com.jp.app.bitcoin.orderbook.BasePresenter;
import com.jp.app.bitcoin.orderbook.BaseView;
import com.jp.app.bitcoin.orderbook.orderbook.OrderbookFragment;

import java.util.List;

import jp.com.lib.orderbook.network.datas.LastPrice;
import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 21..
 */
public interface MainContract {
    interface View extends BaseView<MainContract.Presenter> {


        void drawOrderbook(int marketType, Orderbooks orderbooks);

        void drawPrice(int marketType,  List<LastPrice> priceItem);

        void setTextMaxBuy(String marketName, long price);

        void setTextAvgBuy(long price);

        void setTextMinSell(String marketName, long price);

        void setTextAvgSell(long price);


        boolean isActive();

        void clearData();

        void drawError(int marketType, int ErrorCode);


    }

    interface Presenter extends BasePresenter {


        OrderbookFragment[] generateFragments();

        boolean isFirstTime(SharedPreferences pref);

        void setFirstTime(SharedPreferences pref);

        void getOrderbook(Context context);


        void getInteralCoinoneOrderbook(Context context);

        void getInteralBithumbOrderbook(Context context);

        void getInteralKorbitOrderbook(Context context);

        void getCoinoneOrderbook(Context context);

        void getBithumbOrderbook(Context context);

        void getKorbitOrderbook(Context context);

        void getBtc38LastPrice(Context context);

        void getPoloniexLastPrice(Context context);

        void getKrakenLastPrice(Context context);

        void calSummary();

    }

}
