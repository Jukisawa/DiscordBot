package com.jukisawa.discordBot.http;

import java.net.http.HttpHeaders;
import java.time.Duration;

public interface RateLimitManager {
    void beforeRequest(String route);
    void afterResponse(String route, int statusCode, HttpHeaders headers);
    void onRateLimit(String route, Duration wait);
}