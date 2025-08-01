package com.jukisawa.discordBot.command.music;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Handler for pausing music - matches LoL command pattern
 */
public class PlaylistPauseCommandHandler implements CommandHandler {
    private final PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        playerManager.getMusicManager(event.getGuild()).player.setPaused(true);
        event.getChannel().sendMessage("✅ ⏸️ Musik pausiert.").queue();
    }
}
