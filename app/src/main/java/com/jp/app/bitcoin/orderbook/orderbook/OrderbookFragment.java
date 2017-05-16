package com.jp.app.bitcoin.orderbook.orderbook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.views.adapters.OrderbookAdapter;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jp.app.bitcoin.orderbook.data.OrderItem;
import com.jp.app.bitcoin.orderbook.views.adapters.PriceAdapter;

import jp.com.lib.orderbook.network.datas.LastPrice;

/**
 * Created by jp on 16. 10. 12..
 */
public class OrderbookFragment extends Fragment implements OrderbookContract.View {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String MARKET_TYPE = "MARKET_TYPE";
    public static final String VIEW_TYPE = "VIEW_TYPE";
    public static final int MARKET_TYPE_KORBIT = 0;
    public static final int MARKET_TYPE_COINONE = 1;
    public static final int MARKET_TYPE_BITHUMB = 2;
    public static final int MARKET_TYPE_ETC = 3;
    public static final String VIEW_TYPE_ORDERBOOK = "ORDERBOOK";
    public static final String VIEW_TYPE_PRICE = "PRICE";
    private OrderbookContract.Presenter mPresenter;


    private int mMarketType = -1;
    private String mViewType;

    private ListView mListView;
    private TextView mTextError;
    private Context mContext;
    private OrderbookAdapter mAdapter;

    private PriceAdapter mPriceAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OrderbookFragment newInstance(int marketType, String viewType) {

        OrderbookFragment fragment = new OrderbookFragment();

        Bundle args = new Bundle();
        args.putInt(MARKET_TYPE, marketType);
        args.putString(VIEW_TYPE, viewType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPresenter!= null) {
            mPresenter.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMarketType = getArguments().getInt(MARKET_TYPE);
        mViewType = getArguments().getString(VIEW_TYPE);
        View rootView = null;
        if(mViewType.equals(VIEW_TYPE_ORDERBOOK)) {
            rootView = inflater.inflate(R.layout.orderbook_frag, container, false);




        }else if(mViewType.equals(VIEW_TYPE_PRICE)){
            rootView = inflater.inflate(R.layout.price_frag, container, false);
        }
        mTextError = (TextView) rootView.findViewById(R.id.orderbook_error);
        mListView = (ListView) rootView.findViewById(R.id.orderbook_list);


        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if(mPresenter != null) {
            if(mViewType.equals(VIEW_TYPE_ORDERBOOK)) {
                mPresenter.drawListAvailable();
            }else if(mViewType.equals(VIEW_TYPE_PRICE)){
                mPresenter.drawPriceAvailable();
            }
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void setPresenter(OrderbookContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public boolean isActive() {

        return isAdded();
    }

    @Override
    public void clearData() {
        mPresenter.clearData();
        if (mAdapter != null) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void drawList(List<OrderItem> orderItemList) {
        mListView.setVisibility(View.VISIBLE);
        mTextError.setVisibility(View.GONE);
        mAdapter = new OrderbookAdapter(getActivity(), R.layout.orderbook_item, orderItemList);
        mListView.setAdapter(mAdapter);
        int target =  orderItemList.size() / 2 - 3;
        if (target < 0) target = 0;
        mListView.setSelection(target);


    }

    @Override
    public void drawError(int erroCode) {
        mListView.setVisibility(View.GONE);
        mTextError.setVisibility(View.VISIBLE);
        if(erroCode == 0){
            mTextError.setText(getString(R.string.errro_0));
        }else if(erroCode == 404 || erroCode == 200){
            mTextError.setText(getString(R.string.errro_404_200));
        }else{
            mTextError.setText(getString(R.string.errro_etc));
        }
    }

    @Override
    public void clearDataPrice() {
        if (mPriceAdapter != null) {
            mPriceAdapter.clear();
            mPriceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void drawDataPrice(List<LastPrice> priceItemList) {
        mListView.setVisibility(View.VISIBLE);
        mTextError.setVisibility(View.GONE);

        Collections.sort(priceItemList, new Comparator<LastPrice>() {
            @Override
            public int compare(LastPrice t1, LastPrice t2) {
                return t1.getCode() > t2.getCode() ? -1 : (t1.getCode() < t2.getCode() ) ? 1 : 0;
            }
        });

        mPriceAdapter = new PriceAdapter(getActivity(), R.layout.price_item, priceItemList);
        mListView.setAdapter(mPriceAdapter);
    }
}