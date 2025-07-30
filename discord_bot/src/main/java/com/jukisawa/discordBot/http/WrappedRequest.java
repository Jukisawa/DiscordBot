package com.jukisawa.discordBot.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WrappedRequest {
    private final URI uri;
    private final String method;
    private final Map<String, String> headers;
    private final RateLimitManager rateLimitManager;
    private final HttpClient client = HttpClient.newHttpClient();

    public WrappedRequest(URI uri, String method, Map<String, String> headers, RateLimitManager rateLimitManager) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.rateLimitManager = rateLimitManager;
    }

    private HttpRequest buildHttpRequest() {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .method(method, HttpRequest.BodyPublishers.noBody());

        headers.forEach(builder::header);
        return builder.build();
    }

    public HttpResponseWrapper<JSONObject> getAsJSONObject() throws IOException, InterruptedException, JSONException {
        int retries = 5;

        for (int i = 0; i < retries; i++) {
            rateLimitManager.beforeRequest(uri.toString());

            HttpRequest request = buildHttpRequest();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            rateLimitManager.afterResponse(uri.toString(), response.statusCode(), response.headers());

            if (response.statusCode() == 429) {
                Duration wait = getRetryAfter(response);
                rateLimitManager.onRateLimit(uri.toString(), wait);
                Thread.sleep(wait.toMillis());
                continue;
            }

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JSONObject json = new JSONObject(response.body());
                return new HttpResponseWrapper<>(response, json);
            }

            return new HttpResponseWrapper<>(response, null);
        }

        throw new RuntimeException("Too many retries: rate limit");
    }

    public HttpResponseWrapper<JSONArray> getAsJSONArray() throws IOException, InterruptedException, JSONException {
        int retries = 5;

        for (int i = 0; i < retries; i++) {
            rateLimitManager.beforeRequest(uri.toString());

            HttpRequest request = buildHttpRequest();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            rateLimitManager.afterResponse(uri.toString(), response.statusCode(), response.headers());

            if (response.statusCode() == 429) {
                Duration wait = getRetryAfter(response);
                rateLimitManager.onRateLimit(uri.toString(), wait);
                Thread.sleep(wait.toMillis());
                continue;
            }

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JSONArray json = new JSONArray(response.body());
                return new HttpResponseWrapper<>(response, json);
            }

            return new HttpResponseWrapper<>(response, null);
        }

        throw new RuntimeException("Too many retries: rate limit");
    }

    public String getUrl() {
        return uri.toString();
    }

    private Duration getRetryAfter(HttpResponse<?> response) {
        return response.headers().firstValue("Retry-After")
                .map(val -> {
                    try {
                        return Duration.ofSeconds(Long.parseLong(val));
                    } catch (NumberFormatException e) {
                        return Duration.ofSeconds(1);
                    }
                })
                .orElse(Duration.ofSeconds(1));
    }
}
