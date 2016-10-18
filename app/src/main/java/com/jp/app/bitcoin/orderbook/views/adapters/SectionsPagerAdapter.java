package com.jp.app.bitcoin.orderbook.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jp.app.bitcoin.orderbook.activities.MainActivity;
import com.jp.app.bitcoin.orderbook.fragments.OrderbookFragment;

import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 12..
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private OrderbookFragment[] mFragments;

    public SectionsPagerAdapter(FragmentManager fm, OrderbookFragment[] fragments) {
        super(fm);
        mFragments = fragments;
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

        }
        return null;
    }

    public void setmKorbitOrderbooks(Orderbooks orderbooks) {
        mFragments[OrderbookFragment.MARKET_TYPE_KORBIT].setOrderbooks(orderbooks);
    }

    public void setmCoinoneOrderbooks(Orderbooks orderbooks) {
        mFragments[OrderbookFragment.MARKET_TYPE_COINONE].setOrderbooks(orderbooks);
    }

    public void setmBithumbOrderbooks(Orderbooks orderbooks) {
        mFragments[OrderbookFragment.MARKET_TYPE_BITHUMB].setOrderbooks(orderbooks);
    }



}
