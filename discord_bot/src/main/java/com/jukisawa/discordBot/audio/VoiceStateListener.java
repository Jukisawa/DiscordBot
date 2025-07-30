package com.jukisawa.discordBot.audio;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.internal.entities.channel.concrete.VoiceChannelImpl;

public class VoiceStateListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        PlaylistManager playlistManager = PlaylistManager.getInstance();

        if (!audioManager.isConnected()) return;

        VoiceChannelImpl connectedChannel = (VoiceChannelImpl) audioManager.getConnectedChannel();
        if (connectedChannel == null) return;

        // Count non-bot members in the voice channel
        long humanCount = connectedChannel.getMembers().stream()
            .filter(member -> !member.getUser().isBot())
            .count();

        // If alone (only bot left), leave
        if (humanCount == 0) {
            audioManager.closeAudioConnection();
            playlistManager.clearPlaylist(guild.getIdLong());
            PlayerManager.getInstance().getMusicManager(guild).player.stopTrack();
        }
    }
}
