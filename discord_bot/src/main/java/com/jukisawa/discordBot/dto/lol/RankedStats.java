package com.jukisawa.discordBot.dto.lol;

import org.json.JSONObject;

public record RankedStats(String leagueId, String queueType, String tier, String rank, int leaguePoints, int wins,
        int losses, boolean veteran, boolean freshBlood, boolean inactive, boolean hotStreak) {

    public RankedStats(JSONObject stats) {
        this(stats.optString("leagueId", "Unknown"), stats.optString("queueType", "Unknown"),
                stats.optString("tier", "Unranked"), stats.optString("rank", "Unranked"),
                stats.optInt("leaguePoints", 0), stats.optInt("wins", 0), stats.optInt("losses", 0),
                stats.optBoolean("veteran", false), stats.optBoolean("freshBlood", false),
                stats.optBoolean("inactive", false), stats.optBoolean("hotStreak", false));
    }
}
