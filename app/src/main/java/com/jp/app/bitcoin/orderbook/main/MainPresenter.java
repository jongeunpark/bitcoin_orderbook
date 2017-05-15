package com.jp.app.bitcoin.orderbook.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jp.app.bitcoin.orderbook.orderbook.OrderbookFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.com.lib.orderbook.network.datas.Orderbooks;
import jp.com.lib.orderbook.network.listeners.OrderBooksListener;
import jp.com.lib.orderbook.network.services.exteral.BithumbPublicApiImpl;
import jp.com.lib.orderbook.network.services.exteral.CoinonePublicApiImpl;
import jp.com.lib.orderbook.network.services.exteral.KorbitPublicApiImpl;
import jp.com.lib.orderbook.network.services.exteral.PublickApi;

/**
 * Created by jp on 16. 10. 21..
 */
public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mMainView;

    private long maxPriceInCoinone = -1;
    private long maxPriceInKorbit = -1;
    private long maxPriceInBithumb = -1;

    private long minPriceInCoinone = -1;
    private long minPriceInKorbit = -1;
    private long minPriceInBithumb = -1;


    private boolean isLoading = false;

    private OrderbookFragment mCoinoneFragment, mBithumbFragment, mKorbitFragment;

    public MainPresenter(@NonNull MainContract.View mainView) {

        this.mMainView = mainView;
        mMainView.setPresenter(this);
    }


    @Override
    public boolean isFirstTime(SharedPreferences pref) {
        return pref.getBoolean("is_first", true);

    }


    @Override
    public void setFirstTime(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("is_first", false);
        editor.commit();
    }


    @Override
    public OrderbookFragment[] generateFragments() {
        mKorbitFragment = OrderbookFragment.newInstance(OrderbookFragment.MARKET_TYPE_KORBIT);
        mCoinoneFragment = OrderbookFragment.newInstance(OrderbookFragment.MARKET_TYPE_COINONE);
        mBithumbFragment = OrderbookFragment.newInstance(OrderbookFragment.MARKET_TYPE_BITHUMB);

        return new OrderbookFragment[]{mKorbitFragment, mCoinoneFragment, mBithumbFragment};
    }


    @Override
    public void getOrderbook(Context context) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoading = false;
            }
        }, 1000);
        mBithumbFragment.clearData();
        mCoinoneFragment.clearData();
        mKorbitFragment.clearData();
        mMainView.clearData();

//        getInteralCoinoneOrderbook(context);
//        getInteralBithumbOrderbook(context);
//        getInteralKorbitOrderbook(context);
        getCoinoneOrderbook(context);
        getBithumbOrderbook(context);
        getKorbitOrderbook(context);
    }

    @Override
    public void getInteralCoinoneOrderbook(final Context context) {
        PublickApi coinonePublickApi = CoinonePublicApiImpl.getInstance(context);
        try {

            coinonePublickApi.getInteralOrderbook(new OrderBooksListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {

                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {
                        mMainView.drawOrderbook(OrderbookFragment.MARKET_TYPE_COINONE, orderbooks);

                        orderbooks.sort();
                        minPriceInCoinone = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInCoinone = orderbooks.getBidArray().get(0).getPrice().longValue();
                        calSummary();

                    }
                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {
                    getCoinoneOrderbook(context);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getInteralBithumbOrderbook(final Context context) {
        PublickApi bithumbPublicAPI = BithumbPublicApiImpl.getInstance(context);
        try {
            bithumbPublicAPI.getInteralOrderbook(new OrderBooksListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {
                        mMainView.drawOrderbook(OrderbookFragment.MARKET_TYPE_BITHUMB, orderbooks);

                        orderbooks.sort();
                        minPriceInBithumb = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInBithumb = orderbooks.getBidArray().get(0).getPrice().longValue();
                        calSummary();

                    }

                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {
                    getBithumbOrderbook(context);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getInteralKorbitOrderbook(final Context context) {
        PublickApi korbitPublickApi = KorbitPublicApiImpl.getInstance(context);
        try {

            korbitPublickApi.getInteralOrderbook(new OrderBooksListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {
                        mMainView.drawOrderbook(OrderbookFragment.MARKET_TYPE_KORBIT, orderbooks);

                        orderbooks.sort();

                        minPriceInKorbit = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInKorbit = orderbooks.getBidArray().get(0).getPrice().longValue();
                        calSummary();


                    }
                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {
                    getKorbitOrderbook(context);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getCoinoneOrderbook(final Context context) {

        PublickApi coinonePublickApi = CoinonePublicApiImpl.getInstance(context);
        try {

            coinonePublickApi.getOrderbook(new OrderBooksListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {
                        mMainView.drawOrderbook(OrderbookFragment.MARKET_TYPE_COINONE, orderbooks);

                        orderbooks.sort();
                        minPriceInCoinone = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInCoinone = orderbooks.getBidArray().get(0).getPrice().longValue();
                        calSummary();

                    }
                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {
                    mMainView.drawError(OrderbookFragment.MARKET_TYPE_COINONE, httpStatusCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBithumbOrderbook(final Context context) {
        PublickApi bithumbPublicAPI = BithumbPublicApiImpl.getInstance(context);
        try {
            bithumbPublicAPI.getOrderbook(new OrderBooksListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {
                        mMainView.drawOrderbook(OrderbookFragment.MARKET_TYPE_BITHUMB, orderbooks);

                        orderbooks.sort();
                        minPriceInBithumb = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInBithumb = orderbooks.getBidArray().get(0).getPrice().longValue();
                        calSummary();

                    }

                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {
                    mMainView.drawError(OrderbookFragment.MARKET_TYPE_BITHUMB, httpStatusCode);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getKorbitOrderbook(Context context) {
        PublickApi korbitPublickApi = KorbitPublicApiImpl.getInstance(context);
        try {

            korbitPublickApi.getOrderbook(new OrderBooksListener() {
                @Override
                public void onSuccess(Orderbooks orderbooks) {
                    if (orderbooks != null && orderbooks.getBidArray().size() > 0 && orderbooks.getAskArray().size() > 0) {
                        mMainView.drawOrderbook(OrderbookFragment.MARKET_TYPE_KORBIT, orderbooks);

                        orderbooks.sort();

                        minPriceInKorbit = orderbooks.getAskArray().get(0).getPrice().longValue();
                        maxPriceInKorbit = orderbooks.getBidArray().get(0).getPrice().longValue();
                        calSummary();


                    }
                }

                @Override
                public void onFailure(int httpStatusCode, Throwable throwable) {
                    mMainView.drawError(OrderbookFragment.MARKET_TYPE_KORBIT, httpStatusCode);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void calSummary() {
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
            mins.add(new Order(MainActivity.KORBIT, minPriceInKorbit));
        }

        if (minPriceInCoinone > 0) {
            mins.add(new Order(MainActivity.COINONE, minPriceInCoinone));
        }

        if (minPriceInBithumb > 0) {
            mins.add(new Order(MainActivity.BITHUMB, minPriceInBithumb));
        }

        if (maxPriceInKorbit > 0) {
            maxs.add(new Order(MainActivity.KORBIT, maxPriceInKorbit));
        }

        if (maxPriceInCoinone > 0) {
            maxs.add(new Order(MainActivity.COINONE, maxPriceInCoinone));
        }

        if (maxPriceInBithumb > 0) {
            maxs.add(new Order(MainActivity.BITHUMB, maxPriceInBithumb));
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

            if(mMainView.isActive()) {
                mMainView.setTextMaxBuy(maxMarket, max);
                mMainView.setTextAvgBuy(avg / maxs.size());
            }


        }
        if (mins.size() > 0) {
            long avg = 0;
            for (Order o : mins) {
                avg += o.price;
            }
            min = mins.get(0).price;
            minMarket = mins.get(0).marketName;
            if(mMainView.isActive()) {
                mMainView.setTextMinSell(minMarket, min);
                mMainView.setTextAvgSell(avg / mins.size());
            }

        }
    }

    @Override
    public void start() {

    }
}
