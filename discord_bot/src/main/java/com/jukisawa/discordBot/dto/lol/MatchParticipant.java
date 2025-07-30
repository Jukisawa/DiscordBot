package com.jukisawa.discordBot.dto.lol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import lombok.Data;

@Data
public class MatchParticipant {
    private int champLevel;
    private int championId;

    private int allInPings;
    private int assistMePings;
    private int basicPings;
    private int commandPings;
    private int enemyMissingPings;
    private int enemyVisionPings;
    private int holdPings;
    private int getBackPings;
    private int needVisionPings;
    private int onMyWayPings;
    private int pushPings;
    private int visionClearedPings;

    private int kills;
    private int deaths;
    private int assists;
    private int doubleKills;
    private int tripleKills;
    private int quadraKills;
    private int pentakills;
    private int unrealKills;
    private int killingSprees;
    private int largestKillingSpree;
    private int largestMultiKill;
    private int firstBloodAssist;
    private int firstBloodKill;

    private int baronKills;
    private int dragonKills;
    private int nexusKills;
    private int nexusTakedowns;
    private int nexusLost;
    private int inhibitorKills;
    private int inhibitorTakedowns;
    private int inhibitorsLost;
    private int turretKills;
    private int turretTakedowns;
    private int turretsLost;
    private int damageDealtToBuildings;
    private int damageDealtToObjectives;
    private int damageDealtToTurrets;
    private int firstTowerAssist;
    private int firstTowerKill;
    private int objectivesStolen;
    private int objectivesStolenAssists;

    private int magicDamageDealt;
    private int magicDamageDealtToChampions;
    private int magicDamageTaken;
    private int physicalDamageDealt;
    private int physicalDamageDealtToChampions;
    private int physicalDamageTaken;
    private int trueDamageDealt;
    private int trueDamageDealtToChampions;
    private int trueDamageTaken;
    private int totalDamageDealt;
    private int totalDamageDealtToChampions;
    private int totalDamageTaken;
    private int totalDamageShieldedOnTeammates;
    private int totalHeal;
    private int totalHealsOnTeammates;
    private int totalUnitsHealed;
    private int damageSelfMitigated;
    private int largestCriticalStrike;
    private int longestTimeSpentLiving;
    private int totalTimeCCDealt;
    private int totalTimeSpentDead;
    private int timeCCingOthers;
    private int timePlayed;

    private int consumablesPurchased;
    private int goldEarned;
    private int goldSpent;
    private List<Integer> items;
    private int itemsPurchased;

    private int detectorWardsPlaced;
    private int visionScore;
    private int visionWardsBoughtInGame;
    private int wardsKilled;
    private int wardsPlaced;

    private boolean gameEndedInEarlySurrender;
    private boolean gameEndedInSurrender;
    private String teamPosition;
    private boolean win;

    private int neutralMinionsKilled;
    private int totalAllyJungleMinionsKilled;
    private int totalEnemyJungleMinionsKilled;
    private int totalMinionsKilled;

    private int spell1Casts;
    private int spell2Casts;
    private int spell3Casts;
    private int spell4Casts;
    private int summoner1Casts;
    private int summoner1Id;
    private int summoner2Casts;
    private int summoner2Id;

    public MatchParticipant(JSONObject participant) {
        this.champLevel = participant.optInt("champLevel", 1);
        this.championId = participant.optInt("championId", 0);

        this.allInPings = participant.optInt("allInPings", 0);
        this.assistMePings = participant.optInt("assistMePings", 0);
        this.basicPings = participant.optInt("basicPings", 0);
        this.commandPings = participant.optInt("commandPings", 0);
        this.enemyMissingPings = participant.optInt("enemyMissingPings", 0);
        this.enemyVisionPings = participant.optInt("enemyVisionPings", 0);
        this.holdPings = participant.optInt("holdPings", 0);
        this.getBackPings = participant.optInt("getBackPings", 0);
        this.needVisionPings = participant.optInt("needVisionPings", 0);
        this.onMyWayPings = participant.optInt("onMyWayPings", 0);
        this.pushPings = participant.optInt("pushPings", 0);
        this.visionClearedPings = participant.optInt("visionClearedPings", 0);
        this.kills = participant.optInt("kills", 0);
        this.deaths = participant.optInt("deaths", 0);
        this.assists = participant.optInt("assists", 0);
        this.doubleKills = participant.optInt("doubleKills", 0);
        this.tripleKills = participant.optInt("tripleKills", 0);
        this.quadraKills = participant.optInt("quadraKills", 0);
        this.pentakills = participant.optInt("pentakills", 0);
        this.unrealKills = participant.optInt("unrealKills", 0);
        this.killingSprees = participant.optInt("killingSprees", 0);
        this.largestKillingSpree = participant.optInt("largestKillingSpree", 0);
        this.largestMultiKill = participant.optInt("largestMultiKill", 0);
        this.firstBloodAssist = participant.optInt("firstBloodAssist", 0);
        this.firstBloodKill = participant.optInt("firstBloodKill", 0);
        this.baronKills = participant.optInt("baronKills", 0);
        this.dragonKills = participant.optInt("dragonKills", 0);
        this.nexusKills = participant.optInt("nexusKills", 0);
        this.nexusTakedowns = participant.optInt("nexusTakedowns", 0);
        this.nexusLost = participant.optInt("nexusLost", 0);
        this.inhibitorKills = participant.optInt("inhibitorKills", 0);
        this.inhibitorTakedowns = participant.optInt("inhibitorTakedowns", 0);
        this.inhibitorsLost = participant.optInt("inhibitorsLost", 0);
        this.turretKills = participant.optInt("turretKills", 0);
        this.turretTakedowns = participant.optInt("turretTakedowns", 0);
        this.turretsLost = participant.optInt("turretsLost", 0);
        this.damageDealtToBuildings = participant.optInt("damageDealtToBuildings", 0);
        this.damageDealtToObjectives = participant.optInt("damageDealtToObjectives", 0);
        this.damageDealtToTurrets = participant.optInt("damageDealtToTurrets", 0);
        this.firstTowerAssist = participant.optInt("firstTowerAssist", 0);
        this.firstTowerKill = participant.optInt("firstTowerKill", 0);
        this.objectivesStolen = participant.optInt("objectivesStolen", 0);
        this.objectivesStolenAssists = participant.optInt("objectivesStolenAssists", 0);
        this.magicDamageDealt = participant.optInt("magicDamageDealt", 0);
        this.magicDamageDealtToChampions = participant.optInt("magicDamageDealtToChampions", 0);
        this.magicDamageTaken = participant.optInt("magicDamageTaken", 0);
        this.physicalDamageDealt = participant.optInt("physicalDamageDealt", 0);
        this.physicalDamageDealtToChampions = participant.optInt("physicalDamageDealtToChampions", 0);
        this.physicalDamageTaken = participant.optInt("physicalDamageTaken", 0);
        this.trueDamageDealt = participant.optInt("trueDamageDealt", 0);
        this.trueDamageDealtToChampions = participant.optInt("trueDamageDealtToChampions", 0);
        this.trueDamageTaken = participant.optInt("trueDamageTaken", 0);
        this.totalDamageDealt = participant.optInt("totalDamageDealt", 0);
        this.totalDamageDealtToChampions = participant.optInt("totalDamageDealtToChampions", 0);
        this.totalDamageTaken = participant.optInt("totalDamageTaken", 0);
        this.totalDamageShieldedOnTeammates = participant.optInt("totalDamageShieldedOnTeammates", 0);
        this.totalHeal = participant.optInt("totalHeal", 0);
        this.totalHealsOnTeammates = participant.optInt("totalHealsOnTeammates", 0);
        this.totalUnitsHealed = participant.optInt("totalUnitsHealed", 0);
        this.damageSelfMitigated = participant.optInt("damageSelfMitigated", 0);
        this.largestCriticalStrike = participant.optInt("largestCriticalStrike", 0);
        this.longestTimeSpentLiving = participant.optInt("longestTimeSpentLiving", 0);
        this.totalTimeCCDealt = participant.optInt("totalTimeCCDealt", 0);
        this.totalTimeSpentDead = participant.optInt("totalTimeSpentDead", 0);
        this.consumablesPurchased = participant.optInt("consumablesPurchased", 0);
        this.goldEarned = participant.optInt("goldEarned", 0);
        this.goldSpent = participant.optInt("goldSpent", 0);
        this.items = new ArrayList<>(Arrays.asList(
                participant.optInt("item0"),
                participant.optInt("item1"),
                participant.optInt("item2"),
                participant.optInt("item3"),
                participant.optInt("item4"),
                participant.optInt("item5"),
                participant.optInt("item6")));
        this.itemsPurchased = participant.optInt("itemsPurchased", 0);
        this.detectorWardsPlaced = participant.optInt("detectorWardsPlaced", 0);
        this.visionScore = participant.optInt("visionScore", 0);
        this.visionWardsBoughtInGame = participant.optInt("visionWardsBoughtInGame", 0);
        this.wardsKilled = participant.optInt("wardsKilled", 0);
        this.wardsPlaced = participant.optInt("wardsPlaced", 0);
        this.gameEndedInEarlySurrender = participant.optBoolean("gameEndedInEarlySurrender", false);
        this.gameEndedInSurrender = participant.optBoolean("gameEndedInSurrender", false);
        this.teamPosition = participant.optString("teamPosition", "Unknown");
        this.win = participant.optBoolean("win", false);
        this.neutralMinionsKilled = participant.optInt("neutralMinionsKilled", 0);
        this.totalAllyJungleMinionsKilled = participant.optInt("totalAllyJungleMinionsKilled", 0);
        this.totalEnemyJungleMinionsKilled = participant.optInt("totalEnemyJungleMinionsKilled", 0);
        this.totalMinionsKilled = participant.optInt("totalMinionsKilled", 0);
        this.spell1Casts = participant.optInt("spell1Casts", 0);
        this.spell2Casts = participant.optInt("spell2Casts", 0);
        this.spell3Casts = participant.optInt("spell3Casts", 0);
        this.spell4Casts = participant.optInt("spell4Casts", 0);
        this.summoner1Casts = participant.optInt("summoner1Casts", 0);
        this.summoner1Id = participant.optInt("summoner1Id", 0);
        this.summoner2Casts = participant.optInt("summoner2Casts", 0);
        this.summoner2Id = participant.optInt("summoner2Id", 0);
        this.timeCCingOthers = participant.optInt("timeCCingOthers", 0);
        this.timePlayed = participant.optInt("timePlayed", 0);

    }
}
