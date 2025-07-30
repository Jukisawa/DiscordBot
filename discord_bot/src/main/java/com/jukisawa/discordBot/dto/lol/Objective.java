package com.jukisawa.discordBot.dto.lol;


public record Objective(String name, boolean first, int kills) {
    public Objective(String name, boolean first, int kills) {
        this.first= first;
        this.kills = kills;
        this.name = name;
    }
}
