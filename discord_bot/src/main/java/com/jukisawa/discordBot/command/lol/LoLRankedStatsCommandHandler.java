package com.jukisawa.discordBot.command.lol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.lol.RankedStats;
import com.jukisawa.discordBot.repository.LoLAccountRepository;
import com.jukisawa.discordBot.service.lol.RiotApiService;
import com.jukisawa.discordBot.util.CommandUtils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoLRankedStatsCommandHandler implements CommandHandler {

    private final RiotApiService riotApi;
    private final LoLAccountRepository lolAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoLRankedStatsCommandHandler.class);

    public LoLRankedStatsCommandHandler(RiotApiService riotApi, LoLAccountRepository lolAccountRepository) {
        this.riotApi = riotApi;
        this.lolAccountRepository = lolAccountRepository;
    }

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        try {
            if (arguments.contains("?") || arguments.contains("help")) {
                event.getChannel().sendMessage("Der Commands geht folgendermaßen: #lolRankedStats 'Name#Tag'.").queue();
                return;
            }

            logger.info("Processing LoL Ranked Stats command for: " + arguments);
            String user = CommandUtils.extractOrFetchRiotId(arguments, event, lolAccountRepository);
            String puuid = riotApi.getPuuid(user);
            List<RankedStats> rankedStats = riotApi.getRankedStats(puuid);
            StringBuilder message = new StringBuilder("Ranked Stats for " + arguments + ":\n\n");
            for (RankedStats stats : rankedStats) {
                message.append("QueueType: ").append(stats.getQueueType());
                message.append("\nTier: ").append(stats.getTier());
                message.append("\nRank: ").append(stats.getRank());
                message.append("\nLeague Points: ").append(stats.getLeaguePoints());
                message.append("\nGames: ").append(CommandUtils.convertNumber(stats.getWins() + stats.getLosses()));
                message.append("\nWins: ").append(CommandUtils.convertNumber(stats.getWins()));
                message.append("\nLosses: ").append(CommandUtils.convertNumber(stats.getLosses()));
                message.append("\nVeteran: ").append(stats.isVeteran());
                message.append("\nFresh Blood: ").append(stats.isFreshBlood());
                message.append("\nInactive: ").append(stats.isInactive());
                message.append("\nHot Streak: ").append(stats.isHotStreak());
                message.append("\n\n");
            }
            event.getChannel().sendMessage(message).queue();

        } catch (IllegalArgumentException e) {
            logger.error("Invalid format for nameWithTag: ", e);
            event.getChannel().sendMessage("Bitte gib den Name folgendermaßen an: 'Name#Tag'.").queue();
        } catch (Exception e) {
            logger.error("Error fetching PUUID: ", e);
        }
    }

}
