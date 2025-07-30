package com.jukisawa.discordBot.dto.lol;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import lombok.Data;

@Data
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
        this.endOfGameResult = matchData.optString("endOfGameResult");
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

}
