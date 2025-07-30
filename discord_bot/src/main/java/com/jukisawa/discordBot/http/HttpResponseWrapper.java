package com.jukisawa.discordBot.http;

import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import javax.net.ssl.SSLSession;


public class HttpResponseWrapper<T> implements HttpResponse<T>, AutoCloseable {

    private final HttpResponse<String> raw;
    private final T body;

    public HttpResponseWrapper(HttpResponse<String> raw, T body) {
        this.raw = raw;
        this.body = body;
    }

    @Override
    public int statusCode() {
        return raw.statusCode();
    }

    @Override
    public HttpRequest request() {
        return raw.request();
    }

    @Override
    public Optional<HttpResponse<T>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return raw.headers();
    }

    @Override
    public T body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return raw.sslSession();
    }

    @Override
    public URI uri() {
        return raw.uri();
    }

    @Override
    public Version version() {
        return raw.version();
    }

    @Override
    public void close() {
        // If you have any resources to close (like streams), close them here.
        // Otherwise, leave empty.
    }
}