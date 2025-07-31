package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Handler for playing the playlist - matches LoL command pattern
 */
public class PlaylistPlayCommandHandler implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistPlayCommandHandler.class);
    
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        if (playlistManager.getPlaylist(event.getGuild().getIdLong()).isEmpty()) {
            event.getChannel().sendMessage("❌ Die Playlist ist leer.").queue();
            return;
        }

        if (event.getMember() == null) {
            event.getChannel().sendMessage("❌ This command can only be used in a server.").queue();
            return;
        }

        AudioChannelUnion voiceChannel = event.getMember().getVoiceState() != null ? 
            event.getMember().getVoiceState().getChannel() : null;

        if (voiceChannel == null) {
            event.getChannel().sendMessage("❌ Du musst in einem Voice-Channel sein, um Musik zu spielen.").queue();
            return;
        }

        try {
            event.getGuild().getAudioManager().openAudioConnection(voiceChannel);
            playerManager.loadNextFromPlaylist((TextChannel) event.getChannel());
            event.getChannel().sendMessage("✅ 🎵 Starte Wiedergabe der Playlist!").queue();
            logger.info("Started playlist playback in guild: {}", event.getGuild().getId());
        } catch (Exception e) {
            logger.error("Error starting playlist playback: ", e);
            event.getChannel().sendMessage("❌ Fehler beim Starten der Playlist.").queue();
        }
    }
}
