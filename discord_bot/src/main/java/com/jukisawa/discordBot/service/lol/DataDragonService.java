package com.jukisawa.discordBot.service.lol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jukisawa.discordBot.dto.lol.Champion;
import com.jukisawa.discordBot.dto.lol.Item;
import com.jukisawa.discordBot.dto.lol.SummonerSpell;
import com.jukisawa.discordBot.exceptions.DataNotFoundException;
import com.jukisawa.discordBot.http.HttpRequestBuilder;
import com.jukisawa.discordBot.http.HttpResponseWrapper;
import com.jukisawa.discordBot.http.RateLimitManager;
import com.jukisawa.discordBot.http.SimpleRateLimitManager;
import com.jukisawa.discordBot.http.WrappedRequest;

public class DataDragonService {

    static RateLimitManager rateLimitManager = new SimpleRateLimitManager();
    static String route = "ddragon.leagueoflegends.com";

    public static String getLatestVersion()
            throws DataNotFoundException, IOException, JSONException, InterruptedException {
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager).protocol("https")
                .host(route)
                .path("api", "versions.json")
                .get().build();
        try (HttpResponseWrapper<JSONArray> response = request.getAsJSONArray()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());
            return response.body().getString(0);
        }
    }

    public static List<Item> getItems(String version)
            throws DataNotFoundException, IOException, JSONException, InterruptedException {
        List<Item> items = new ArrayList<>();
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(route)
                .path("cdn", version, "data", "en_US", "item.json")
                .get().build();
        try (HttpResponseWrapper<JSONObject> response = request.getAsJSONObject()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());

            JSONObject data = response.body().getJSONObject("data");
            for (String key : data.keySet()) {
                JSONObject itemJson = data.getJSONObject(key);
                items.add(new Item(Integer.parseInt(key), itemJson.getString("name"),
                        itemJson.getJSONObject("gold").optInt("total")));
            }
            return items;
        }
    }

    public static List<Champion> getChampions(String version)
            throws DataNotFoundException, IOException, JSONException, InterruptedException {
        List<Champion> champions = new ArrayList<>();
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(route)
                .path("cdn", version, "data", "en_US", "champion.json")
                .get().build();
        try (HttpResponseWrapper<JSONObject> response = request.getAsJSONObject()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());

            JSONObject data = response.body().getJSONObject("data");
            for (String key : data.keySet()) {
                JSONObject championJson = data.getJSONObject(key);
                champions.add(new Champion(championJson.optInt("key"), championJson.getString("name")));
            }
            return champions;
        }
    }

    public static List<SummonerSpell> getSummonerSpells(String version)
            throws DataNotFoundException, IOException, JSONException, InterruptedException {
        List<SummonerSpell> summonerSpells = new ArrayList<>();
        WrappedRequest request = new HttpRequestBuilder(rateLimitManager)
                .protocol("https")
                .host(route)
                .path("cdn", version, "data", "en_US", "summoner.json")
                .get().build();
        try (HttpResponseWrapper<JSONObject> response = request.getAsJSONObject()) {
            if (response.statusCode() == 404)
                throw new DataNotFoundException(request.getUrl());

            JSONObject data = response.body().getJSONObject("data");
            for (String key : data.keySet()) {
                JSONObject spellJson = data.getJSONObject(key);
                summonerSpells.add(
                        new SummonerSpell(Integer.parseInt(spellJson.getString("key")), spellJson.getString("name")));
            }
            return summonerSpells;
        }
    }
}
