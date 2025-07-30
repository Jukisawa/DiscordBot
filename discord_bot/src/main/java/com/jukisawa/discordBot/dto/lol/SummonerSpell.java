package com.jukisawa.discordBot.dto.lol;

public record SummonerSpell(int id, String name) {
    public SummonerSpell(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
