package com.jukisawa.discordBot.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestBuilder {
    private final RateLimitManager rateLimitManager;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> parameters = new HashMap<>();
    private String protocol = "https";
    private String host;
    private List<String> pathSegments = new ArrayList<>();
    private String method = "GET";

    public HttpRequestBuilder(RateLimitManager rateLimitManager) {
        this.rateLimitManager = rateLimitManager;
    }

    public HttpRequestBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public HttpRequestBuilder host(String host) {
        this.host = host;
        return this;
    }

    public HttpRequestBuilder path(String... segments) {
        this.pathSegments.addAll(List.of(segments));
        return this;
    }

    public HttpRequestBuilder get() {
        this.method = "GET";
        return this;
    }

    public HttpRequestBuilder addQueryParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public HttpRequestBuilder addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public HttpRequestBuilder headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public WrappedRequest build() {
        StringBuilder url = new StringBuilder();
        url.append(protocol).append("://").append(host).append("/");
        url.append(String.join("/", pathSegments));
        if (parameters.size() > 0) {
            url.append("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                char last = url.charAt(url.length() - 1);
                if (last != '?' && last != '&')
                    url.append('&');
                try {
                    String key = URLEncoder.encode(entry.getKey(), "UTF-8");
                    String value = URLEncoder.encode(entry.getValue(), "UTF-8");
                    url.append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        URI uri = URI.create(protocol + "://" + host + "/" + String.join("/", pathSegments));
        return new WrappedRequest(uri, method, headers, rateLimitManager);
    }
}
