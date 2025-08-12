package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.GuildMusicManager;
import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

public class PlaylistVolumeCommandHandler implements CommandHandler {
    
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistVolumeCommandHandler.class);

    @Override
    public void handle(CommandEvent event, String arguments) {
        logger.info("Handling playlist volume command: " + arguments);
        
        if (arguments.isEmpty()) {
            event.channel().sendMessage("Bitte gib eine Lautstärke an.").queue();
            return;
        }
        
        try {
            int volume = Integer.parseInt(arguments);
            if (volume < 0 || volume > 100) {
                event.channel().sendMessage("Lautstärke muss zwischen 0 und 100 liegen.").queue();
            } else {
                GuildMusicManager musicManager = playerManager.getMusicManager(event.guild());
                musicManager.player.setVolume(volume);
                event.channel().sendMessage("Lautstärke auf " + volume + " gesetzt.").queue();
            }
        } catch (NumberFormatException e) {
            event.channel().sendMessage("Ungültige Lautstärkeangabe.").queue();
        }
    }
}
