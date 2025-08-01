package com.jukisawa.discordBot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.event.CommandEvent;
import com.jukisawa.discordBot.event.EventBus;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Thin Discord event listener that publishes events to the event bus
 * instead of directly handling commands
 */
public class DiscordEventListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DiscordEventListener.class);
    
    private final EventBus eventBus;
    private final String commandPrefix;

    public DiscordEventListener(EventBus eventBus, String commandPrefix) {
        this.eventBus = eventBus;
        this.commandPrefix = commandPrefix;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        
        // Ignore bot messages and non-guild messages
        if (event.getAuthor().isBot() || !event.isFromGuild()) {
            return;
        }

        // Check if message starts with command prefix
        if (!message.startsWith(commandPrefix)) {
            return;
        }

        // Extract command and arguments
        String withoutPrefix = message.substring(commandPrefix.length()).trim();
        String command = extractCommand(withoutPrefix);
        String arguments = withoutPrefix.substring(command.length()).trim();

        logger.debug("Publishing command event: {} with args: {}", command, arguments);

        // Publish event asynchronously - doesn't block Discord thread
        CommandEvent commandEvent = new CommandEvent(command, arguments, event);
        eventBus.publish(commandEvent);
    }

    private String extractCommand(String message) {
        int spaceIndex = message.indexOf(' ');
        if (spaceIndex == -1) {
            return message;
        }
        return message.substring(0, spaceIndex);
    }
}
