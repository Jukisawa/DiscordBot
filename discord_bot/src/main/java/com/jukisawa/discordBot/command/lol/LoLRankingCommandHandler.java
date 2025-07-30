package com.jukisawa.discordBot.command.lol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.lol.ChampionMastery;
import com.jukisawa.discordBot.service.lol.RiotApiService;
import com.jukisawa.discordBot.util.CommandUtils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoLRankingCommandHandler implements CommandHandler {

    private final RiotApiService riotApi;
    private static final Logger logger = LoggerFactory.getLogger(LoLRankingCommandHandler.class);

    public LoLRankingCommandHandler(RiotApiService riotApi) {
        this.riotApi = riotApi;
    }

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        try {
            if (arguments.contains("?") || arguments.contains("help")) {
                event.getChannel()
                        .sendMessage(
                                "Der Commands geht folgendermaßen: #lolRanking 'Name#Tag' 'Name#Tag' 'Name#Tag'....")
                        .queue();
                return;
            }
            logger.info("Processing LoL Ranking command for: " + arguments);

            List<String> names = new ArrayList<>();
            Pattern pattern = Pattern.compile("([\\w ]{3,16}#[\\w]{3,5})");
            Matcher matcher = pattern.matcher(arguments);

            while (matcher.find()) {
                names.add(matcher.group(1));
            }

            Map<String, Integer> scoreMap = new HashMap<>();

            for (String name : names) {
                String puuid = riotApi.getPuuid(name);
                List<ChampionMastery> championMastery = riotApi.getChampionMasterys(puuid);
                int totalScore = 0;
                for (ChampionMastery mastery : championMastery) {
                    totalScore += (mastery.getChampionPoints() / (1000 + (mastery.getChampionLevel() * 250)))
                            + (mastery.getChampionLevel() * 75);
                }
                int summoner = riotApi.getSummonerLevel(puuid);
                totalScore += summoner / 10;

                scoreMap.put(name, totalScore);
            }

            StringBuilder message = new StringBuilder("Ranking:\n\n");
            scoreMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(
                            entry -> message.append(entry.getKey()).append(": ").append(CommandUtils.convertNumber(entry.getValue())).append("\n"));

            event.getChannel().sendMessage(message.toString()).queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("Ein Fehler ist aufgetreten: " + e.getMessage()).queue();
        }
    }

}
