package com.jukisawa.discordBot.bot;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jukisawa.discordBot.cache.StaticDataCache;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.command.lol.LoLItemsCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLMasteryCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLRankedStatsCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLRankingCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLShieldingCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLTotalMasteryCommandHandler;
import com.jukisawa.discordBot.command.music.PlaylistCommandHandler;
import com.jukisawa.discordBot.dto.lol.Champion;
import com.jukisawa.discordBot.dto.lol.Item;
import com.jukisawa.discordBot.dto.lol.SummonerSpell;
import com.jukisawa.discordBot.repository.LoLAccountRepository;
import com.jukisawa.discordBot.service.ConfigService;
import com.jukisawa.discordBot.service.TTSAnnouncer;
import com.jukisawa.discordBot.service.lol.DataDragonService;
import com.jukisawa.discordBot.service.lol.RiotApiService;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot extends ListenerAdapter {

    public static void main(String[] args) {
        RiotApiService riotApi = new RiotApiService();
        LoLAccountRepository lolAccountRepository = new LoLAccountRepository();
        Map<String, CommandHandler> handlers = new HashMap<>();

        handlers.put("lolMastery", new LoLMasteryCommandHandler(riotApi, lolAccountRepository));
        handlers.put("lolTotalMastery", new LoLTotalMasteryCommandHandler(riotApi, lolAccountRepository));
        handlers.put("lolRankedStats", new LoLRankedStatsCommandHandler(riotApi, lolAccountRepository));
        handlers.put("lolItems", new LoLItemsCommandHandler(riotApi, lolAccountRepository));
        handlers.put("lolShielding", new LoLShieldingCommandHandler(riotApi, lolAccountRepository));
        handlers.put("lolRanking", new LoLRankingCommandHandler(riotApi));
        handlers.put("playlist", new PlaylistCommandHandler());

        handlers.put("help", new CommandHandler() {
            @Override
            public void handle(MessageReceivedEvent event, String arguments) {
                event.getChannel().sendMessage("Available commands: " + String.join(", ", handlers.keySet())).queue();
            }
        });

        String token = ConfigService.loadConfig("discordToken");
        String commandPrefix = ConfigService.loadConfig("commandPrefix");

        MessageReceiveListener listener = new MessageReceiveListener(handlers, commandPrefix);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String latestVersion = DataDragonService.getLatestVersion();
                List<Champion> newChampions = DataDragonService.getChampions(latestVersion);
                List<Item> newItems = DataDragonService.getItems(latestVersion);
                List<SummonerSpell> newSummonerSpells = DataDragonService.getSummonerSpells(latestVersion);
                StaticDataCache.updateChampions(newChampions);
                StaticDataCache.updateItems(newItems);
                StaticDataCache.updateSummonerSpells(newSummonerSpells);
                System.out.println("Static data updated.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 4, TimeUnit.HOURS);

        JDA jda = JDABuilder.createDefault(token, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING))
                .addEventListeners(listener)
                .build();

        TTSAnnouncer ttsAnnouncer = new TTSAnnouncer(jda);
        ttsAnnouncer.start();
    }
}