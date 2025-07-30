package com.jukisawa.discordBot.dto.lol;

import java.util.List;

import org.json.JSONObject;

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

    public long getChampionId() {
        return championId;
    }

    public void setChampionId(long championId) {
        this.championId = championId;
    }

    public int getChampionLevel() {
        return championLevel;
    }

    public void setChampionLevel(int championLevel) {
        this.championLevel = championLevel;
    }

    public int getChampionPoints() {
        return championPoints;
    }

    public void setChampionPoints(int championPoints) {
        this.championPoints = championPoints;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public int getChampionPointsSinceLastLevel() {
        return championPointsSinceLastLevel;
    }

    public void setChampionPointsSinceLastLevel(int championPointsSinceLastLevel) {
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
    }

    public int getChampionPointsUntilNextLevel() {
        return championPointsUntilNextLevel;
    }

    public void setChampionPointsUntilNextLevel(int championPointsUntilNextLevel) {
        this.championPointsUntilNextLevel = championPointsUntilNextLevel;
    }

    public int getMarkRequiredForNextLevel() {
        return markRequiredForNextLevel;
    }

    public void setMarkRequiredForNextLevel(int markRequiredForNextLevel) {
        this.markRequiredForNextLevel = markRequiredForNextLevel;
    }

    public int getChampionSeasonMilestone() {
        return championSeasonMilestone;
    }

    public void setChampionSeasonMilestone(int championSeasonMilestone) {
        this.championSeasonMilestone = championSeasonMilestone;
    }

    public int getTokensEarned() {
        return tokensEarned;
    }

    public void setTokensEarned(int tokensEarned) {
        this.tokensEarned = tokensEarned;
    }

    public List<String> getMilestoneGrades() {
        return milestoneGrades;
    }

    public void setMilestoneGrades(List<String> milestoneGrades) {
        this.milestoneGrades = milestoneGrades;
    }
}
