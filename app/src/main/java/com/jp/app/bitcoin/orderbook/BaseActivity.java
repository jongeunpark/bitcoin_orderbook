package com.jp.app.bitcoin.orderbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.jp.app.bitcoin.orderbook.BuildConfig;
import com.jp.app.bitcoin.orderbook.R;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jp on 16. 10. 16..
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

    }

}
