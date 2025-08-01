package com.jukisawa.discordBot.command;

import java.util.ArrayList;
import java.util.List;

import com.jukisawa.discordBot.command.middleware.CommandMiddleware;
import com.jukisawa.discordBot.event.CommandEvent;

/**
 * Represents a chain of middleware and a final command handler
 */
public class CommandChain {
    private final List<CommandMiddleware> middleware = new ArrayList<>();
    private CommandHandler handler;

    /**
     * Add middleware to the chain
     */
    public void addMiddleware(CommandMiddleware middleware) {
        this.middleware.add(middleware);
    }

    /**
     * Set the final command handler
     */
    public void setHandler(CommandHandler handler) {
        this.handler = handler;
    }

    /**
     * Execute the command chain
     */
    public void execute(CommandEvent event) {
        executeMiddleware(event, 0);
    }

    private void executeMiddleware(CommandEvent event, int index) {
        if (index >= middleware.size()) {
            // All middleware executed, run the handler
            if (handler != null) {
                // Convert CommandEvent back to original format for existing handlers
                handler.handle(event.discordEvent(), event.arguments());
            }
            return;
        }

        CommandMiddleware current = middleware.get(index);
        current.execute(event, () -> executeMiddleware(event, index + 1));
    }
}
