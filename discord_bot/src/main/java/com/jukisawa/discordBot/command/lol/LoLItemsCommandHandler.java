package com.jukisawa.discordBot.command.lol;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.cache.StaticDataCache;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.lol.Item;
import com.jukisawa.discordBot.dto.lol.LoLCommandInput;
import com.jukisawa.discordBot.dto.lol.MatchData;
import com.jukisawa.discordBot.dto.lol.MatchParticipant;
import com.jukisawa.discordBot.event.CommandEvent;
import com.jukisawa.discordBot.exceptions.InputParseException;
import com.jukisawa.discordBot.repository.LoLAccountRepository;
import com.jukisawa.discordBot.service.lol.RiotApiService;
import com.jukisawa.discordBot.util.CommandUtils;

import net.dv8tion.jda.api.utils.FileUpload;

public class LoLItemsCommandHandler implements CommandHandler {

  private final RiotApiService riotApi;
  private final LoLAccountRepository lolAccountRepository;
  private static final Logger logger = LoggerFactory.getLogger(LoLItemsCommandHandler.class);

  public LoLItemsCommandHandler(RiotApiService riotApi, LoLAccountRepository lolAccountRepository) {
    this.riotApi = riotApi;
    this.lolAccountRepository = lolAccountRepository;
  }

  @Override
  public void handle(CommandEvent event, String arguments) {
    try {
      if (arguments.contains("?") || arguments.contains("help")) {
        event.channel().sendMessage(
            "Der Commands geht folgendermaßen #lolItems 'Name#Tag' 'Anzahl an Spiele die bewertet werden sollen' 'Der Warteschlangen typ Bsp. Solo'")
            .queue();
        return;
      }

      logger.info("Processing LoL Items command");

      LoLCommandInput commandInput = CommandUtils.parseLoLCommandInput(arguments);

      commandInput.setRiotId(CommandUtils.extractOrFetchRiotId(commandInput.getRiotId(), event, lolAccountRepository));

      List<Item> allItems = StaticDataCache.getItems();
      Map<Item, Integer> itemUsage = new HashMap<>();
      String puuid = riotApi.getPuuid(commandInput.getRiotId());
      List<String> games = riotApi.getRecentGamesByQueueType(puuid, commandInput.getGameCount(),
          commandInput.getQueueType());
      for (String matchId : games) {
        MatchData matchData = riotApi.getMatchData(matchId);
        for (MatchParticipant participant : matchData.getParticipants()) {
          for (int itemId : participant.items()) {
            if (itemId > 0) {
              Item item = allItems.stream().filter(it -> it.id() == itemId).findFirst().orElse(null);
              if (item != null) {
                itemUsage.put(item, itemUsage.getOrDefault(item, 0) + 1);
              }
            }
          }
        }
      }

      List<Map.Entry<Item, Integer>> sortedItems = new ArrayList<>(itemUsage.entrySet());
      sortedItems.sort((a, b) -> b.getValue().compareTo(a.getValue()));

      // Schreibe CSV-Datei
      File tempFile = File.createTempFile("item_usage", ".csv");
      try (java.io.PrintWriter writer = new java.io.PrintWriter(tempFile, "UTF-8")) {
        writer.println("ItemName,UsageCount");
        for (Map.Entry<Item, Integer> entry : sortedItems) {
          writer.println("\"" + entry.getKey().name().replace("\"", "\"\"") + "\"," + entry.getValue());
        }
      }

      FileUpload fileUpload = FileUpload.fromData(tempFile, "item_usage.csv");
      event.channel().sendFiles(Collections.singleton(fileUpload)).queue(
          (msg) -> tempFile.delete());
    } catch (InputParseException e) {
      event.channel().sendMessage(e.getMessage()).queue();
    } catch (Exception e) {
      logger.error("Error fetching items: ", e);
    }
  }

  @Override
  public String getDescription() {
    return "Liste von meist gekauften Items in den letzten Games";
  }

}
