package com.jukisawa.discordBot.http;

import java.net.http.HttpHeaders;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRateLimitManager implements RateLimitManager {

    private final Map<String, Instant> routeBlockUntil = new ConcurrentHashMap<>();
    private volatile Instant globalBlockUntil = Instant.EPOCH;

    @Override
    public void beforeRequest(String route) {
        Instant now = Instant.now();
        Instant routeUntil = routeBlockUntil.getOrDefault(route, Instant.EPOCH);
        Instant globalUntil = globalBlockUntil;

        Instant waitUntil = routeUntil.isAfter(globalUntil) ? routeUntil : globalUntil;

        if (now.isBefore(waitUntil)) {
            Duration wait = Duration.between(now, waitUntil);
            System.out.println("Waiting " + wait.toMillis() + "ms before requesting: " + route);
            try {
                Thread.sleep(wait.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted during rate limit wait", e);
            }
        }
    }

    @Override
    public void afterResponse(String route, int statusCode, HttpHeaders headers) {
        if (statusCode == 429)
            return; // handled in onRateLimit

        parseRateLimitHeaders(route, headers);
    }

    @Override
    public void onRateLimit(String route, Duration wait) {
        Instant waitUntil = Instant.now().plus(wait);
        System.out.println("Rate limit hit on " + route + ", delaying until " + waitUntil);
        routeBlockUntil.put(route, waitUntil);
    }

    private void parseRateLimitHeaders(String route, HttpHeaders headers) {
        Optional<String> resetAfter = headers.firstValue("X-RateLimit-Reset-After");
        Optional<String> resetTimestamp = headers.firstValue("X-RateLimit-Reset");

        // Option 1: Use Reset-After in seconds
        if (resetAfter.isPresent()) {
            try {
                long seconds = (long) Double.parseDouble(resetAfter.get());
                Instant resume = Instant.now().plusSeconds(seconds);
                routeBlockUntil.put(route, resume);
                return;
            } catch (NumberFormatException ignored) {
            }
        }

        // Option 2: Use absolute reset timestamp (UNIX seconds)
        if (resetTimestamp.isPresent()) {
            try {
                long epoch = Long.parseLong(resetTimestamp.get());
                Instant resume = Instant.ofEpochSecond(epoch);
                routeBlockUntil.put(route, resume);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    /** Optional: Add support for global rate limit */
    public void setGlobalLimit(Duration wait) {
        globalBlockUntil = Instant.now().plus(wait);
    }

    public void clearAllLimits() {
        routeBlockUntil.clear();
        globalBlockUntil = Instant.EPOCH;
    }
}