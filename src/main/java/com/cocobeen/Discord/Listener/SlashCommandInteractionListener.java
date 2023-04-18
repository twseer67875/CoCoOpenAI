package com.cocobeen.Discord.Listener;

import com.cocobeen.Discord.DiscordJDA;
import com.cocobeen.Main;
import com.cocobeen.OpenAI.ChatGPT;
import com.cocobeen.OpenAI.DallE;
import com.cocobeen.Utils.ChatGPTJSONObjectAnalyze;
import com.cocobeen.Utils.DallEJSONObjectAnalyze;
import com.cocobeen.Utils.IPGeolocationAnalyze;
import com.cocobeen.ipgeolocation.IPGeolocationLookup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlashCommandInteractionListener extends ListenerAdapter {
    private final String regex = "^([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.([01]?\\d?\\d|2[0-4]\\d|25[0-5])\\.([01]?\\d?\\d|2[0-4]\\d|25[0-5])$";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        String command = event.getName();
        SlashCommandInteraction interaction = event.getInteraction();

        switch (command){
            case "chatgpt":{
                runChatGPT(event, interaction);
                break;
            }
            case "dalle": {
                runDallE(event, interaction);
                break;
            }
            case "ipcheck": {
                runIPGeolocationLookup(event, interaction);
                break;
            }
        }
    }

    private void runChatGPT(SlashCommandInteractionEvent event, SlashCommandInteraction interaction){
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

        boolean finalShow_content = show_content;
        String finalModel = model;
        Runnable task = () -> {
            ChatGPTJSONObjectAnalyze objectAnalyze = null;
            if (Main.ChatGPTMessageLog.containsKey(userID)){
                String log = Main.ChatGPTMessageLog.get(userID);
                objectAnalyze = new ChatGPTJSONObjectAnalyze(ChatGPT.getChatGPTMessage(finalModel, content, log));
            }
            else {
                objectAnalyze = new ChatGPTJSONObjectAnalyze(ChatGPT.getChatGPTMessage(finalModel, content));
            }

            if (objectAnalyze.getJSON() == null) {
                event.getHook().sendMessage("ChatGPT 請求失敗，可能是參數有誤或是程式錯誤，如問題持續請聯絡開發者").queue();
                return;
            }

            String result = objectAnalyze.getContent();
            Main.ChatGPTMessageLog.put(userID, result.replaceAll("\n", "\\n"));

            result = "原始問題: `" + content + "`\n\n" + result;

            if (finalShow_content){
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
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    private void runDallE(SlashCommandInteractionEvent event, SlashCommandInteraction interaction){
        String userID = event.getUser().getId();
        if (!Main.Whitelist.contains(userID)){
            event.reply("您沒有權限使用此功能").queue();
            return;
        }

        String img_size = interaction.getOption("圖片解析度").getAsString();
        int img_amount = interaction.getOption("生成圖片數量").getAsInt();
        String description = interaction.getOption("提示詞").getAsString();

        event.deferReply().queue();

        if (img_amount > 4){
            event.getHook().sendMessage("無法生成超過 4 張以上的圖片").queue();
            return;
        }

        Runnable task = () -> {
            DallEJSONObjectAnalyze objectAnalyze = new DallEJSONObjectAnalyze(DallE.getDallEMessage(description, img_amount, img_size));

            if (objectAnalyze.getJSON() == null){
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
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    private void runIPGeolocationLookup(SlashCommandInteractionEvent event, SlashCommandInteraction interaction){
        String ip = interaction.getOption("ip位置").getAsString();
        event.deferReply().queue();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);

        if (!matcher.matches()){
            event.getHook().sendMessage("這個不是一個正確的 IP 位置，請檢察該 IP 位置是否正確").queue();
            return;
        }

        Runnable task = () -> {
            IPGeolocationAnalyze ipInfo =
                    new IPGeolocationAnalyze(IPGeolocationLookup.getIPGeolocationData(Main.IPGeolocationApiKey, ip));

            EmbedBuilder embedBuilder = new EmbedBuilder();

            String description
                    = "IP所屬區域: `" + ipInfo.getContinentName() + "/" + ipInfo.getCountryCode2() + "`"
                    + "\nIP所屬國家與地區: `" + ipInfo.getCountryName() + "/" + ipInfo.getCountryCapital() + "`"
                    + "\n區域網際網路註冊機構: `" + ipInfo.getISP() + "`"
                    + "\n所屬的ISP或公司: `" + ipInfo.getOrganization() + "`";

            MessageEmbed messageEmbed = embedBuilder
                    .setColor(Color.ORANGE)
                    .setTitle("IP位置 " + ip)
                    .setThumbnail(ipInfo.getCountryFlagUrl())
                    .setDescription(description)
                    .setFooter("IP 位置資訊查詢", DiscordJDA.jda.getSelfUser().getAvatarUrl())
                    .setTimestamp(OffsetDateTime.now())
                    .build();

            event.getHook().sendMessageEmbeds(messageEmbed).queue();

        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
