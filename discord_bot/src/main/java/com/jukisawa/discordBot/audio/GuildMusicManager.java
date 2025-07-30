package com.jukisawa.discordBot.audio;

import com.sedmelluq.discord.lavaplayer.player.*;
import net.dv8tion.jda.api.audio.AudioSendHandler;

public class GuildMusicManager {

    public final AudioPlayer player;
    public final TrackScheduler scheduler;

    public GuildMusicManager(AudioPlayerManager manager, long guildId) {
        this.player = manager.createPlayer();
        this.scheduler = new TrackScheduler(player, guildId, manager);
        this.player.addListener(scheduler);
        player.setVolume(30); // Set default volume
    }

    public AudioSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}