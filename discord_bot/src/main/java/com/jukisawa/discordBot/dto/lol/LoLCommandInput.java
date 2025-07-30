package com.jukisawa.discordBot.dto.lol;

public class LoLCommandInput {
    private String riotId;
    private int gameCount;
    private QueueType queueType;

    public LoLCommandInput(String riotId, int gameCount, QueueType queueType) {
        this.gameCount = gameCount;
        this.queueType = queueType;
        this.riotId = riotId;
    }

    public String getRiotId() {
        return riotId;
    }

    public void setRiotId(String riotId) {
        this.riotId = riotId;
    }

    public int getGameCount() {
        return gameCount;
    }

    public void setGameCount(int gameCount) {
        this.gameCount = gameCount;
    }

    public QueueType getQueueType() {
        return queueType;
    }

    public void setQueueType(QueueType queueType) {
        this.queueType = queueType;
    }
}
