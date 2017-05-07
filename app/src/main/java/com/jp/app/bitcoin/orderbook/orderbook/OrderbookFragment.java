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


import java.util.List;

import com.jp.app.bitcoin.orderbook.data.OrderItem;

/**
 * Created by jp on 16. 10. 12..
 */
public class OrderbookFragment extends Fragment implements OrderbookContract.View {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String MARKET_TYPE = "MARKET_TYPE";
    public static final int MARKET_TYPE_KORBIT = 0;
    public static final int MARKET_TYPE_COINONE = 1;
    public static final int MARKET_TYPE_BITHUMB = 2;
    private OrderbookContract.Presenter mPresenter;


    private int mMarketType = -1;

    private ListView mListView;
    private TextView mTextError;
    private Context mContext;
    private OrderbookAdapter mAdapter;


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
    public void onResume() {
        super.onResume();
        if(mPresenter!= null) {
            mPresenter.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.orderbook_frag, container, false);
        mMarketType = getArguments().getInt(MARKET_TYPE);
        mListView = (ListView) rootView.findViewById(R.id.orderbook_list);
        mTextError = (TextView) rootView.findViewById(R.id.orderbook_error);
        if(mPresenter != null) {
            mPresenter.drawListAvailable();
        }


        return rootView;
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
}