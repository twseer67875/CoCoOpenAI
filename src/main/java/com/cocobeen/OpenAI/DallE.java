package com.cocobeen.OpenAI;

import com.cocobeen.Main;
import okhttp3.*;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class DallE {

    private static String OpenAI_Img_Url = "https://api.openai.com/v1/images/generations";

    public static JSONObject getDallEMessage(String description, int images_amount, String size){
        String post = "{\"prompt\": \"" + description + "\", \"n\": " + images_amount + ", \"size\": \"" + size +"\", \"response_format\": \"b64_json\"}";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), post);

        Request request = new Request.Builder()
                .url(OpenAI_Img_Url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + Main.OpenAI_Key)
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
