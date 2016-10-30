package jp.com.lib.orderbook.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

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
