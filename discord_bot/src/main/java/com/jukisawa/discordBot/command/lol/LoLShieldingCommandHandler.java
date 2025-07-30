package com.jukisawa.discordBot.command.lol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.cache.StaticDataCache;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.lol.Champion;
import com.jukisawa.discordBot.dto.lol.LoLCommandInput;
import com.jukisawa.discordBot.dto.lol.MatchData;
import com.jukisawa.discordBot.dto.lol.MatchParticipant;
import com.jukisawa.discordBot.repository.LoLAccountRepository;
import com.jukisawa.discordBot.service.lol.RiotApiService;
import com.jukisawa.discordBot.util.CommandUtils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoLShieldingCommandHandler implements CommandHandler {

  private final RiotApiService riotApi;
  private final LoLAccountRepository lolAccountRepository;
  private static final Logger logger = LoggerFactory.getLogger(LoLShieldingCommandHandler.class);

  public LoLShieldingCommandHandler(RiotApiService riotApi, LoLAccountRepository lolAccountRepository) {
    this.riotApi = riotApi;
    this.lolAccountRepository = lolAccountRepository;
  }

  @Override
  public void handle(MessageReceivedEvent event, String arguments) {
    try {

      if (arguments.contains("?") || arguments.contains("help")) {
        event.getChannel().sendMessage(
            "Der Commands geht folgendermaßen #lolShielding 'Name#Tag' 'Anzahl an Spiele die bewertet werden sollen' 'Der Warteschlangen typ Bsp. Solo'")
            .queue();
        return;
      }

      logger.info("Processing LoL Shielding command");

      LoLCommandInput commandInput = CommandUtils.parseLoLCommandInput(arguments, event);
      commandInput.setRiotId(CommandUtils.extractOrFetchRiotId(commandInput.getRiotId(), event, lolAccountRepository));
      String puuid = riotApi.getPuuid(commandInput.getRiotId());
      List<String> games = riotApi.getRecentGamesByQueueType(puuid, commandInput.getGameCount(),
          commandInput.getQueueType());
      List<Champion> allChampions = StaticDataCache.getChampions();
      StringBuilder response = new StringBuilder("Shielding Stats:");
      for (String matchId : games) {
        response.append("\n\nGame ").append(matchId).append(":");
        MatchData matchData = riotApi.getMatchData(matchId);
        for (MatchParticipant participant : matchData.getParticipants()) {
          String champion = allChampions.stream()
              .filter(champ -> champ.getId() == participant.getChampionId())
              .map(champ -> champ.getName())
              .findFirst()
              .orElse("Unknown Champion");
          if (champion != "Unknown Champion") {
            int totalShielding = participant.getTotalDamageShieldedOnTeammates();
            response.append("\n").append(champion);
            response.append("\tShielding: ").append(CommandUtils.convertNumber(totalShielding));
          }
        }
      }
      event.getChannel().sendMessage(response.toString()).queue();
    } catch (Exception e) {
      logger.error("Error fetching items: ", e);
    }
  }

}
