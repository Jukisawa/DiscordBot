package com.jukisawa.discordBot.dto.lol;
public enum QueueType {
    ALL("Alle Warteschlangen", 0),
    NORMAL_DRAFT("Normal Draft", 400),
    RANKED_SOLO_5x5("Ranked Solo", 420),
    RANKED_FLEX_SR("Ranked Flex", 440),
    QUICKPLAY("Quickplay", 490),
    ARAM("ARAM", 450);

    private final String name;
    private final int id;

    QueueType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static QueueType getByName(String name) {
        for (QueueType queueType : values()) {
            if (queueType.name.toLowerCase().contains(name.trim().toLowerCase())) {
                return queueType;
            }
        }
        throw new IllegalArgumentException("Unbekannter Queue-Typ: " + name);
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}