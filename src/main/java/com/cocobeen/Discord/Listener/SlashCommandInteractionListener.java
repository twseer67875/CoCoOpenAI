package com.cocobeen.Discord.Listener;

import com.cocobeen.Main;
import com.cocobeen.OpenAI.ChatGPT;
import com.cocobeen.OpenAI.DallE;
import com.cocobeen.Utils.ChatGPTJSONObjectAnalyze;
import com.cocobeen.Utils.DallEJSONObjectAnalyze;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

public class SlashCommandInteractionListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        String command = event.getName();

        switch (command){
            case "chatgpt":{
                runChatGPT(event);
                break;
            }
            case "dalle": {
                runDallE(event);
                break;
            }
        }
    }

    private void runChatGPT(SlashCommandInteractionEvent event){
        SlashCommandInteraction interaction = event.getInteraction();
        String model = "gpt-3.5-turbo";
        String content = interaction.getOption("問題").getAsString();

        boolean show_content = true;

        for (OptionMapping optionMapping : interaction.getOptions()){
            if (optionMapping.getName().equals("公共頻道顯示")){
                show_content = optionMapping.getAsBoolean();
            }
            if (optionMapping.getName().equals("模型")){
                model = interaction.getOption("模型").getAsString();
            }
        }

        event.deferReply().queue();
        String userID = event.getUser().getId();

        ChatGPTJSONObjectAnalyze objectAnalyze = null;

        if (Main.ChatGPTMessageLog.containsKey(userID)){
            String log = Main.ChatGPTMessageLog.get(userID);
            objectAnalyze = new ChatGPTJSONObjectAnalyze(ChatGPT.getChatGPTMessage(model, content, log));
        }
        else {
            objectAnalyze = new ChatGPTJSONObjectAnalyze(ChatGPT.getChatGPTMessage(model, content));
        }

        if (objectAnalyze.getJSONObject() == null) {
            event.getHook().sendMessage("ChatGPT 請求失敗，可能是參數有誤或是程式錯誤，如問題持續請聯絡開發者").queue();
            return;
        }

        String result = objectAnalyze.getContent();
        Main.ChatGPTMessageLog.put(userID, result.replaceAll("\n", "\\n"));

        result = "原始問題: `" + content + "`\n\n" + result;

        if (show_content){
            event.getHook().sendMessage(result).queue();
            return;
        }

        User user = event.getUser();
        if (!user.isBot()){
            event.getHook().sendMessage("您的問題已被送至私人訊息內，如您沒有看到機器人的回覆請開啟 **允許陌生訊息** 並再次嘗試").queue();
            String finalResult = result;
            user.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage(finalResult))
                    .queue();
        }
    }

    private void runDallE(SlashCommandInteractionEvent event){
        SlashCommandInteraction interaction = event.getInteraction();
        String img_size = interaction.getOption("圖片解析度").getAsString();
        int img_amount = interaction.getOption("生成圖片數量").getAsInt();
        String description = interaction.getOption("提示詞").getAsString();

        event.deferReply().queue();

        if (img_amount > 4){
            event.getHook().sendMessage("無法生成超過 4 張以上的圖片").queue();
            return;
        }

        DallEJSONObjectAnalyze objectAnalyze = new DallEJSONObjectAnalyze(DallE.getDallEMessage(description, img_amount, img_size));

        if (objectAnalyze.getJSONObject() == null){
            event.getHook().sendMessage("DALL·E 請求失敗，可能是參數有誤或是程式錯誤，如問題持續請聯絡開發者").queue();
            return;
        }

        String result = "使用提示詞: `" + description + "`\n\n生成結果:\n";

        Collection<FileUpload> fileUploads = new ArrayList<>();

        int amount = 0;

        for (String base64 : objectAnalyze.getImgB64()){
            byte[] decoder = Base64.getDecoder().decode(base64);
            FileUpload upload = FileUpload.fromData(decoder, "img" + amount + ".jpg");
            fileUploads.add(upload);
            amount++;
        }

        event.getHook().sendMessage(result).addFiles(fileUploads).queue();
    }
}
