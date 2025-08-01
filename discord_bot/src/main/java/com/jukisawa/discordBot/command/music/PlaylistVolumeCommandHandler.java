package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.GuildMusicManager;
import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlaylistVolumeCommandHandler implements CommandHandler {
    
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistVolumeCommandHandler.class);

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        logger.info("Handling playlist volume command: " + arguments);
        
        if (arguments.isEmpty()) {
            event.getChannel().sendMessage("Bitte gib eine Lautstärke an.").queue();
            return;
        }
        
        try {
            int volume = Integer.parseInt(arguments);
            if (volume < 0 || volume > 100) {
                event.getChannel().sendMessage("Lautstärke muss zwischen 0 und 100 liegen.").queue();
            } else {
                GuildMusicManager musicManager = playerManager.getMusicManager(event.getGuild());
                musicManager.player.setVolume(volume);
                event.getChannel().sendMessage("Lautstärke auf " + volume + " gesetzt.").queue();
            }
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Ungültige Lautstärkeangabe.").queue();
        }
    }
}
