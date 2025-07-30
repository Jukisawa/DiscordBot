package com.jukisawa.discordBot.dto.music;

import lombok.Data;

@Data
public class Song {
    private final String title;
    private final String url;
    private final String requestedBy;

}
