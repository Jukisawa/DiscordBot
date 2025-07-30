package com.jukisawa.discordBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lol_accounts")
public class LoLAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long discordUserId;
    private String gameName;
    private String tagLine;

    public LoLAccount() {
    }

    public LoLAccount(Long discordUserId, String gameName, String tagLine) {
        this.tagLine = tagLine;
        this.discordUserId = discordUserId;
        this.gameName = gameName;
    }

    public Long getId() {
        return this.id;
    }

    public Long getDiscordUserId() {
        return this.discordUserId;
    }

    public String getGameName() {
        return this.gameName;
    }

    public String getTagLine() {
        return this.tagLine;
    }

    public void setDiscordUserId(Long discorduserId) {
        this.discordUserId = discorduserId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
}
