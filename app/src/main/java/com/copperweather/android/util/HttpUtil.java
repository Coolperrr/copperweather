package com.copperweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by copper on 2017/1/15.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callcack){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callcack);
    }
}
