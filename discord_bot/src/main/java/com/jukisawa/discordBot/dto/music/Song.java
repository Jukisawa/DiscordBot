package com.jukisawa.discordBot.dto.music;

public record Song(String title, String url, String requestedBy) {
    public Song(String title, String url, String requestedBy) {
        this.requestedBy = requestedBy;
        this.title = title;
        this.url = url;
    }
}
