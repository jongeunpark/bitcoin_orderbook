package com.jp.app.bitcoin.orderbook.help;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import static com.google.common.base.Preconditions.checkNotNull;

import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.util.ActivityAnimator;

/**
 * Created by jp on 16. 10. 21..
 */
public class HelpFragment extends Fragment implements HelpContract.View{

    private HelpContract.Presenter mPresenter;
    private int step = 0;

    private View mViewMain;
    private View mViewNav, mViewSummary, mViewPager, mViewActionbar;
    private ImageView mImageNav, mImageSummary, mImagePager;

    public static HelpFragment newInstance(){


        return new HelpFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.help_frag, container, false);

        mViewMain = root.findViewById(R.id.help_view_main);
        mViewNav = root.findViewById(R.id.help_view_nav);
        mViewSummary = root.findViewById(R.id.help_view_summary);
        mViewPager = root.findViewById(R.id.help_view_viewpager);
        mViewActionbar = root.findViewById(R.id.help_view_actionbar);
        mImageNav = (ImageView) root.findViewById(R.id.main_image_nav);
        mImageSummary = (ImageView) root.findViewById(R.id.main_image_summary);
        mImagePager = (ImageView) root.findViewById(R.id.main_image_pager);
        mViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showNextStep();


            }
        });
        return root;
    }


    @Override
    public void setPresenter(@NonNull HelpContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showStep1() {
        mImageNav.setVisibility(View.GONE);
        mViewNav.setAlpha(0.8f);
        mViewNav.setBackgroundColor(Color.parseColor("#000000"));
        mViewSummary.setAlpha(1.0f);
        mViewSummary.setBackgroundResource(R.drawable.white_rect);
        mImageSummary.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStep2() {
        mImageSummary.setVisibility(View.GONE);
        mViewSummary.setAlpha(0.8f);
        mViewSummary.setBackgroundColor(Color.parseColor("#000000"));
        mViewPager.setAlpha(1.0f);
        mViewPager.setBackgroundResource(R.drawable.white_rect);
        mImagePager.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStep3() {

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
        ActivityAnimator a = new ActivityAnimator();
        a.PullRightPushLeft(getActivity());
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
