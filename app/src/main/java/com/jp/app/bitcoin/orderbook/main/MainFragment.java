package com.jp.app.bitcoin.orderbook.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.views.adapters.SectionsPagerAdapter;

import java.util.List;

import github.chenupt.springindicator.SpringIndicator;
import jp.com.lib.orderbook.network.datas.LastPrice;
import jp.com.lib.orderbook.network.datas.Orderbooks;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jp on 16. 10. 21..
 */
public class MainFragment extends Fragment implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SpringIndicator mSpringIndicator;
    private TextView mTextMinSell;
    private TextView mTextMaxBuy;
    private TextView mTextAvgSell;
    private TextView mTextAvgBuy;
    private TextView mTextTitle;

    private TextView mTextRefresh;
    private Handler mHandle;
    private Runnable mRunnable;
    private MyCount counter;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        mRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandle.removeCallbacks(mRunnable);
        stopCountDown();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(), mPresenter.generateFragments());

        mSpringIndicator = (SpringIndicator) root.findViewById(R.id.main_indicator);
        mViewPager = (ViewPager) root.findViewById(R.id.main_viewpager_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSpringIndicator.setViewPager(mViewPager);
        mTextMaxBuy = (TextView) root.findViewById(R.id.main_summary_buy);
        mTextMinSell = (TextView) root.findViewById(R.id.main_summary_sell);
        mTextAvgBuy = (TextView) root.findViewById(R.id.main_summary_avg_buy);
        mTextAvgSell = (TextView) root.findViewById(R.id.main_summary_avg_sell);
        mTextRefresh = (TextView) root.findViewById(R.id.main_text_refresh);


        return root;
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        initHandler();
    }

    private void initHandler(){
        mHandle = new Handler();
        mRunnable = new Runnable() {

            @Override
            public void run() {

                startCountDown();
                mPresenter.getOrderbook(getActivity());
                mHandle.postDelayed(mRunnable, 30000);
            }
        };
    }
    private void stopCountDown() {
        if (counter != null) {
            counter.cancel();

            counter = null;
        }
    }

    private void startCountDown() {
        if (counter == null) {

            counter = new MyCount(30000, 1000);
        }

        counter.start();
    }


    @Override
    public void drawOrderbook(int marketType, Orderbooks orderbooks) {
        mSectionsPagerAdapter.setOrderbooks(marketType, orderbooks);

    }

    @Override
    public void drawPrice(int marketType, List<LastPrice> priceItem) {
        mSectionsPagerAdapter.setPrice(marketType, priceItem);
    }

    @Override
    public void setTextMaxBuy(String marketName, long price) {
            String fullMsg = String.format(getString(R.string.main_summary_buy), marketName, String.format("%,d", price));
            final SpannableStringBuilder sp = new SpannableStringBuilder(fullMsg);
            int start = 7;
            int end = fullMsg.indexOf("(");

            sp.setSpan(
                    new ForegroundColorSpan(getResources().getColor(R.color.red_background)), start
                    , end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mTextMaxBuy.setText(sp);
    }

    @Override
    public void setTextAvgBuy(long price) {
        mTextAvgBuy.setText(String.format(getString(R.string.main_summary_avg_buy), String.format("%,d", price)));
    }

    @Override
    public void setTextMinSell(String marketName, long price) {
        String fullMsg = String.format(getString(R.string.main_summary_sell), marketName, String.format("%,d",price ));
        final SpannableStringBuilder sp = new SpannableStringBuilder(fullMsg);
        int start = 7;
        int end = fullMsg.indexOf("(");

        sp.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.blue_background)), start
                , end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTextMinSell.setText(sp);
    }

    @Override
    public void setTextAvgSell(long price) {
        mTextAvgSell.setText(String.format(getString(R.string.main_summary_avg_sell), String.format("%,d", price)));
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void clearData() {
        mHandle.removeCallbacks(mRunnable);
        stopCountDown();
        initHandler();
        mPresenter.start();
        mRunnable.run();

    }

    @Override
    public void drawError(int marketType, int ErrorCode) {
        mSectionsPagerAdapter.setError(marketType, ErrorCode);
    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onTick(long millisUntilFinished) {


            mTextRefresh.setText((millisUntilFinished / 1000) + " " + getString(R.string.refresh));

        }
    }
}
