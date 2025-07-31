package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.music.Song;
import com.jukisawa.discordBot.service.musik.CachingSongService;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Handler for adding songs to playlist - matches LoL command pattern
 */
public class PlaylistAddCommandHandler implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistAddCommandHandler.class);
    
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private final CachingSongService songService = new CachingSongService();

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        if (arguments.isEmpty()) {
            event.getChannel().sendMessage("❌ Bitte gib einen Songnamen oder eine URL an.").queue();
            return;
        }

        try {
            logger.info("Adding song to playlist: {}", arguments);
            
            String title, url;
            if (isUrl(arguments)) {
                url = arguments;
                title = songService.getTitleFromUrl(url);
            } else {
                url = songService.getUrlFromTitle(arguments);
                title = songService.getTitleFromUrl(url);
            }
            
            playlistManager.addSong(event.getGuild().getIdLong(), 
                new Song(title, url, event.getAuthor().getName()));
            
            logger.info("Song added to playlist: {}", title);
            event.getChannel().sendMessage("✅ Song added to playlist: " + title).queue();
            
        } catch (Exception e) {
            logger.error("Error adding song to playlist: ", e);
            event.getChannel().sendMessage("❌ Fehler beim Hinzufügen des Songs zur Playlist.").queue();
        }
    }

    private boolean isUrl(String input) {
        try {
            java.net.URI uri = java.net.URI.create(input);
            return uri.getScheme() != null
                    && (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"));
        } catch (Exception e) {
            return false;
        }
    }
}
