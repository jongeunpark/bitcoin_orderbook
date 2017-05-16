package com.jp.app.bitcoin.orderbook.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jp.app.bitcoin.orderbook.main.MainActivity;
import com.jp.app.bitcoin.orderbook.orderbook.OrderbookFragment;
import com.jp.app.bitcoin.orderbook.orderbook.OrderbookPresenter;

import java.util.ArrayList;
import java.util.List;

import jp.com.lib.orderbook.network.datas.LastPrice;
import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 12..
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private OrderbookFragment[] mFragments;
    private List<OrderbookPresenter> mPresenters;

    public SectionsPagerAdapter(FragmentManager fm, OrderbookFragment[] fragments) {
        super(fm);
        mFragments = fragments;
        mPresenters = new ArrayList<>();

        for (OrderbookFragment fragment : fragments) {
            mPresenters.add(new OrderbookPresenter(fragment));
        }

    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {

        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case OrderbookFragment.MARKET_TYPE_KORBIT:
                return MainActivity.KORBIT;
            case OrderbookFragment.MARKET_TYPE_COINONE:
                return MainActivity.COINONE;
            case OrderbookFragment.MARKET_TYPE_BITHUMB:
                return MainActivity.BITHUMB;
            case OrderbookFragment.MARKET_TYPE_ETC:
                return MainActivity.ETC;

        }
        return null;
    }

    public void setOrderbooks(int marketType, Orderbooks orderbooks) {
        mPresenters.get(marketType).setOrderbook(orderbooks);
    }
    public void setError(int marketType, int erroCode) {
        mPresenters.get(marketType).setError(erroCode);
    }
    public void setPrice(int marketType, List<LastPrice> priceItemList) {
        mPresenters.get(marketType).setPrice(priceItemList);
    }


}
