package com.cocobeen;

import com.cocobeen.Discord.DiscordJDA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static String Discord_Bot_Token = "DiscordBotToken";
    public static String Discord_Bot_Activity = "石頭萬事通，你最好的日本助理";
    public static String OpenAI_Key = "OpenAPIKey";
    public static String IPGeolocationApiKey = "IPGeolocationApiKey";
    public static HashMap<String, String> ChatGPTMessageLog = new HashMap<>();
    public static Set<String> Whitelist = new HashSet<>();


    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        DiscordJDA.initDiscordBot();

    }
}