package jp.com.lib.orderbook.network.services;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import cz.msebera.android.httpclient.Header;
import jp.com.lib.orderbook.network.HttpSender;
import jp.com.lib.orderbook.network.datas.Orderbook;
import jp.com.lib.orderbook.network.datas.Orderbooks;
import jp.com.lib.orderbook.network.listeners.OrderBookArrayListener;

/**
 * Created by jp on 16. 10. 12..
 */
public class KorbitPublicApiImpl implements PublickApi {
    private static final String BASE_URL = "https://api.korbit.co.kr/v1/";
    private static final String ORDERBOOK_URL = "orderbook";

    private static Context mContext;
    private static KorbitPublicApiImpl mPublicAPI;

    private KorbitPublicApiImpl() {

    }

    public static KorbitPublicApiImpl getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (mPublicAPI == null) {
            mPublicAPI = new KorbitPublicApiImpl();
        }
        return mPublicAPI;
    }

    @Override
    public Orderbooks getOrderbook(final OrderBookArrayListener listener) throws Exception {
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                try {



                    JSONArray bids = response.getJSONArray("bids");
                    JSONArray asks = response.getJSONArray("asks");
                    Orderbooks orderbooks = new Orderbooks();
                    for (int i = 0; i < bids.length(); i++) {
                        JSONArray bid = bids.getJSONArray(i);
                        BigDecimal qty = new BigDecimal( bid.getString(1));
                        BigDecimal price = new BigDecimal(bid.getString(0));
                        Orderbook orderbook = new Orderbook();
                        orderbook.setPrice(price);
                        orderbook.setQty(qty);
                        orderbooks.addBid(orderbook);

                    }
                    for (int i = 0; i < asks.length(); i++) {
                        JSONArray ask = asks.getJSONArray(i);

                        BigDecimal qty = new BigDecimal( ask.getString(1));
                        BigDecimal price =new BigDecimal( ask.getString(0));
                        Orderbook orderbook = new Orderbook();
                        orderbook.setPrice(price);
                        orderbook.setQty(qty);
                        orderbooks.addAsk(orderbook);
                    }

                    listener.onSuccess(orderbooks);

                } catch (JSONException e) {
                    listener.onFailure(statusCode, e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject jsonObject) {
                listener.onFailure(statusCode, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                listener.onFailure(statusCode, throwable);
            }
        };
        HttpSender.sendGetRequest(BASE_URL + "" + ORDERBOOK_URL, jsonHttpResponseHandler);
        return null;
    }
}