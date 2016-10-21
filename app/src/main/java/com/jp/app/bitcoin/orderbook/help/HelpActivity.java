package com.jp.app.bitcoin.orderbook.help;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jp.app.bitcoin.orderbook.BaseActivity;
import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.util.ActivityAnimator;
import com.jp.app.bitcoin.orderbook.util.ActivityUtils;


public class HelpActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_act);
        getSupportActionBar().hide();
        HelpFragment helpFragment = (HelpFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(helpFragment == null){
            helpFragment = HelpFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), helpFragment, R.id.contentFrame);
        }
        new HelpPresenter(helpFragment);
    }


}
