package com.jukisawa.discordBot.command.music;

import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.music.Song;
import com.jukisawa.discordBot.event.CommandEvent;

/**
 * Handler for listing playlist contents - matches LoL command pattern
 */
public class PlaylistListCommandHandler implements CommandHandler {
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();

    @Override
    public void handle(CommandEvent event, String arguments) {
        var playlist = playlistManager.getPlaylist(event.guild().getIdLong());
        
        if (playlist.isEmpty()) {
            event.channel().sendMessage("📝 Die Playlist ist leer.").queue();
            return;
        }
        
        StringBuilder sb = new StringBuilder("📝 **Aktuelle Playlist:**\n");
        int index = 1;
        for (Song song : playlist) {
            sb.append(String.format("%d. %s (von %s)\n", index++, song.title(), song.requestedBy()));
        }
        
        event.channel().sendMessage(sb.toString()).queue();
    }
    
}
