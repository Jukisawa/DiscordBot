package com.jukisawa.discordBot.audio;

import com.jukisawa.discordBot.dto.music.Song;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.track.*;
import com.sedmelluq.discord.lavaplayer.player.event.*;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private final long guildId;
    private final AudioPlayerManager playerManager;

    public TrackScheduler(AudioPlayer player, long guildId, AudioPlayerManager manager) {
        this.player = player;
        this.guildId = guildId;
        this.playerManager = manager;
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            player.getPlayingTrack();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            Song nextSong = playlistManager.nextSong(guildId);

            if (nextSong != null) {
                AudioTrack nextTrack = loadAudioTrackFromSong(nextSong);
                player.startTrack(nextTrack, false);
            } else if (playlistManager.isRepeatQueue(guildId)) {
                playlistManager.resetQueueFromOriginal(guildId);
                Song repeatSong = playlistManager.nextSong(guildId);
                if (repeatSong != null) {
                    AudioTrack repeatTrack = loadAudioTrackFromSong(repeatSong);
                    player.startTrack(repeatTrack, false);
                }
            }
        }
    }

    public AudioTrack loadAudioTrackFromSong(Song song) {
        BlockingAudioLoadResultHandler resultHandler = new BlockingAudioLoadResultHandler();

        playerManager.loadItem(song.url(), resultHandler);
        try {
            AudioTrack track = resultHandler.getTrack();
            track.setUserData(track.getInfo().title);
            return track;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
