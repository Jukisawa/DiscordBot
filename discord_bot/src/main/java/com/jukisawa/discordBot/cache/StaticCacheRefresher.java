package com.jukisawa.discordBot.cache;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;

import com.jukisawa.discordBot.dto.lol.Champion;
import com.jukisawa.discordBot.dto.lol.Item;
import com.jukisawa.discordBot.dto.lol.SummonerSpell;
import com.jukisawa.discordBot.exceptions.DataNotFoundException;
import com.jukisawa.discordBot.service.lol.DataDragonService;

public class StaticCacheRefresher {

    /**
     * Refresh the static data cache with new data.
     * This method should be called periodically to ensure the cache is up-to-date.
     * @throws InterruptedException 
     * @throws IOException 
     * @throws JSONException 
     * @throws DataNotFoundException 
     */
    public static void refreshCache() throws DataNotFoundException, JSONException, IOException, InterruptedException {
        // Logic to fetch new data and update the cache
        // For example, fetching champions, items, and summoner spells from an API
        String latestVersion = DataDragonService.getLatestVersion();
        List<Champion> newChampions = DataDragonService.getChampions(latestVersion);
        List<Item> newItems = DataDragonService.getItems(latestVersion);
        List<SummonerSpell> newSummonerSpells = DataDragonService.getSummonerSpells(latestVersion);


        StaticDataCache.updateChampions(newChampions);
        StaticDataCache.updateItems(newItems);
        StaticDataCache.updateSummonerSpells(newSummonerSpells);
    }

    
}
