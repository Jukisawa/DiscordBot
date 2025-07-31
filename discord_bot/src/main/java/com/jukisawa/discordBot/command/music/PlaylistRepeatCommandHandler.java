package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.audio.PlaylistManager;
import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlaylistRepeatCommandHandler implements CommandHandler {
    
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistRepeatCommandHandler.class);

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        logger.info("Handling playlist repeat command");
        playlistManager.switchRepeatQueue(event.getGuild().getIdLong());
        event.getChannel().sendMessage("Repeat mode ist nun "
                + (playlistManager.isRepeatQueue(event.getGuild().getIdLong()) ? "Aktiv" : "Deaktiv"))
                .queue();
    }
}
