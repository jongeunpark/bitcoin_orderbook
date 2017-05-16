package jp.com.lib.orderbook.network.services.etc;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import cz.msebera.android.httpclient.Header;
import jp.com.lib.orderbook.network.HttpSender;
import jp.com.lib.orderbook.network.datas.LastPrice;
import jp.com.lib.orderbook.network.datas.Orderbook;
import jp.com.lib.orderbook.network.datas.Orderbooks;
import jp.com.lib.orderbook.network.listeners.LastPriceListener;
import jp.com.lib.orderbook.network.services.Constants;

/**
 * Created by jp on 2017. 5. 15..
 */
public class PoloniexLastApiImpl implements LastApi{

    //USDT_BTC
    //USDT_DASH
    //USDT_LTC
    //USDT_XRP
    //USDT_ETH
    //USDT_ETC

    private static final String BASE_URL = "https://poloniex.com/";
    private static final String LAST_URL = "public?command=returnTicker";

    private static Context mContext;
    private static PoloniexLastApiImpl mLastApi;

    private PoloniexLastApiImpl() {

    }
    public static PoloniexLastApiImpl getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (mLastApi == null) {
            mLastApi = new PoloniexLastApiImpl();
        }
        return mLastApi;
    }

    @Override
    public void getListPrice(final String currency, final LastPriceListener listener) throws Exception {
        final JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                try {
                    LastPrice lastPrice = new LastPrice();

                    JSONObject last = response.getJSONObject(currency);
                    lastPrice.setPrice(new BigDecimal(last.getString("last")));
                    lastPrice.setCurrency(LastPrice.CURRENCY_USD);
                    lastPrice.setCode(LastPrice.CODE_POLONIEX);
                    listener.onSuccess(lastPrice);

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
        HttpSender.sendGetRequest(BASE_URL+""+LAST_URL,jsonHttpResponseHandler);
    }
}
