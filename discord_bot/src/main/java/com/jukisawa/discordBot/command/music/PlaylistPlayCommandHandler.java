package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

/**
 * Handler for playing the playlist - matches LoL command pattern
 */
public class PlaylistPlayCommandHandler implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistPlayCommandHandler.class);
    
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();

    @Override
    public void handle(CommandEvent event, String arguments) {
        if (playlistManager.getPlaylist(event.guild().getIdLong()).isEmpty()) {
            event.channel().sendMessage("❌ Die Playlist ist leer.").queue();
            return;
        }

        if (event.member() == null) {
            event.channel().sendMessage("❌ This command can only be used in a server.").queue();
            return;
        }

        AudioChannelUnion voiceChannel = event.member().getVoiceState() != null ? 
            event.member().getVoiceState().getChannel() : null;

        if (voiceChannel == null) {
            event.channel().sendMessage("❌ Du musst in einem Voice-Channel sein, um Musik zu spielen.").queue();
            return;
        }

        try {
            event.guild().getAudioManager().openAudioConnection(voiceChannel);
            playerManager.loadNextFromPlaylist((TextChannel) event.channel());
            event.channel().sendMessage("✅ 🎵 Starte Wiedergabe der Playlist!").queue();
            logger.info("Started playlist playback in guild: {}", event.guild().getId());
        } catch (Exception e) {
            logger.error("Error starting playlist playback: ", e);
            event.channel().sendMessage("❌ Fehler beim Starten der Playlist.").queue();
        }
    }
}
