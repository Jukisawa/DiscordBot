package com.jukisawa.discordBot.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.GuildMusicManager;
import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.util.TTSUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import marytts.exceptions.SynthesisException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class TTSAnnouncer {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final JDA jda;
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(TTSAnnouncer.class);
    private final TTSUtil ttsUtil = new TTSUtil();
    TTSPhraseService ttsPhraseService = new TTSPhraseService();

    public TTSAnnouncer(JDA jda) {
        this.jda = jda;
    }

    public void start() {
        scheduler.schedule(this::announceLoop, getRandomDelay(), TimeUnit.SECONDS);
    }

    private void announceLoop() {
        try {
            List<Guild> guilds = jda.getGuilds();
            for (Guild guild : guilds) {
                List<VoiceChannel> voiceChannels = guild.getVoiceChannels();

                for (VoiceChannel vc : voiceChannels) {
                    List<Member> members = vc.getMembers();
                    if (members.isEmpty()) {
                        continue;
                    }
                    for (Member member : members) {
                        if (member.getUser().isBot()) {
                            continue;
                        }
                        List<Activity> activities = member.getActivities();
                        for (Activity activity : activities) {
                            logger.info(activity.getType().name() + ": " + activity.getName());
                            if (activity.getType() == Activity.ActivityType.PLAYING) {
                                String gameName = activity.getName();
                                if (gameName != null && !gameName.isEmpty()) {
                                    if (!ttsPhraseService.hasPhrasesForGame(gameName)) {
                                        continue;
                                    }
                                    announceInChannel(guild, vc, ttsPhraseService.getRandomPhraseForGame(gameName));
                                    return; // Only announce once
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error("Error during TTS announcement loop", e);
        } finally {
            scheduler.schedule(this::announceLoop, getRandomDelay(), TimeUnit.SECONDS);
        }
    }

    private void announceInChannel(Guild guild, VoiceChannel voiceChannel, String message) throws Exception {
        GuildMusicManager musicManager = playerManager.getMusicManager(guild);
        File ttsFile;
        try {
            ttsFile = ttsUtil.generateTTS(message);
        } catch (IOException e) {
            logger.error("Failed to generate TTS for message: " + message, e);
            return;
        } catch (SynthesisException e) {
            logger.error("TTS synthesis error for message: " + message, e);
            return;
        }

        guild.getAudioManager().openAudioConnection(voiceChannel);
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        playerManager.getPlayerManager().loadItemOrdered(musicManager, ttsFile.getAbsolutePath(),
                new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        musicManager.scheduler.queue(track);
                        // Close connection after short delay
                        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                            guild.getAudioManager().closeAudioConnection();
                        }, track.getDuration() + 1000, TimeUnit.MILLISECONDS);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                    }

                    @Override
                    public void noMatches() {
                    }

                    @Override
                    public void loadFailed(FriendlyException exception) {
                        System.err.println("TTS playback failed: " + exception.getMessage());
                    }
                });
    }

    private int getRandomDelay() {
        return random.nextInt(20);
    }
}
