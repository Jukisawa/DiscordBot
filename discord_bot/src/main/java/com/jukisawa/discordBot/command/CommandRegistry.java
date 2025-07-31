package com.jukisawa.discordBot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.command.middleware.CommandMiddleware;
import com.jukisawa.discordBot.event.CommandEvent;

/**
 * Registry for commands with middleware support
 */
public class CommandRegistry {
    private static final Logger logger = LoggerFactory.getLogger(CommandRegistry.class);
    
    private final Map<String, CommandChain> commands = new HashMap<>();
    private final List<CommandMiddleware> globalMiddleware = new ArrayList<>();

    /**
     * Add global middleware that applies before all commands
     */
    public void addGlobalMiddleware(CommandMiddleware... middleware) {
        this.globalMiddleware.addAll(Arrays.asList(middleware));
        logger.info("Added {} global middleware", middleware.length);
    }

    /**
     * Register a command with optional command-specific middleware
     */
    public void registerCommand(String name, CommandHandler handler, CommandMiddleware... middleware) {
        CommandChain chain = new CommandChain();

        // Add global middleware first
        globalMiddleware.forEach(chain::addMiddleware);

        // Add command-specific middleware
        Arrays.stream(middleware).forEach(chain::addMiddleware);

        // Add actual handler
        chain.setHandler(handler);

        commands.put(name.toLowerCase(), chain);
        logger.info("Registered command: {} with {} middleware", name, 
            globalMiddleware.size() + middleware.length);
    }

    /**
     * Execute a command asynchronously
     */
    public CompletableFuture<Void> executeCommand(CommandEvent event) {
        CommandChain chain = commands.get(event.command().toLowerCase());
        if (chain == null) {
            logger.debug("No handler found for command: {}", event.command());
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            try {
                chain.execute(event);
            } catch (Exception e) {
                logger.error("Error executing command {}: ", event.command(), e);
                event.discordEvent().getChannel()
                    .sendMessage("❌ An error occurred while processing your command.").queue();
            }
        });
    }

    /**
     * Get all registered command names
     */
    public java.util.Set<String> getCommandNames() {
        return commands.keySet();
    }
}
