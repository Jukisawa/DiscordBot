package com.jukisawa.discordBot.audio;

import java.util.concurrent.CountDownLatch;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class BlockingAudioLoadResultHandler implements AudioLoadResultHandler {

    private final CountDownLatch latch = new CountDownLatch(1);
    private AudioTrack track;
    private Exception exception;

    @Override
    public void trackLoaded(AudioTrack track) {
        this.track = track;
        latch.countDown();
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        this.track = playlist.getTracks().get(0);
        latch.countDown();
    }

    @Override
    public void noMatches() {
        latch.countDown();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        this.exception = exception;
        latch.countDown();
    }

    public AudioTrack getTrack() throws Exception {
        latch.await();

        if (exception != null)
            throw exception;
        return track;
    }
}
