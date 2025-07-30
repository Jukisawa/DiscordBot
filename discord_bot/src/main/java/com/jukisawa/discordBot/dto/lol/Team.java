package com.jukisawa.discordBot.dto.lol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public record Team(
        List<Integer> bans,
        Map<String, Integer> feats,
        List<Objective> objectives,
        boolean win) {
    public Team(JSONObject teamJson) {
        this(parseBans(teamJson),
                parseFeats(teamJson),
                parseObjectives(teamJson),
                teamJson.optBoolean("win"));
    }

    private static List<Integer> parseBans(JSONObject team) {
        return team.getJSONArray("bans")
                .toList()
                .stream()
                .map(obj -> new JSONObject((Map<?, ?>) obj)) // safely cast from raw Map
                .map(json -> json.optInt("championId"))
                .toList();
    }

    private static Map<String, Integer> parseFeats(JSONObject team) {
        Map<String, Integer> feats = new HashMap<>();
        JSONObject featsJson = team.getJSONObject("feats");
        for (String key : featsJson.keySet()) {
            JSONObject featObject = featsJson.getJSONObject(key);
            feats.put(key, featObject.getInt("featState"));
        }
        return feats;
    }

    private static List<Objective> parseObjectives(JSONObject team) {
        List<Objective> objectives = new ArrayList<>();
        JSONObject objectivesJson = team.getJSONObject("objectives");
        for (String key : objectivesJson.keySet()) {
            JSONObject obj = objectivesJson.getJSONObject(key);
            objectives.add(new Objective(key, obj.optBoolean("first"), obj.optInt("kills")));
        }
        return objectives;
    }
}
