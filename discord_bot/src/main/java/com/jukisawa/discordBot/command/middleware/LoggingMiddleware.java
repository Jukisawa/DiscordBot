package com.jukisawa.discordBot.command.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.event.CommandEvent;

/**
 * Middleware that logs command execution timing and details
 */
public class LoggingMiddleware implements CommandMiddleware {
    private static final Logger logger = LoggerFactory.getLogger(LoggingMiddleware.class);

    @Override
    public void execute(CommandEvent event, Runnable next) {
        logger.info("Executing command: {} by user: {} in guild: {}", 
            event.command(), 
            event.getUserName(), 
            event.getGuildId());

        long start = System.currentTimeMillis();
        try {
            next.run();
        } catch (Exception e) {
            logger.error("Command {} failed for user {}: ", 
                event.command(), event.getUserName(), e);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            logger.info("Command {} completed in {}ms", event.command(), duration);
        }
    }
}
