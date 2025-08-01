package com.jukisawa.discordBot.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Event representing a Discord command that needs to be processed
 */
public record CommandEvent(String command, String arguments, MessageReceivedEvent discordEvent) {
    
    /**
     * Get the user who issued the command
     */
    public String getUserName() {
        return discordEvent.getAuthor().getName();
    }
    
    /**
     * Get the guild ID where the command was issued
     */
    public long getGuildId() {
        return discordEvent.getGuild().getIdLong();
    }
    
    /**
     * Get the user ID who issued the command
     */
    public long getUserId() {
        return discordEvent.getAuthor().getIdLong();
    }
}
