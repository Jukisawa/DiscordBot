package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

public class PlaylistClearCommandHandler implements CommandHandler {

    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistClearCommandHandler.class);

    @Override
    public void handle(CommandEvent event, String arguments) {
        logger.info("Handling playlist clear command");
        playlistManager.clearPlaylist(event.guildId());
        event.channel().sendMessage("Playlist cleared.").queue();
    }
}
