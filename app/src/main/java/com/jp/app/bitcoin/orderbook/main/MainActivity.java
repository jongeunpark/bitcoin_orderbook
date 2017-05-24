package com.jp.app.bitcoin.orderbook.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jp.app.bitcoin.orderbook.BaseActivity;
import com.jp.app.bitcoin.orderbook.BuildConfig;
import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.help.HelpActivity;
import com.jp.app.bitcoin.orderbook.util.ActivityAnimator;
import com.jp.app.bitcoin.orderbook.util.ActivityUtils;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String KORBIT = "KORBIT";
    public static final String COINONE = "COINONE";
    public static final String BITHUMB = "BITHUMB";
    public static final String ETC = "해외 시세";

    public static final int HELP_ACTIVITY_REQUESTCODE = 101;
    private MainPresenter mainPresenter;
    private FloatingActionButton mFab;
    private WebView mWebView;
    private View mViewChart;
    private View mViewChartTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

        setContentView(R.layout.main_act);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.contentFrame);
        }
        mainPresenter = new MainPresenter(mainFragment);

        if (mainPresenter.isFirstTime(getSharedPreferences("BITCOIN_PRICE", MODE_PRIVATE))) {

            openHelp();
        }

        TextView title = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_text_title);

        try {
            title.setText(getString(getApplicationInfo().labelRes)
                    + " "
                    + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

        } catch (PackageManager.NameNotFoundException e) {

            title.setText(getString(getApplicationInfo().labelRes));
        }
        mViewChart = findViewById(R.id.main_view_chart);
        mViewChartTop = findViewById(R.id.chart_view_top);
        mFab = (FloatingActionButton) findViewById(R.id.main_fab_chart);
        mWebView = (WebView) findViewById(R.id.chart_web_chart);
        WebSettings set = mWebView.getSettings();

        set.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });





        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mViewChart.getVisibility() == View.GONE) {

                    mViewChart.setVisibility(View.VISIBLE);
                    mFab.setVisibility(View.GONE);
                }
            }
        });
        mViewChartTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mViewChart.getVisibility() == View.VISIBLE) {

                    mViewChart.setVisibility(View.GONE);
                    mFab.setVisibility(View.VISIBLE);
                }
            }
        });
        showWebView();


    }

    @Override
    public void onBackPressed() {


        finishMyApp();

    }

    public void finishMyApp() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(mViewChart.getVisibility() == View.VISIBLE){
                mViewChart.setVisibility(View.GONE);
                mFab.setVisibility(View.VISIBLE);
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mene_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_main_refresh:
                showWebView();
                mainPresenter.getOrderbook(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.main_nav_help) {
            openHelp();
        } else if (id == R.id.main_nav_review) {
            openMarket();
        } else if (id == R.id.main_nav_share) {
            openShareApp();
        } else if (id == R.id.main_nav_send) {
            openEmailApp();
        } else if (id == R.id.main_nav_korbit) {
            openWeb("https://www.korbit.co.kr/");
        } else if (id == R.id.main_nav_coinone) {
            openWeb("https://coinone.co.kr/");
        } else if (id == R.id.main_nav_bithumb) {
            openWeb("https://www.bithumb.com/");
        }
        else if (id == R.id.main_nav_eth_orderbook){
            openApp("com.jp.app.ethereum.orderbook");
        }else if (id == R.id.main_nav_ltc_orderbook){
            openApp("com.jp.app.ltc.orderbook");
        }else if (id == R.id.main_nav_etc_orderbook){
            openApp("com.jp.app.etc.orderbook");
        }else if (id == R.id.main_nav_dash_orderbook){
            openApp("com.jp.app.dash.orderbook");
        }else if (id == R.id.main_nav_xrp_orderbook){
            openApp("com.jp.app.ripple.orderbook");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openApp(String packageName){
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

            startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri
                        .parse("market://details?id="+packageName));
                startActivity(intent2);
            } catch (Exception e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }

    }

    public void openHelp() {
        startActivityForResult(new Intent(this, HelpActivity.class), MainActivity.HELP_ACTIVITY_REQUESTCODE);
        ActivityAnimator a = new ActivityAnimator();
        a.PullLeftPushRight(this);
    }

    private void openWeb(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            startActivity(i);
        } catch (Exception e) {

        }

    }

    private void openMarket() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri
                    .parse("market://details?id="
                            + getPackageManager().getPackageInfo(
                            getPackageName(), 0).packageName));
            startActivity(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void openShareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        try {
            sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id="
                            + getPackageManager().getPackageInfo(
                            getPackageName(), 0).packageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void openEmailApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        String body = "\n\n\n\n\n--------------------\n아래 내용을 지우거나 수정하지 마세요.\n";
        try {
            body += "\nApp Name : " + getString(getApplicationInfo().labelRes);
            body += "\nApp Version : "
                    + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            body += "\nOS Version : " + android.os.Build.VERSION.RELEASE;
            body += "\nDevice : " + android.os.Build.MODEL;
            intent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"jp.jongeun.park@gmail.com.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == HELP_ACTIVITY_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                mainPresenter.setFirstTime(getSharedPreferences("BITCOIN_PRICE", MODE_PRIVATE));
            }
        }
    }
    private void showWebView(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = (float) ((float) (displayMetrics.heightPixels  - convertDpToPixel(56*2, this)) * 0.9);
        float width = (float) (displayMetrics.widthPixels * 0.95);
        String content = "<!-- TradingView Widget BEGIN -->\n" +
                "<script type=\"text/javascript\" src=\"https://d33t3vvu2t2yu5.cloudfront.net/tv.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "new TradingView.widget({\n" +
                "  \"width\": "+convertPixelsToDp(width, this)+",\n" +
                "  \"height\": "+convertPixelsToDp(height/2, this)+",\n" +
                "  \"symbol\": \"POLONIEX:BTCUSDT\",\n" +
                "  \"interval\": \"D\",\n" +
                "  \"timezone\": \"Etc/UTC\",\n" +
                "  \"theme\": \"White\",\n" +
                "  \"style\": \"1\",\n" +
                "  \"locale\": \"en\",\n" +
                "  \"toolbar_bg\": \"#f1f3f6\",\n" +
                "  \"enable_publishing\": false,\n" +
                "  \"hide_top_toolbar\": true,\n" +
                "  \"save_image\": false,\n" +
                "  \"hideideas\": true\n" +
                "});\n" +
                "</script>\n" +
                "<!-- TradingView Widget END -->\n";
        mWebView.loadData(content, "text/html", "UTF-8");
    }
    private static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
