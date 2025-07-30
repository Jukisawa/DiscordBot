package com.jukisawa.discordBot.cache;

import java.util.Collections;
import java.util.List;

import com.jukisawa.discordBot.dto.lol.Champion;
import com.jukisawa.discordBot.dto.lol.Item;
import com.jukisawa.discordBot.dto.lol.SummonerSpell;

public class StaticDataCache {

    private static List<Champion> champions = Collections.emptyList();
    private static List<Item> items = Collections.emptyList();
    private static List<SummonerSpell> summonerSpells = Collections.emptyList();

    private StaticDataCache() {} // Prevent instantiation

    public static synchronized void updateChampions(List<Champion> newChampions) {
        champions = List.copyOf(newChampions);
    }

    public static synchronized void updateItems(List<Item> newItems) {
        items = List.copyOf(newItems);
    }

    public static synchronized void updateSummonerSpells(List<SummonerSpell> newSummonerSpells) {
        summonerSpells = List.copyOf(newSummonerSpells);
    }

    public static List<Champion> getChampions() {
        return champions;
    }

    public static List<Item> getItems() {
        return items;
    }

    public static List<SummonerSpell> getSummonerSpells() {
        return summonerSpells;
    }
}
