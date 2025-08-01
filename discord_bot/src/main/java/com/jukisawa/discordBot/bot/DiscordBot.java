package com.jukisawa.discordBot.bot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBot extends ListenerAdapter {

    public static void main(String[] args) {
        // Use the new event-driven BotApplication
        BotApplication app = new BotApplication();
        
        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(app::shutdown));
        
        app.start();
    }
}