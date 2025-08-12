package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.music.Song;
import com.jukisawa.discordBot.event.CommandEvent;

public class PlaylistRemoveCommandHandler implements CommandHandler {
    
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistRemoveCommandHandler.class);

    @Override
    public void handle(CommandEvent event, String arguments) {
        logger.info("Handling playlist remove command: " + arguments);
        
        if (arguments.isEmpty()) {
            event.channel().sendMessage("Bitte gib einen Songnamen an.").queue();
            return;
        }
        
        Song removedSong = playlistManager.removeSong(event.guild().getIdLong(), arguments);
        if (removedSong != null) {
            event.channel().sendMessage("Song removed from playlist: " + removedSong.title()).queue();
        } else {
            event.channel().sendMessage("Song not found in playlist: " + arguments).queue();
        }
    }
    
}
