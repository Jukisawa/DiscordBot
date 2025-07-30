package com.jukisawa.discordBot.dto.lol;

public record LolProfile(String gameName, String tagLine) {
    public LolProfile(String gameName, String tagLine) {
        this.gameName = gameName;
        this.tagLine = tagLine;
    }
}
