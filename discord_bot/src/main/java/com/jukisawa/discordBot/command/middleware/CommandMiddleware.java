package com.jukisawa.discordBot.command.middleware;

import com.jukisawa.discordBot.event.CommandEvent;

/**
 * Middleware interface for processing commands before they reach the handler.
 * Allows for cross-cutting concerns like logging, rate limiting, authentication.
 */
@FunctionalInterface
public interface CommandMiddleware {
    /**
     * Execute the middleware logic
     * @param event The command event
     * @param next Runnable to continue to next middleware or handler
     */
    void execute(CommandEvent event, Runnable next);
}
