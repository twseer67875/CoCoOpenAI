package com.cocobeen.OpenAI;

import com.cocobeen.Main;
import okhttp3.*;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ChatGPT {
    private static String OpenAI_Chat_Url = "https://api.openai.com/v1/chat/completions";

    public static JSONObject getChatGPTMessage(String model, String content){
        String post = "{\"model\": \"" + model + "\",\"messages\": [{\"role\": \"user\", \"content\": \"" + content + "\"}]}";
        return postChatGPTRequest(post);
    }

    public static JSONObject getChatGPTMessage(String model, String content, String reply_before){
        String post =
                "{\"model\": \"" + model + "\",\"messages\": [{\"role\": \"assistant\", \"content\": \" "+ reply_before +" \"},{\"role\": \"user\", \"content\": \"" + content + "\"}]}";

        return postChatGPTRequest(post);
    }

    private static JSONObject postChatGPTRequest(String post){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), post);

        Request request = new Request.Builder()
                .url(OpenAI_Chat_Url)
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
