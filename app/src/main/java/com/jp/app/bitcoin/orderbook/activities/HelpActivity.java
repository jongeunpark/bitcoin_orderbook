package com.jp.app.bitcoin.orderbook.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.anims.ActivityAnimator;
import com.jp.app.bitcoin.orderbook.fragments.OrderbookFragment;
import com.jp.app.bitcoin.orderbook.views.adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import jp.com.lib.orderbook.network.datas.Orderbooks;
import jp.com.lib.orderbook.network.listeners.OrderBookArrayListener;
import jp.com.lib.orderbook.network.services.BithumbPublicApiImpl;
import jp.com.lib.orderbook.network.services.CoinonePublicApiImpl;
import jp.com.lib.orderbook.network.services.KorbitPublicApiImpl;
import jp.com.lib.orderbook.network.services.PublickApi;


public class HelpActivity extends BaseActivity {


    private int step = 0;
    private View mViewMain;
    private View mViewNav, mViewSummary, mViewPager, mViewActionbar;
    private ImageView mImageNav, mImageSummary, mImagePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initView() {
        mViewMain = findViewById(R.id.help_view_main);
        mViewNav = findViewById(R.id.help_view_nav);
        mViewSummary = findViewById(R.id.help_view_summary);
        mViewPager = findViewById(R.id.help_view_viewpager);
        mViewActionbar = findViewById(R.id.help_view_actionbar);
        mImageNav = (ImageView) findViewById(R.id.main_image_nav);
        mImageSummary = (ImageView) findViewById(R.id.main_image_summary);
        mImagePager = (ImageView) findViewById(R.id.main_image_pager);
    }

    private void initEvent() {
        mViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (step == 0) {
                    mImageNav.setVisibility(View.GONE);
                    mViewNav.setAlpha(0.8f);
                    mViewNav.setBackgroundColor(Color.parseColor("#000000"));
                    mViewSummary.setAlpha(1.0f);
                    mViewSummary.setBackgroundResource(R.drawable.white_rect);
                    mImageSummary.setVisibility(View.VISIBLE);
                    step++;

                } else if (step == 1) {

                    mImageSummary.setVisibility(View.GONE);
                    mViewSummary.setAlpha(0.8f);
                    mViewSummary.setBackgroundColor(Color.parseColor("#000000"));
                    mViewPager.setAlpha(1.0f);
                    mViewPager.setBackgroundResource(R.drawable.white_rect);
                    mImagePager.setVisibility(View.VISIBLE);
                    step++;
                } else if (step == 2) {

                    setResult(RESULT_OK);
                    finish();
                    ActivityAnimator a = new ActivityAnimator();
                    a.PullRightPushLeft(HelpActivity.this);

                }
            }
        });
    }

}
