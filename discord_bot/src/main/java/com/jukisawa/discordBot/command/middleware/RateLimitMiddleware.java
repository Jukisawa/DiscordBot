package com.jukisawa.discordBot.command.middleware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.jukisawa.discordBot.event.CommandEvent;

/**
 * Simple rate limiting middleware based on user ID
 */
public class RateLimitMiddleware implements CommandMiddleware {
    private final Map<Long, Long> lastCommandTime = new ConcurrentHashMap<>();
    private final long rateLimitMs;

    /**
     * Create rate limit middleware
     * @param rateLimitSeconds Minimum seconds between commands per user
     */
    public RateLimitMiddleware(int rateLimitSeconds) {
        this.rateLimitMs = TimeUnit.SECONDS.toMillis(rateLimitSeconds);
    }

    /**
     * Create rate limit middleware with default 2 second limit
     */
    public RateLimitMiddleware() {
        this(2);
    }

    @Override
    public void execute(CommandEvent event, Runnable next) {
        long userId = event.getUserId();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastCommandTime.getOrDefault(userId, 0L);

        if (currentTime - lastTime < rateLimitMs) {
            event.discordEvent().getChannel()
                .sendMessage("⏰ Rate limit exceeded. Please slow down.").queue();
            return;
        }

        lastCommandTime.put(userId, currentTime);
        next.run();
    }
}
