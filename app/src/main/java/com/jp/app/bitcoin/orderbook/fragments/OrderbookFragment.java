package com.jp.app.bitcoin.orderbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.activities.MainActivity;
import com.jp.app.bitcoin.orderbook.models.OrderItem;
import com.jp.app.bitcoin.orderbook.views.adapters.OrderbookAdapter;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.com.lib.orderbook.network.datas.Orderbook;
import jp.com.lib.orderbook.network.datas.Orderbooks;

/**
 * Created by jp on 16. 10. 12..
 */
public class OrderbookFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String MARKET_TYPE = "MARKET_TYPE";
    public static final int MARKET_TYPE_KORBIT = 0;
    public static final int MARKET_TYPE_COINONE = 1;
    public static final int MARKET_TYPE_BITHUMB = 2;


    private static final int MAX = 5;
    private WaveSwipeRefreshLayout mRefresh;
    private ListView mListView;
    private Context mContext;
    private int mMarketType = -1;
    private OrderbookAdapter mAdapter;

    private List<OrderItem> orderItemList;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OrderbookFragment newInstance(int marketType) {

        OrderbookFragment fragment = new OrderbookFragment();

        Bundle args = new Bundle();
        args.putInt(MARKET_TYPE, marketType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_orderbook, container, false);
        mMarketType = getArguments().getInt(MARKET_TYPE);
        mListView = (ListView) rootView.findViewById(R.id.orderbook_list);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
        mRefresh = (WaveSwipeRefreshLayout) rootView.findViewById(R.id.orderbook_refresh);

        mRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.

                if (mContext instanceof MainActivity && mAdapter != null) {
                    orderItemList = null;
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                    if (mMarketType == MARKET_TYPE_KORBIT) {
                        ((MainActivity) mContext).getKorbitOrderbook();
                    } else if (mMarketType == MARKET_TYPE_COINONE) {
                        ((MainActivity) mContext).getCoinoneOrderbook();
                    } else if (mMarketType == MARKET_TYPE_BITHUMB) {
                        ((MainActivity) mContext).getBithumbOrderbook();
                    }

                } else {
                    if (mRefresh.isRefreshing()) {
                        mRefresh.setRefreshing(false);
                    }

                }
            }
        });
        mRefresh.setWaveRGBColor(0xFF, 0xFF, 0xFF);

        if (orderItemList != null && orderItemList.size() > 0) {
            mAdapter = new OrderbookAdapter(getActivity(), R.layout.row_orderbook, orderItemList);
            mListView.setAdapter(mAdapter);
            moveCenter();
        }

        return rootView;
    }

    public void setOrderbooks(Orderbooks orderbooks) {
        if (orderbooks != null && orderbooks.getAskArray().size() > MAX && orderbooks.getBidArray().size() > MAX) {
            Collections.sort(orderbooks.getAskArray(), new Comparator<Orderbook>() {
                @Override
                public int compare(Orderbook t1, Orderbook t2) {

                    return t2.getPrice().compareTo(t1.getPrice());
                }
            });

            Collections.sort(orderbooks.getBidArray(), new Comparator<Orderbook>() {
                @Override
                public int compare(Orderbook t1, Orderbook t2) {
                    return t2.getPrice().compareTo(t1.getPrice());
                }
            });

            orderItemList = new ArrayList<OrderItem>();
            int end = orderbooks.getAskArray().size();
            int start = end - MAX;

            for (Orderbook orderbook : orderbooks.getAskArray().subList(start, end)) {


                orderItemList.add(new OrderItem(orderbook.getPrice(), orderbook.getQty(), OrderItem.ORDER_TYPE.ASK));
            }
            start = 0;
            end = MAX;
            for (Orderbook orderbook : orderbooks.getBidArray().subList(start, end)) {

                orderItemList.add(new OrderItem(orderbook.getPrice(), orderbook.getQty(), OrderItem.ORDER_TYPE.BID));
            }
            if (isAdded()) {
                mAdapter = new OrderbookAdapter(getActivity(), R.layout.row_orderbook, orderItemList);
                mListView.setAdapter(mAdapter);
                moveCenter();
            }
        }


    }

    public void offLoading() {

        if (mRefresh != null && mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(false);
        }

    }

    private void moveCenter() {


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext = context;
    }

    public void reset() {
        orderItemList = null;
        if (mAdapter != null) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
}