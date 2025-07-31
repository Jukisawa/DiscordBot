package com.jukisawa.discordBot.command.music;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Nested command registry for playlist commands
 * Handles: !playlist add, !playlist remove, !playlist play, etc.
 * 
 * This follows the same simple pattern as LoL commands but distributes
 * to individual handlers for each sub-command.
 */
public class PlaylistCommandRegistry implements CommandHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(PlaylistCommandRegistry.class);
    private final Map<String, CommandHandler> subCommands = new HashMap<>();
    
    public PlaylistCommandRegistry() {
        // Register all sub-command handlers using the simple pattern
        subCommands.put("add", new PlaylistAddCommandHandler());
        subCommands.put("remove", new PlaylistRemoveCommandHandler());
        subCommands.put("list", new PlaylistListCommandHandler());
        subCommands.put("play", new PlaylistPlayCommandHandler());
        subCommands.put("pause", new PlaylistPauseCommandHandler());
        subCommands.put("resume", new PlaylistResumeCommandHandler());
        subCommands.put("stop", new PlaylistStopCommandHandler());
        subCommands.put("skip", new PlaylistSkipCommandHandler());
        subCommands.put("clear", new PlaylistClearCommandHandler());
        subCommands.put("shuffle", new PlaylistShuffleCommandHandler());
        subCommands.put("repeat", new PlaylistRepeatCommandHandler());
        subCommands.put("volume", new PlaylistVolumeCommandHandler());
        subCommands.put("help", new PlaylistHelpCommandHandler());
        
        logger.info("Registered {} playlist sub-commands", subCommands.size());
    }

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        logger.info("Handling playlist command: " + arguments);
        
        if (arguments.trim().isEmpty()) {
            // Default to help if no sub-command provided
            subCommands.get("help").handle(event, "");
            return;
        }
        
        // Parse sub-command and arguments
        String[] parts = arguments.trim().split("\\s+", 2);
        String subCommand = parts[0].toLowerCase();
        String subArguments = parts.length > 1 ? parts[1] : "";
        
        // Find and execute the appropriate handler
        CommandHandler handler = subCommands.get(subCommand);
        if (handler != null) {
            logger.info("Executing playlist sub-command: {} with args: {}", subCommand, subArguments);
            handler.handle(event, subArguments);
        } else {
            // Unknown sub-command, show help
            event.getChannel().sendMessage("Unknown playlist command: `" + subCommand + 
                "`. Use `!playlist help` to see available commands.").queue();
        }
    }
}
