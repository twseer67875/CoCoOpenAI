package com.cocobeen.Discord.Listener;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandAutoCompleteInteractionListener extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String command = event.getName();
        String args = event.getFocusedOption().getName();

        switch (command){
            case "chatgpt":{
                if (args.equals("模型")){
                    String[] gpt_model_options = new String[]{"gpt-3.5-turbo", "gpt-4"};
                    List<Command.Choice> options = Stream.of(gpt_model_options)
                            .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                            .map(word -> new Command.Choice(word, word))
                            .collect(Collectors.toList());
                    event.replyChoices(options).queue();
                }
                break;
            }
            case "dalle": {
                if (args.equals("圖片解析度")){
                    String[] dalle_size = new String[]{"256x256", "512x512", "1024x1024"};
                    List<Command.Choice> options = Stream.of(dalle_size)
                            .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                            .map(word -> new Command.Choice(word, word))
                            .collect(Collectors.toList());
                    event.replyChoices(options).queue();
                }
            }
        }
    }
}
