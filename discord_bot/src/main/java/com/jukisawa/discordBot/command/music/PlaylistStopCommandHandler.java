package com.jukisawa.discordBot.command.music;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * Handler for stopping music and clearing playlist - matches LoL command pattern
 */
public class PlaylistStopCommandHandler implements CommandHandler {
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();

    @Override
    public void handle(CommandEvent event, String arguments) {
        var audioManager = event.guild().getAudioManager();
        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
        }
        playlistManager.clearPlaylist(event.guild().getIdLong());
        playerManager.stop((TextChannel) event.channel());
        event.channel().sendMessage("✅ ⏹️ Musik gestoppt und Playlist geleert.").queue();
    }
}
