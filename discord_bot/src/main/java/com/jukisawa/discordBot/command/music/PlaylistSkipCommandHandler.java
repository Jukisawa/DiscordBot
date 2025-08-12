package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class PlaylistSkipCommandHandler implements CommandHandler {
    
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistSkipCommandHandler.class);

    @Override
    public void handle(CommandEvent event, String arguments) {
        logger.info("Handling playlist skip command");
        playerManager.skip((TextChannel) event.channel());
    }
}
