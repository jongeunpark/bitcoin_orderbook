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
public class KrakenLastApiImpl implements LastApi{

    //XBTEUR XXBTZEUR
    //DASHEUR DASHEUR
    //LTCEUR XLTCZEUR
    //
    //ETHEUR XETHZEUR
    //ETCEUR XETCZEUR

    private static final String BASE_URL = "https://api.kraken.com/";
    private static final String LAST_URL = "0/public/Ticker";

    private static Context mContext;
    private static KrakenLastApiImpl mLastApi;

    private KrakenLastApiImpl() {

    }
    public static KrakenLastApiImpl getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (mLastApi == null) {
            mLastApi = new KrakenLastApiImpl();
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

                    JSONObject result = response.getJSONObject("result");
                    String input = "";
                    if(currency.equals("XBTEUR")){
                        input = "XXBTZEUR";
                    }else  if(currency.equals("DASHEUR")){
                        input = "DASHEUR";
                    }else  if(currency.equals("LTCEUR")){
                        input = "XLTCZEUR";
                    }else  if(currency.equals("ETHEUR")){
                        input = "XETHZEUR";
                    }else  if(currency.equals("ETCEUR")){
                        input = "XETCZEUR";
                    }
                    JSONObject last = result.getJSONObject(input);

                    lastPrice.setPrice(new BigDecimal(last.getJSONArray("c").get(0).toString()));
                    lastPrice.setCurrency(LastPrice.CURRENCY_EUR);
                    lastPrice.setCode(LastPrice.CODE_KRAKEN);
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
        HttpSender.sendGetRequest(BASE_URL+""+LAST_URL+"?pair="+currency,jsonHttpResponseHandler);
    }
}
