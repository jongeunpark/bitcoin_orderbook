package com.jp.app.bitcoin.orderbook.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.jp.app.bitcoin.orderbook.R;
import com.jp.app.bitcoin.orderbook.anims.ActivityAnimator;
import com.jp.app.bitcoin.orderbook.fragments.OrderbookFragment;
import com.jp.app.bitcoin.orderbook.views.adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.com.lib.orderbook.network.datas.Orderbooks;
import jp.com.lib.orderbook.network.listeners.OrderBookArrayListener;
import jp.com.lib.orderbook.network.services.BithumbPublicApiImpl;

import jp.com.lib.orderbook.network.services.CoinonePublicApiImpl;
import jp.com.lib.orderbook.network.services.KorbitPublicApiImpl;
import jp.com.lib.orderbook.network.services.PublickApi;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String KORBIT = "KORBIT";
    public static final String COINONE = "COINONE";
    public static final String BITHUMB = "BITHUMB";
    public static final String OKCOIN = "OKCOIN.CN";
    private static final int HELP_ACTIVITY_REQUESTCODE = 101;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private OrderbookFragment mCoinoneFragment, mBithumbFragment, mKorbitFragment;

    private long maxPriceInCoinone = -1;
    private long maxPriceInKorbit = -1;
    private long maxPriceInBithumb = -1;

    private long minPriceInCoinone = -1;
    private long minPriceInKorbit = -1;
    private long minPriceInBithumb = -1;


    private TextView mTextMinSell;
    private TextView mTextMaxBuy;
    private TextView mTextAvgSell;
    private TextView mTextAvgBuy;
    private TextView mTextTitle;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (isFirst()) {
            openHelp();
        }
        initView();
        initEvent();
        getOrderbook();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), generateFragments());

        mTextTitle = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_text_title);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTextMaxBuy = (TextView) findViewById(R.id.main_summary_buy);
        mTextMinSell = (TextView) findViewById(R.id.main_summary_sell);
        mTextAvgBuy = (TextView) findViewById(R.id.main_summary_avg_buy);
        mTextAvgSell = (TextView) findViewById(R.id.main_summary_avg_sell);

        try {
            mTextTitle.setText(getString(getApplicationInfo().labelRes)
                    + " "
                    + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

        } catch (PackageManager.NameNotFoundException e) {

            mTextTitle.setText(getString(getApplicationInfo().labelRes));
        }


    }

    private void initEvent() {

    }

    private OrderbookFragment[] generateFragments() {
        mKorbitFragment = OrderbookFragment.newInstance(OrderbookFragment.MARKET_TYPE_KORBIT);
        mCoinoneFragment = OrderbookFragment.newInstance(OrderbookFragment.MARKET_TYPE_COINONE);
        mBithumbFragment = OrderbookFragment.newInstance(OrderbookFragment.MARKET_TYPE_BITHUMB);

        return new OrderbookFragment[]{mKorbitFragment, mCoinoneFragment, mBithumbFragment};

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                getOrderbook();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openHelp() {
        startActivityForResult(new Intent(MainActivity.this, HelpActivity.class), HELP_ACTIVITY_REQUESTCODE);
        ActivityAnimator a = new ActivityAnimator();
        a.PullLeftPushRight(this);
    }


    private boolean isFirst() {
        SharedPreferences pref = getSharedPreferences("BITCOIN_PRICE", MODE_PRIVATE);
        return pref.getBoolean("is_first", true);
    }


    private void setFirst() {
        SharedPreferences pref = getSharedPreferences("BITCOIN_PRICE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("is_first", false);
        editor.commit();
    }

    public void getCoinoneOrderbook() {
        PublickApi coinonePublickApi = CoinonePublicApiImpl.getInstance(this);
        try {

            coinonePublickApi.getOrderbook(new OrderBookArrayListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {

                        mSectionsPagerAdapter.setmCoinoneOrderbooks(orderbooks);
                        orderbooks.sort();
                        minPriceInCoinone = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInCoinone = orderbooks.getBidArray().get(0).getPrice().longValue();
                        drawPriceView();
                        mCoinoneFragment.offLoading();
                    }
                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getKorbitOrderbook() {
        PublickApi korbitPublickApi = KorbitPublicApiImpl.getInstance(this);
        try {

            korbitPublickApi.getOrderbook(new OrderBookArrayListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {

                        mSectionsPagerAdapter.setmKorbitOrderbooks(orderbooks);
                        orderbooks.sort();

                        minPriceInKorbit = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInKorbit = orderbooks.getBidArray().get(0).getPrice().longValue();
                        drawPriceView();

                        mKorbitFragment.offLoading();
                    }
                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getBithumbOrderbook() {
        PublickApi bithumbPublicAPI = BithumbPublicApiImpl.getInstance(this);
        try {
            bithumbPublicAPI.getOrderbook(new OrderBookArrayListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {

                        mSectionsPagerAdapter.setmBithumbOrderbooks(orderbooks);
                        orderbooks.sort();
                        minPriceInBithumb = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInBithumb = orderbooks.getBidArray().get(0).getPrice().longValue();
                        drawPriceView();
                        mBithumbFragment.offLoading();
                    }

                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOrderbook() {
        if(isLoading){
            return;
        }
        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              isLoading = false;
            }
        },1000);
        mBithumbFragment.reset();
        mCoinoneFragment.reset();
        mKorbitFragment.reset();
        getCoinoneOrderbook();
        getBithumbOrderbook();
        getKorbitOrderbook();


    }

    private void drawPriceView() {
        class Order {
            String marketName;
            long price;


            public Order(String marketName, long price) {
                this.marketName = marketName;
                this.price = price;
            }
        }

        List<Order> mins = new ArrayList<Order>();
        List<Order> maxs = new ArrayList<Order>();
        if (minPriceInKorbit > 0) {
            mins.add(new Order(KORBIT, minPriceInKorbit));
        }

        if (minPriceInCoinone > 0) {
            mins.add(new Order(COINONE, minPriceInCoinone));
        }

        if (minPriceInBithumb > 0) {
            mins.add(new Order(BITHUMB, minPriceInBithumb));
        }

        if (maxPriceInKorbit > 0) {
            maxs.add(new Order(KORBIT, maxPriceInKorbit));
        }

        if (maxPriceInCoinone > 0) {
            maxs.add(new Order(COINONE, maxPriceInCoinone));
        }

        if (maxPriceInBithumb > 0) {
            maxs.add(new Order(BITHUMB, maxPriceInBithumb));
        }

        long max = -1;
        long min = -1;
        String maxMarket = null;
        String minMarket = null;

        Collections.sort(mins, new Comparator<Order>() {
            @Override
            public int compare(Order t1, Order t2) {

                return ((Long) t1.price).compareTo(t2.price);
            }
        });


        Collections.sort(maxs, new Comparator<Order>() {
            @Override
            public int compare(Order t1, Order t2) {
                return ((Long) t2.price).compareTo(t1.price);
            }
        });

        if (maxs.size() > 0) {
            long avg = 0;
            for (Order o : maxs) {
                avg += o.price;
            }
            max = maxs.get(0).price;
            maxMarket = maxs.get(0).marketName;
            String fullMsg = String.format(getString(R.string.main_summary_buy), maxMarket, String.format("%,d", max));
            final SpannableStringBuilder sp = new SpannableStringBuilder(fullMsg);
            int start = 11;
            int end = fullMsg.indexOf("(");

            sp.setSpan(
                    new ForegroundColorSpan(getResources().getColor(R.color.red_background)), 11
                    , end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mTextMaxBuy.setText(sp);
            mTextAvgBuy.setText(String.format(getString(R.string.main_summary_avg_buy), String.format("%,d", avg / maxs.size())));


        }
        if (mins.size() > 0) {
            long avg = 0;
            for (Order o : mins) {
                avg += o.price;
            }
            min = mins.get(0).price;
            minMarket = mins.get(0).marketName;
            String fullMsg = String.format(getString(R.string.main_summary_sell), minMarket, String.format("%,d", min));
            final SpannableStringBuilder sp = new SpannableStringBuilder(fullMsg);
            int start = 11;
            int end = fullMsg.indexOf("(");

            sp.setSpan(
                    new ForegroundColorSpan(getResources().getColor(R.color.blue_background)), 11
                    , end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mTextMinSell.setText(sp);

            mTextAvgSell.setText(String.format(getString(R.string.main_summary_avg_sell), String.format("%,d", avg / mins.size())));
        }


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
                setFirst();
            }
        }
    }


}
