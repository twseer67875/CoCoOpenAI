package com.cocobeen.Discord.Listener;

import com.cocobeen.OpenAI.ChatGPT;
import com.cocobeen.OpenAI.DallE;
import com.cocobeen.Utils.ChatGPTJSONObjectAnalyze;
import com.cocobeen.Utils.DallEJSONObjectAnalyze;
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
                rubChatGPT(event);
                break;
            }
            case "dalle": {
                runDallE(event);
                break;
            }
        }
    }

    private void rubChatGPT(SlashCommandInteractionEvent event){
        SlashCommandInteraction interaction = event.getInteraction();
        String model = interaction.getOption("模型").getAsString();
        String content = interaction.getOption("問題").getAsString();

        boolean show_content = true;

        for (OptionMapping optionMapping : interaction.getOptions()){
            if (optionMapping.getName().equals("公共頻道顯示")){
                show_content = optionMapping.getAsBoolean();
            }
        }

        event.deferReply().queue();

        ChatGPTJSONObjectAnalyze objectAnalyze = new ChatGPTJSONObjectAnalyze(ChatGPT.getChatGPTMessage(model, content));

        if (objectAnalyze.getJSONObject() == null){
            event.getHook().sendMessage("ChatGPT 請求失敗，可能是參數有誤或是程式錯誤，如問題持續請聯絡開發者").queue();
            return;
        }

        String result = objectAnalyze.getContent();

        if (show_content){
            result = "原始問題: `" + content + "`\n\n" + result;
        }

        event.getHook().sendMessage(result).queue();
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
