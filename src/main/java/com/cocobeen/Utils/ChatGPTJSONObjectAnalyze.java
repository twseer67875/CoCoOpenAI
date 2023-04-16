package com.cocobeen.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPTJSONObjectAnalyze {
    private JSONObject jsonObj = null;
    private String id = "";
    private String object = "";
    private int created = 0;
    private String model = "";
    private int prompt_tokens = 0;
    private int completion_tokens = 0;
    private int total_tokens = 0;
    private String role = "";
    private String content = "";
    private String finish_reason = "";
    private int index = 0;

    public ChatGPTJSONObjectAnalyze(JSONObject jsonObject){
        if (jsonObject.has("error")){
            return;
        }

        jsonObj = jsonObject;

        id = jsonObject.getString("id");
        object = jsonObject.getString("object");
        created = jsonObject.getInt("created");
        model = jsonObject.getString("model");

        JSONObject usage = jsonObject.getJSONObject("usage");
        prompt_tokens = usage.getInt("prompt_tokens");
        completion_tokens = usage.getInt("completion_tokens");
        total_tokens = usage.getInt("total_tokens");

        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject choicesJSONObject = choices.getJSONObject(0);

        JSONObject message = choicesJSONObject.getJSONObject("message");

        role = message.getString("role");
        content = message.getString("content");

        finish_reason = choicesJSONObject.getString("finish_reason");
        index = choicesJSONObject.getInt("index");
    }

    public JSONObject getJSONObject(){
        return jsonObj;
    }

    public String getID(){
        return id;
    }

    public String getObject(){
        return object;
    }

    public int getCreated(){
        return created;
    }

    public String getModel(){
        return model;
    }

    public int getPromptTokens(){
        return prompt_tokens;
    }

    public int getCompletionTokens(){
        return completion_tokens;
    }

    public int getTotalTokens(){
        return total_tokens;
    }

    public String getRole(){
        return role;
    }

    public String getContent(){
        return content;
    }

    public String getFinishReason(){
        return finish_reason;
    }

    public int getIndex(){
        return index;
    }
}
