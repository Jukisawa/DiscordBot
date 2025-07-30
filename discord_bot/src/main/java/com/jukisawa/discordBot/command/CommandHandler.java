package com.jukisawa.discordBot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandHandler {
    void handle(MessageReceivedEvent event, String arguments);
}
