package jp.com.lib.orderbook.network.services.etc;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import cz.msebera.android.httpclient.Header;
import jp.com.lib.orderbook.network.HttpSender;
import jp.com.lib.orderbook.network.datas.LastPrice;
import jp.com.lib.orderbook.network.listeners.LastPriceListener;

/**
 * Created by jp on 2017. 5. 15..
 */
public class Btc38LastApiImpl implements LastApi{

    //btc
    //dash
    //ltc
    //xrp


    private static final String BASE_URL = "http://api.btc38.com/";
    private static final String LAST_URL = "v1/ticker.php";

    private static Context mContext;
    private static Btc38LastApiImpl mLastApi;

    private Btc38LastApiImpl() {

    }
    public static Btc38LastApiImpl getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (mLastApi == null) {
            mLastApi = new Btc38LastApiImpl();
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

                    JSONObject ticker = response.getJSONObject("ticker");

                    lastPrice.setPrice(new BigDecimal(ticker.getDouble("last")));
                    lastPrice.setCurrency(LastPrice.CURRENCY_CNY);
                    lastPrice.setCode(LastPrice.CODE_BTC38);
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
        HttpSender.sendGetRequest(BASE_URL+""+LAST_URL+"?c="+currency,jsonHttpResponseHandler);
    }
}
