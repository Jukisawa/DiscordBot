package com.jukisawa.discordBot.command.music;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.GuildMusicManager;
import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.music.Song;
import com.jukisawa.discordBot.service.musik.CachingSongService;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.internal.entities.channel.concrete.VoiceChannelImpl;

public class PlaylistCommandHandler implements CommandHandler {

    PlaylistManager playlistManager = PlaylistManager.getInstance();
    PlayerManager playerManager = PlayerManager.getInstance();
    CachingSongService cachingSongService = new CachingSongService();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistCommandHandler.class);

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        logger.info("Handling playlist command: " + arguments);
        String commandAddon;
        int index = arguments.indexOf(" ");
        if (index == -1) {
            commandAddon = arguments;
        } else {
            commandAddon = arguments.substring(0, arguments.indexOf(" "));
        }

        Guild guild = event.getGuild();
        Member member = event.getMember();

        String value = "";
        if (commandAddon.length() != arguments.length()) {
            value = arguments.substring(commandAddon.length()).trim();
        }
        try {
            switch (commandAddon) {
                case "help":
                    event.getChannel().sendMessage(
                            "Der Playlist Command geht folgendermaßen: #playlist 'add/remove/list/play/pause/resume/stop/skip/clear/shuffle/repeat' 'Song-Name oder URL'.")
                            .queue();
                    return;
                case "add":
                    addSongToPlaylist(event, value);
                    break;
                case "remove":
                    removeSongFromPlaylist(event, value);
                    break;
                case "list":
                    String playlist = getPlaylist(guild.getIdLong());
                    event.getChannel().sendMessage(playlist).queue();
                    break;
                case "play":
                    if (playlistManager.getPlaylist(guild.getIdLong()).isEmpty()) {
                        event.getChannel().sendMessage("Die Playlist ist leer.").queue();
                    } else {
                        VoiceChannelImpl voiceChannel = (VoiceChannelImpl) member.getVoiceState().getChannel();
                        guild.getAudioManager().openAudioConnection(voiceChannel);
                        playerManager.loadNextFromPlaylist((TextChannel) event.getChannel());
                    }
                    break;
                case "pause":
                    GuildMusicManager musicManager = playerManager.getMusicManager(guild);
                    AudioPlayer player = musicManager.player;
                    player.setPaused(true);
                    break;
                case "resume":
                    musicManager = playerManager.getMusicManager(guild);
                    player = musicManager.player;
                    player.setPaused(false);
                case "stop":
                    AudioManager audioManager = guild.getAudioManager();
                    if (audioManager.isConnected()) {
                        audioManager.closeAudioConnection();
                    }
                    playlistManager.clearPlaylist(guild.getIdLong());
                    playerManager.stop((TextChannel) event.getChannel());
                    break;
                case "skip":
                    playerManager.skip((TextChannel) event.getChannel());
                    break;
                case "clear":
                    playlistManager.clearPlaylist(guild.getIdLong());
                    break;
                case "shuffle":
                    playlistManager.shufflePlaylist(guild.getIdLong());
                    break;
                case "repeat":
                    playlistManager.switchRepeatQueue(guild.getIdLong());
                    event.getChannel().sendMessage("Repeat mode ist nun "
                            + (playlistManager.isRepeatQueue(guild.getIdLong()) ? "Aktiv" : "Deaktiv"))
                            .queue();
                    break;
                case "volume":
                    if (value.isEmpty()) {
                        event.getChannel().sendMessage("Bitte gib eine Lautstärke an.").queue();
                    } else {
                        try {
                            int volume = Integer.parseInt(value);
                            if (volume < 0 || volume > 100) {
                                event.getChannel().sendMessage("Lautstärke muss zwischen 0 und 100 liegen.").queue();
                            } else {
                                GuildMusicManager musicManagerVolume = playerManager.getMusicManager(guild);
                                musicManagerVolume.player.setVolume(volume);
                                event.getChannel().sendMessage("Lautstärke auf " + volume + " gesetzt.").queue();
                            }
                        } catch (NumberFormatException e) {
                            event.getChannel().sendMessage("Ungültige Lautstärkeangabe.").queue();
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("Error handling playlist command: ", e);
        }
    }

    private String getPlaylist(long guildId) {
        StringBuilder sb = new StringBuilder();
        for (Song song : playlistManager.getPlaylist(guildId)) {
            sb.append(song.getTitle()).append(" von ").append(song.getRequestedBy()).append("\n");
        }

        if (sb.isEmpty()) {
            return "Die Playlist ist leer.";
        } else {
            return sb.toString();
        }
    }

    private void addSongToPlaylist(MessageReceivedEvent event, String song) throws Exception {
        String title, url;
        if (song.isEmpty()) {
            event.getChannel().sendMessage("Bitte gib einen Songnamen oder eine URL an.").queue();
            return;
        }
        logger.info("Adding song to playlist: " + song);
        if (isUrl(song)) {
            logger.info("Detected URL: " + song);
            url = song;
            title = cachingSongService.getTitleFromUrl(url);
        } else {
            logger.info("Detected title: " + song);
            url = cachingSongService.getUrlFromTitle(song);
            title = cachingSongService.getTitleFromUrl(url);
        }
        playlistManager.addSong(event.getGuild().getIdLong(), new Song(title, url, event.getAuthor().getName()));
        logger.info("Song added to playlist: " + title);
        event.getChannel().sendMessage("Song added to playlist: " + title).queue();
    }

    private void removeSongFromPlaylist(MessageReceivedEvent event, String song) {
        Song removedSong = playlistManager.removeSong(event.getGuild().getIdLong(), song);
        if (removedSong != null) {
            event.getChannel().sendMessage("Song removed from playlist: " + removedSong.getTitle()).queue();
        } else {
            event.getChannel().sendMessage("Song not found in playlist: " + song).queue();
        }
    }

    public static boolean isUrl(String input) {
        try {
            URI uri = URI.create(input);
            return uri.getScheme() != null
                    && (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"));
        } catch (Exception e) {
            return false;
        }
    }
}
