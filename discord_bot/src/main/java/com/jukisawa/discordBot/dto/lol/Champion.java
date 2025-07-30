package com.jukisawa.discordBot.dto.lol;

public record Champion(int id, String name) {
    public Champion(int id, String name) {
        this.id = id;
        this.name = name;
    }
}