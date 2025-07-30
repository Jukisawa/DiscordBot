package com.jukisawa.discordBot.audio;

import java.util.HashMap;
import java.util.Map;

import com.jukisawa.discordBot.dto.music.Song;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;

import lombok.Getter;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class PlayerManager {

    private static final PlayerManager INSTANCE = new PlayerManager();

    @Getter
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    private PlayerManager() {
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static PlayerManager getInstance() {
        return INSTANCE;
    }

    public synchronized GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(playerManager, guildId);
            guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
            return musicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        Guild guild = channel.getGuild();
        GuildMusicManager musicManager = getMusicManager(guild);

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                channel.sendMessage("🎶 Now playing: **" + track.getInfo().title + "**").queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack() != null
                        ? playlist.getSelectedTrack()
                        : playlist.getTracks().get(0);

                musicManager.scheduler.queue(firstTrack);
                channel.sendMessage("🎶 Playlist track: **" + firstTrack.getInfo().title + "**").queue();
            }

            @Override
            public void noMatches() {
                channel.sendMessage("❌ No matches found for: " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("⚠️ Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public void loadNextFromPlaylist(TextChannel channel) {
        Guild guild = channel.getGuild();
        PlaylistManager playlistManager = PlaylistManager.getInstance();

        Song nextSong = playlistManager.nextSong(guild.getIdLong());
        if (nextSong == null) {
            channel.sendMessage("📭 Playlist is empty.").queue();
            return;
        }

        String url = nextSong.getUrl();
        loadAndPlay(channel, url);
    }

    public void stop(TextChannel channel) {
        Guild guild = channel.getGuild();
        GuildMusicManager musicManager = getMusicManager(guild);
        musicManager.player.stopTrack();
        channel.sendMessage("⏹️ Playback stopped.").queue();
    }

    public void skip(TextChannel channel) {
        Guild guild = channel.getGuild();
        GuildMusicManager musicManager = getMusicManager(guild);
        AudioTrack currentTrack = musicManager.player.getPlayingTrack();

        if (currentTrack != null) {
            musicManager.player.stopTrack();
            channel.sendMessage("⏭️ Skipped: **" + currentTrack.getInfo().title + "**").queue();
        } else {
            channel.sendMessage("❌ No track is currently playing to skip.").queue();
        }
    }
}