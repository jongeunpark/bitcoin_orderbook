package jp.com.lib.orderbook.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by jp on 16. 10. 12..
 */
public class HttpSender {
   private static final int TIMEOUT = 5000;


   public static void sendGetRequest(String url, JsonHttpResponseHandler jsonHttpResponseHandler){
       AsyncHttpClient client = new AsyncHttpClient();
       client.setTimeout(TIMEOUT);
       client.addHeader("Content-Type", "application/json");
       client.get(url, jsonHttpResponseHandler);
   }
}
