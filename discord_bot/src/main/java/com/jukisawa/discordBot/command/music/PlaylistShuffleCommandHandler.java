package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

public class PlaylistShuffleCommandHandler implements CommandHandler {
    
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistShuffleCommandHandler.class);

    @Override
    public void handle(CommandEvent event, String arguments) {
        logger.info("Handling playlist shuffle command");
        playlistManager.shufflePlaylist(event.guild().getIdLong());
        event.channel().sendMessage("Playlist shuffled.").queue();
    }
}
