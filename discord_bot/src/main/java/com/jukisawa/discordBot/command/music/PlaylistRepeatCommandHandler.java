package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

public class PlaylistRepeatCommandHandler implements CommandHandler {
    
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistRepeatCommandHandler.class);

    @Override
    public void handle(CommandEvent event, String arguments) {
        logger.info("Handling playlist repeat command");
        playlistManager.switchRepeatQueue(event.guild().getIdLong());
        event.channel().sendMessage("Repeat mode ist nun "
                + (playlistManager.isRepeatQueue(event.guild().getIdLong()) ? "Aktiv" : "Deaktiv"))
                .queue();
    }
    
}
