package com.jukisawa.discordBot.dto.lol;

public record Item(int id, String name, int price) {
    public Item(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
