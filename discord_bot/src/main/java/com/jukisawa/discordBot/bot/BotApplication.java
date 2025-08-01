package com.jukisawa.discordBot.bot;

import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.cache.StaticCacheRefresher;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.command.CommandRegistry;
import com.jukisawa.discordBot.command.lol.LoLItemsCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLMasteryCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLRankedStatsCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLRankingCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLShieldingCommandHandler;
import com.jukisawa.discordBot.command.lol.LoLTotalMasteryCommandHandler;
import com.jukisawa.discordBot.command.music.PlaylistCommandRegistry;
import com.jukisawa.discordBot.command.middleware.AuthenticationMiddleware;
import com.jukisawa.discordBot.command.middleware.LoggingMiddleware;
import com.jukisawa.discordBot.command.middleware.RateLimitMiddleware;
import com.jukisawa.discordBot.command.middleware.VoiceChannelRequiredMiddleware;
import com.jukisawa.discordBot.event.CommandEvent;
import com.jukisawa.discordBot.event.EventBus;
import com.jukisawa.discordBot.exceptions.DataNotFoundException;
import com.jukisawa.discordBot.repository.LoLAccountRepository;
import com.jukisawa.discordBot.service.ConfigService;
import com.jukisawa.discordBot.service.TTSAnnouncer;
import com.jukisawa.discordBot.service.lol.RiotApiService;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * Main application class that sets up the event-driven Discord bot
 */
public class BotApplication {
    private static final Logger logger = LoggerFactory.getLogger(BotApplication.class);
    
    private EventBus eventBus;
    private ScheduledExecutorService scheduler;

    public void start() {
        logger.info("Starting Discord Bot with event-driven architecture...");
        
        try {
            // Initialize core components
            eventBus = new EventBus();
            CommandRegistry commandRegistry = new CommandRegistry();
            scheduler = Executors.newScheduledThreadPool(2);

            // Setup middleware
            setupMiddleware(commandRegistry);

            // Setup services
            RiotApiService riotApi = new RiotApiService();
            LoLAccountRepository lolRepo = new LoLAccountRepository();

            // Register commands
            registerCommands(commandRegistry, riotApi, lolRepo);

            // Setup event handlers
            setupEventHandlers(eventBus, commandRegistry);

            // Setup periodic tasks
            setupPeriodicTasks();

            // Start Discord bot
            startDiscordBot(eventBus);

            logger.info("Discord Bot started successfully!");

        } catch (Exception e) {
            logger.error("Failed to start Discord Bot: ", e);
            throw new RuntimeException("Bot startup failed", e);
        }
    }

    private void setupMiddleware(CommandRegistry commandRegistry) {
        commandRegistry.addGlobalMiddleware(
            new LoggingMiddleware(),
            new RateLimitMiddleware(1), // 1 second rate limit
            new AuthenticationMiddleware()
        );
        logger.info("Global middleware configured");
    }

    private void registerCommands(CommandRegistry commandRegistry, RiotApiService riotApi, LoLAccountRepository lolRepo) {
        // LoL commands
        commandRegistry.registerCommand("lolMastery", 
            new LoLMasteryCommandHandler(riotApi, lolRepo));
        
        commandRegistry.registerCommand("lolTotalMastery", 
            new LoLTotalMasteryCommandHandler(riotApi, lolRepo));
        
        commandRegistry.registerCommand("lolRankedStats", 
            new LoLRankedStatsCommandHandler(riotApi, lolRepo));
        
        commandRegistry.registerCommand("lolItems", 
            new LoLItemsCommandHandler(riotApi, lolRepo));
        
        commandRegistry.registerCommand("lolShielding", 
            new LoLShieldingCommandHandler(riotApi, lolRepo));
        
        commandRegistry.registerCommand("lolRanking", 
            new LoLRankingCommandHandler(riotApi));

        // Playlist commands using nested command registry (keeps original command format)
        commandRegistry.registerCommand("playlist", 
            new PlaylistCommandRegistry(), 
            new VoiceChannelRequiredMiddleware());

        // Help command
        commandRegistry.registerCommand("help", new CommandHandler() {
            @Override
            public void handle(net.dv8tion.jda.api.events.message.MessageReceivedEvent event, String arguments) {
                event.getChannel().sendMessage("Available commands: " + 
                    String.join(", ", commandRegistry.getCommandNames())).queue();
            }
        });

        logger.info("Registered {} commands", commandRegistry.getCommandNames().size());
    }

    private void setupEventHandlers(EventBus eventBus, CommandRegistry commandRegistry) {
        eventBus.register(CommandEvent.class, event -> 
            commandRegistry.executeCommand(event));

        logger.info("Event handlers configured");
    }

    private void setupPeriodicTasks() {
        // Update static data every 4 hours
        scheduler.scheduleAtFixedRate(() -> {
            try {
                StaticCacheRefresher.refreshCache();
            } catch (DataNotFoundException | JSONException | IOException | InterruptedException e) {
                logger.error("Error refreshing static cache: ", e);
            }
        }, 0, 4, TimeUnit.HOURS);
    }

    private void startDiscordBot(EventBus eventBus) throws Exception {
        String token = ConfigService.loadConfig("discordToken");
        String commandPrefix = ConfigService.loadConfig("commandPrefix");

        JDA jda = JDABuilder.createDefault(token, EnumSet.of(
                GatewayIntent.GUILD_MESSAGES, 
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_VOICE_STATES, 
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES, 
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_TYPING))
            .addEventListeners(new DiscordEventListener(eventBus, commandPrefix))
            .build();

        // Wait for JDA to be ready
        jda.awaitReady();

        // Start TTS announcer
        TTSAnnouncer ttsAnnouncer = new TTSAnnouncer(jda);
        ttsAnnouncer.start();

        logger.info("Discord JDA initialized and ready");
    }

    public void shutdown() {
        logger.info("Shutting down Discord Bot...");
        
        if (eventBus != null) {
            eventBus.shutdown();
        }
        
        if (scheduler != null) {
            scheduler.shutdown();
        }
        
        logger.info("Discord Bot shutdown completed");
    }

    public static void main(String[] args) {
        BotApplication app = new BotApplication();
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(app::shutdown));
        
        app.start();
    }
}
