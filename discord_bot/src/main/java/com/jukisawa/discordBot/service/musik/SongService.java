package com.jukisawa.discordBot.service.musik;

public interface SongService {
    String getUrlFromTitle(String title) throws Exception;
    String getTitleFromUrl(String url) throws Exception;
}