package com.jukisawa.discordBot.dto.lol;

import java.util.List;

import org.json.JSONObject;

import lombok.Data;

@Data
public class ChampionMastery {
    private long championId;
    private int championLevel;
    private int championPoints;
    private long lastPlayTime;
    private int championPointsSinceLastLevel;
    private int championPointsUntilNextLevel;
    private int markRequiredForNextLevel;
    private int championSeasonMilestone;
    private int tokensEarned;
    private List<String> milestoneGrades;

    public ChampionMastery(JSONObject mastery) {
        this.championId = mastery.optLong("championId", 0);
        this.championLevel = mastery.optInt("championLevel", 0);
        this.championPoints = mastery.optInt("championPoints", 0);
        this.lastPlayTime = mastery.optLong("lastPlayTime", 0);
        this.championPointsSinceLastLevel = mastery.optInt("championPointsSinceLastLevel", 0);
        this.championPointsUntilNextLevel = mastery.optInt("championPointsUntilNextLevel", 0);
        this.markRequiredForNextLevel = mastery.optInt("markRequiredForNextLevel", 0);
        this.championSeasonMilestone = mastery.optInt("championSeasonMilestone", 0);
        this.tokensEarned = mastery.optInt("tokensEarned", 0);
        this.milestoneGrades = mastery.has("milestoneGrades")
                ? mastery.getJSONArray("milestoneGrades").toList().stream()
                        .map(Object::toString).toList()
                : List.of();
    }
}
