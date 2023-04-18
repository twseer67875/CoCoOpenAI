package com.cocobeen.ipgeolocation;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class IPGeolocationLookup {

    public static JSONObject getIPGeolocationData(String apiKey, String IP){
        String url = "https://api.ipgeolocation.io/ipgeo?apiKey=" + apiKey + "&ip=" + IP;

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }
        catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
