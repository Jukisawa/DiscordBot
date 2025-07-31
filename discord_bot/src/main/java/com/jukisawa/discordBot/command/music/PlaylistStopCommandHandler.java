package com.jukisawa.discordBot.command.music;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Handler for stopping music and clearing playlist - matches LoL command pattern
 */
public class PlaylistStopCommandHandler implements CommandHandler {
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        var audioManager = event.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
        }
        playlistManager.clearPlaylist(event.getGuild().getIdLong());
        playerManager.stop((TextChannel) event.getChannel());
        event.getChannel().sendMessage("✅ ⏹️ Musik gestoppt und Playlist geleert.").queue();
    }
}
