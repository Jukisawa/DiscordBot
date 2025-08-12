package com.jukisawa.discordBot.command;

import com.jukisawa.discordBot.event.CommandEvent;

public interface CommandHandler {
    void handle(CommandEvent event, String arguments);

    default String getDescription() {
        return "";
    };
}
