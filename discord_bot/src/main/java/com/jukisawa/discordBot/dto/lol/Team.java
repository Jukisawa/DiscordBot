package com.jukisawa.discordBot.dto.lol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Team {

    private List<Integer> bans;
    private Map<String, Integer> feats;
    private List<Objective> objectives;
    private boolean win;

    public Team(JSONObject team) {
        this.bans = team.getJSONArray("bans")
                .toList()
                .stream()
                .map(JSONObject::new)
                .map(json -> json.optInt("championId"))
                .toList();
        this.feats = new HashMap<>();
        JSONObject featsJson = team.getJSONObject("feats");
        for (String key : featsJson.keySet()) {
            JSONObject featObject = featsJson.getJSONObject(key);
            int featState = featObject.getInt("featState");
            feats.put(key, featState);
        }
        this.objectives = new ArrayList<>();
        JSONObject objectivesJson = team.getJSONObject("objectives");
        for (String key : objectivesJson.keySet()) {
            Objective objective = new Objective();
            objective.setName(key);
            objective.setFirst(objectivesJson.optBoolean(key));
            objective.setKills(objectivesJson.optInt(key));
        }
        this.win = team.optBoolean("win");
    }
}
