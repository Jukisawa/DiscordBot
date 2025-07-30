package com.jukisawa.discordBot.service.lol;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jukisawa.discordBot.dto.lol.ChampionMastery;
import com.jukisawa.discordBot.dto.lol.MatchData;
import com.jukisawa.discordBot.dto.lol.QueueType;
import com.jukisawa.discordBot.dto.lol.RankedStats;
import com.jukisawa.discordBot.exceptions.DataNotFoundException;
import com.jukisawa.discordBot.http.HttpRequestBuilder;
import com.jukisawa.discordBot.http.HttpResponseWrapper;
import com.jukisawa.discordBot.http.RateLimitManager;
import com.jukisawa.discordBot.http.SimpleRateLimitManager;
import com.jukisawa.discordBot.http.WrappedRequest;
import com.jukisawa.discordBot.service.ConfigService;

public class RiotApiService {
    private final String riotApiKey;
    static RateLimitManager rateLimitManager = new SimpleRateLimitManager();
    static String europeRoute = "europe.api.riotgames.com";
    static String euwRoute = "euw1.api.riotgames.com";

    public RiotApiService() {
        String riotApiKey;
        try {
            riotApiKey = ConfigService.loadConfig("riotApiKey");
        } catch (RuntimeException e) {
            throw new RuntimeException("Riot API Key nicht in config.properties gefunden!", e);
        }
        this.riotApiKey = riotApiKey;
    }

    public String getPuuid(String nameWithTag) throws DataNotFoundException, IOException, JSONException, InterruptedException {
        String[] parts = nameWithTag.split("#");
        if (parts.length != 2)
            throw new IllegalArgumentException("Format muss Name#Tag sein!");

        String name = URLEncoder.encode(parts[0], "UTF-8").replace("+", "%20");
        String tag = URLEncoder.encode(parts[1], "UTF-8");

        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(europeRoute)
                .path("riot", "account", "v1", "accounts", "by-riot-id")
                .path(name, tag)
                .addHeader("X-Riot-Token", riotApiKey)
                .get().build();
        try (HttpResponseWrapper<JSONObject> response = request.getAsJSONObject()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());
            return response.body().getString("puuid");
        }
    }

    public List<ChampionMastery> getChampionMasterys(String puuid) throws DataNotFoundException, IOException, JSONException, InterruptedException {
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(euwRoute)
                .path("lol", "champion-mastery", "v4", "champion-masteries", "by-puuid")
                .path(puuid)
                .addHeader("X-Riot-Token", riotApiKey)
                .get().build();
        try (HttpResponseWrapper<JSONArray> response = request.getAsJSONArray()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());

            return response.body()
                    .toList()
                    .stream()
                    .map(o -> (HashMap<?, ?>) o)
                    .map(JSONObject::new)
                    .map(ChampionMastery::new)
                    .collect(Collectors.toList());
        }

    }

    public int getSummonerLevel(String puuid) throws DataNotFoundException, IOException, JSONException, InterruptedException {
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(euwRoute)
                .path("lol", "summoner", "v4", "summoners", "by-puuid")
                .path(puuid)
                .addHeader("X-Riot-Token", riotApiKey)
                .get().build();
        try (HttpResponseWrapper<JSONObject> response = request.getAsJSONObject()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());
            return response.body().optInt("summonerLevel", 0);
        }
    }

    public List<RankedStats> getRankedStats(String puuid) throws DataNotFoundException, IOException, JSONException, InterruptedException {
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(euwRoute)
                .path("lol", "league", "v4", "entries", "by-puuid")
                .path(puuid)
                .addHeader("X-Riot-Token", riotApiKey)
                .get().build();
        try (HttpResponseWrapper<JSONArray> response = request.getAsJSONArray()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());

            return response.body()
                    .toList()
                    .stream()
                    .map(o -> (HashMap<?, ?>) o)
                    .map(JSONObject::new)
                    .map(RankedStats::new)
                    .collect(Collectors.toList());
        }
    }

    public List<String> getRecentGames(String puuid, int gameCount) throws DataNotFoundException, IOException, JSONException, InterruptedException {
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(europeRoute)
                .path("lol", "match", "v5", "matches", "by-puuid")
                .path(puuid, "ids")
                .addQueryParameter("start", "0")
                .addQueryParameter("count", String.valueOf(gameCount))
                .addHeader("X-Riot-Token", riotApiKey)
                .get().build();
        try (HttpResponseWrapper<JSONArray> response = request.getAsJSONArray()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());
            return response.body().toList().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
    }

    public List<String> getRecentGamesByQueueType(String puuid, int gameCount, QueueType queueType)
            throws DataNotFoundException, IOException, JSONException, InterruptedException {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(europeRoute)
                .path("lol", "match", "v5", "matches", "by-puuid")
                .path(puuid, "ids")
                .addQueryParameter("start", "0")
                .addQueryParameter("count", String.valueOf(gameCount))
                .addHeader("X-Riot-Token", riotApiKey);

        if (queueType != null) {
            requestBuilder.addQueryParameter("queue", String.valueOf(queueType.getId()));
        }

        WrappedRequest request = requestBuilder.get().build();
        try (HttpResponseWrapper<JSONArray> response = request.getAsJSONArray()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());
            return response.body().toList().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
    }

    public MatchData getMatchData(String matchId) throws DataNotFoundException, IOException, JSONException, InterruptedException {
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(europeRoute)
                .path("lol", "match", "v5", "matches")
                .path(matchId)
                .addHeader("X-Riot-Token", riotApiKey)
                .get().build();
        try (HttpResponseWrapper<JSONObject> response = request.getAsJSONObject()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());
            return new MatchData(response.body().getJSONObject("info"));
        }
    }
}
