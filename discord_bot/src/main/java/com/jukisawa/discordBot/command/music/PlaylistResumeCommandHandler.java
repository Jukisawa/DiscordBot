package com.jukisawa.discordBot.command.music;

import com.jukisawa.discordBot.audio.PlayerManager;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

/**
 * Handler for resuming music - matches LoL command pattern
 */
public class PlaylistResumeCommandHandler implements CommandHandler {
    private final PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    public void handle(CommandEvent event, String arguments) {
        playerManager.getMusicManager(event.guild()).player.setPaused(false);
        event.channel().sendMessage("✅ ▶️ Musik fortgesetzt.").queue();
    }
}
