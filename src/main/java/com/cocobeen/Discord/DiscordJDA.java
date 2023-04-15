package com.cocobeen.Discord;

import com.cocobeen.Discord.Listener.CommandAutoCompleteInteractionListener;
import com.cocobeen.Discord.Listener.SlashCommandInteractionListener;
import com.cocobeen.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordJDA {
    public static JDA jda = null;

    public static void initDiscordBot(){
        JDABuilder jdaBuilder = JDABuilder.createDefault(Main.Discord_Bot_Token);

        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        jdaBuilder.setActivity(Activity.playing(Main.Discord_Bot_Activity));
        jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.enableIntents(GatewayIntent.DIRECT_MESSAGES);

        jda = jdaBuilder
                .addEventListeners(new SlashCommandInteractionListener())
                .addEventListeners(new CommandAutoCompleteInteractionListener())
                .build();

        initBotSlashCommand();
    }

    private static void initBotSlashCommand(){
        jda.updateCommands().addCommands(
                Commands
                        .slash("chatgpt", "使用此指令來指定 ChatGPT 模型並發問問題")
                        .addOption(OptionType.STRING, "模型", "您要使用的 ChatGPT 模型", true, true)
                        .addOption(OptionType.STRING, "問題", "在此輸入您想要詢問 ChatGPT 的問題", true)
                        .addOption(OptionType.BOOLEAN, "公共頻道顯示", "是否於公共頻道內顯示您詢問的問題 (默認為 是)", false, false),
                Commands
                        .slash("dalle", "使用此指令來透過 DALL·E 並輸入提示詞來生成AI圖片")
                        .addOption(OptionType.STRING, "圖片解析度", "您要生成的圖片解析度", true, true)
                        .addOption(OptionType.INTEGER, "生成圖片數量", "您要生成的圖片數量 (最多 4 張)", true)
                        .addOption(OptionType.STRING, "提示詞", "您要生成的圖片正面提示詞 (最多 1000 個字符)", true)
        ).queue();
    }
}
