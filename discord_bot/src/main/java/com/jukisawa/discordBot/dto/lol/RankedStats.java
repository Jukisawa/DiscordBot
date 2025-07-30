package com.jukisawa.discordBot.dto.lol;

import org.json.JSONObject;

import lombok.Data;


@Data
public class RankedStats {
    private String leagueId;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean veteran;
    private boolean freshBlood;
    private boolean inactive;
    private boolean hotStreak;

    public RankedStats(JSONObject stats) {
        this.leagueId = stats.optString("leagueId", "Unknown");
        this.queueType = stats.optString("queueType", "Unknown");
        this.tier = stats.optString("tier", "Unranked");
        this.rank = stats.optString("rank", "Unranked");
        this.leaguePoints = stats.optInt("leaguePoints", 0);
        this.wins = stats.optInt("wins", 0);
        this.losses = stats.optInt("losses", 0);
        this.veteran = stats.optBoolean("veteran", false);
        this.freshBlood = stats.optBoolean("freshBlood", false);
        this.inactive = stats.optBoolean("inactive", false);
        this.hotStreak = stats.optBoolean("hotStreak", false);
    }
}
