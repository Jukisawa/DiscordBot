package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlaylistSkipCommandHandler implements CommandHandler {
    
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistSkipCommandHandler.class);

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        logger.info("Handling playlist skip command");
        playerManager.skip((TextChannel) event.getChannel());
    }
}
