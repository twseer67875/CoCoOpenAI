package com.cocobeen.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DallEJSONObjectAnalyze {
    private JSONObject object = null;
    private List<String> img_b64 = new ArrayList<>();
    private int created = 0;

    public DallEJSONObjectAnalyze(JSONObject jsonObject){
        if (jsonObject.has("error")){
            return;
        }

        object = jsonObject;

        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; data.length() != i; i++){
            JSONObject b64 = data.getJSONObject(i);
            img_b64.add(b64.getString("b64_json"));
        }

        created = jsonObject.getInt("created");
    }

    public JSONObject getJSONObject(){
        return object;
    }

    public List<String> getImgB64(){
        return img_b64;
    }

    public int getCreated(){
        return created;
    }
}
