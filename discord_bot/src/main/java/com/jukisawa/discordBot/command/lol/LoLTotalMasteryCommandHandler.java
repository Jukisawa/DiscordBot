package com.jukisawa.discordBot.command.lol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.lol.ChampionMastery;
import com.jukisawa.discordBot.repository.LoLAccountRepository;
import com.jukisawa.discordBot.service.lol.RiotApiService;
import com.jukisawa.discordBot.util.CommandUtils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoLTotalMasteryCommandHandler implements CommandHandler {

    private final RiotApiService riotApiService;
    private final LoLAccountRepository lolAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoLTotalMasteryCommandHandler.class);

    public LoLTotalMasteryCommandHandler(RiotApiService riotApiService, LoLAccountRepository lolAccountRepository) {
        this.riotApiService = riotApiService;
        this.lolAccountRepository = lolAccountRepository;
    }

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        try {
            if (arguments.contains("?") || arguments.contains("help")) {
                event.getChannel().sendMessage("Der Commands geht folgendermaßen: #lolTotalMastery 'Name#Tag'.")
                        .queue();
                return;
            }
            String user = CommandUtils.extractOrFetchRiotId(arguments, event, lolAccountRepository);
            String puuid = riotApiService.getPuuid(user);
            List<ChampionMastery> masterys = riotApiService.getChampionMasterys(puuid);
            int totalMastery = 0;
            int totalLevel = 0;
            for (ChampionMastery mastery : masterys) {
                totalMastery += mastery.getChampionPoints();
                totalLevel += mastery.getChampionLevel();
            }
            String text = String.format(
                    "Total Champion Mastery für %s \nTotal Champion Mastery: %s \nTotal Champion Level: %s", arguments,
                    CommandUtils.convertNumber(totalMastery),
                    CommandUtils.convertNumber(totalLevel));
            event.getChannel().sendMessage(text).queue();
            ;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid format for nameWithTag: ", e);
            event.getChannel().sendMessage("Bitte gib den Namen folgendermaßen an: 'Name#Tag'.").queue();
        } catch (Exception e) {
            logger.error("Error fetching PUUID: ", e);
        }
    }

}
