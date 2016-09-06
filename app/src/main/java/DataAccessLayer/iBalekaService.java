package DataAccessLayer;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by Okuhle on 8/30/2016.
 */
public class iBalekaService {

    private static final AsyncHttpClient httpClient = new AsyncHttpClient();

    //Post JSON to the server
    public static void post (Context context, String url, HttpEntity entity, Header[] headers, String contentType, AsyncHttpResponseHandler handler) {
        httpClient.addHeader("Content-Type", "application/json");
        httpClient.post(context, url, headers, entity, contentType, handler);
    }

    //Get information from the server
    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler handler) {
        httpClient.get(context, url, params, handler);
    }

}
