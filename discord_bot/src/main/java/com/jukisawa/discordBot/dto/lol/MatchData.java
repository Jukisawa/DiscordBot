package com.jukisawa.discordBot.dto.lol;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class MatchData {
    private String endOfGameResult;
    private Long gameCreation;
    private int gameDuration;
    private Long gameEndTimestamp;
    private Long gameId;
    private String gameMode;
    private String gameName;
    private Long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private int mapId;
    private List<MatchParticipant> participants;
    private List<Team> teams;

    public MatchData(JSONObject matchData) {
        this.endOfGameResult = matchData.optString("endOfGameResult");
        this.gameMode = matchData.optString("gameMode");
        this.gameName = matchData.optString("gameName");
        this.gameVersion = matchData.optString("gameVersion");
        this.gameType = matchData.optString("gameType");
        this.gameCreation = matchData.optLong("gameCreation");
        this.gameEndTimestamp = matchData.optLong("gameEndTimestamp");
        this.gameId = matchData.optLong("gameId");
        this.gameStartTimestamp = matchData.optLong("gameStartTimestamp");
        this.gameDuration = matchData.optInt("gameDuration");
        this.mapId = matchData.optInt("mapId");
        this.participants = matchData.getJSONArray("participants")
                .toList()
                .stream()
                .map(o -> (HashMap<?, ?>) o)
                .map(JSONObject::new)
                .map(MatchParticipant::new)
                .collect(Collectors.toList());
        this.teams = matchData.getJSONArray("teams")
                .toList()
                .stream()
                .map(o -> (HashMap<?, ?>) o)
                .map(JSONObject::new)
                .map(Team::new)
                .collect(Collectors.toList());

    }

    public String getEndOfGameResult() {
        return endOfGameResult;
    }

    public void setEndOfGameResult(String endOfGameResult) {
        this.endOfGameResult = endOfGameResult;
    }

    public Long getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(Long gameCreation) {
        this.gameCreation = gameCreation;
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
    }

    public Long getGameEndTimestamp() {
        return gameEndTimestamp;
    }

    public void setGameEndTimestamp(Long gameEndTimestamp) {
        this.gameEndTimestamp = gameEndTimestamp;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Long getGameStartTimestamp() {
        return gameStartTimestamp;
    }

    public void setGameStartTimestamp(Long gameStartTimestamp) {
        this.gameStartTimestamp = gameStartTimestamp;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public List<MatchParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MatchParticipant> participants) {
        this.participants = participants;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}
