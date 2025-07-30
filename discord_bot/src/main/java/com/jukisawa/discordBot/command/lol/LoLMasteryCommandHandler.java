package com.jukisawa.discordBot.command.lol;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.cache.StaticDataCache;
import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.dto.lol.Champion;
import com.jukisawa.discordBot.dto.lol.ChampionMastery;
import com.jukisawa.discordBot.repository.LoLAccountRepository;
import com.jukisawa.discordBot.service.lol.RiotApiService;
import com.jukisawa.discordBot.util.CommandUtils;
import com.jukisawa.discordBot.util.TableImageRenderer;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class LoLMasteryCommandHandler implements CommandHandler {

    private final RiotApiService riotApi;
    private final LoLAccountRepository lolAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoLMasteryCommandHandler.class);

    public LoLMasteryCommandHandler(RiotApiService riotApi, LoLAccountRepository lolAccountRepository) {
        this.riotApi = riotApi;
        this.lolAccountRepository = lolAccountRepository;
    }

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        try {
            if (arguments.contains("?") || arguments.contains("help")) {
                event.getChannel().sendMessage("Der Commands geht folgendermaßen: #lolMastery 'Name#Tag'.").queue();
                return;
            }
            logger.info("Processing LoL Mastery command for: " + arguments);
            String user = CommandUtils.extractOrFetchRiotId(arguments, event, lolAccountRepository);
            String puuid = riotApi.getPuuid(user);
            List<ChampionMastery> championMasteries = riotApi.getChampionMasterys(puuid);

            Pair<List<String>, List<List<String>>> tableData = buildTableData(championMasteries);

            BufferedImage tableImg = TableImageRenderer.createTableImage(tableData.getLeft(), tableData.getRight());
            File tempFile = File.createTempFile("lolmastery_table", ".png");
            ImageIO.write(tableImg, "png", tempFile);

            FileUpload fileUpload = FileUpload.fromData(tempFile, "lolprofile.png");
            event.getChannel().sendFiles(Collections.singleton(fileUpload)).queue(msg -> tempFile.delete());

        } catch (IllegalArgumentException e) {
            logger.error("Invalid format for nameWithTag: ", e);
            event.getChannel().sendMessage("Bitte gib den Namen folgendermaßen an: 'Name#Tag'.").queue();
        } catch (Exception e) {
            logger.error("Error fetching PUUID: ", e);
        }
    }

    private Pair<List<String>, List<List<String>>> buildTableData(List<ChampionMastery> championMasteries) {
        List<String> headers = Arrays.asList("Champion", "Level", "Points", "Last Play", "Points Since Last Level",
                "Points Until Next Level", "Mark Required For Next Level", "Tokens Earned",
                "Season Milestone", "Milestone Grades");
        List<Champion> allChampions = StaticDataCache.getChampions();
        List<List<String>> rows = new ArrayList<>();
        for (ChampionMastery mastery : championMasteries) {

            String championName = allChampions.stream()
                    .filter(champ -> champ.id() == mastery.getChampionId())
                    .map(champ -> champ.name())
                    .findFirst()
                    .orElse("Unknown Champion");
            String lastPlayTimeStr = "-";

            if (mastery.getLastPlayTime() > 0) {
                java.time.Instant instant = java.time.Instant.ofEpochMilli(mastery.getLastPlayTime());
                java.time.LocalDate date = java.time.LocalDateTime
                        .ofInstant(instant, java.time.ZoneId.systemDefault()).toLocalDate();
                lastPlayTimeStr = date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            rows.add(Arrays.asList(
                    String.valueOf(championName),
                    String.valueOf(mastery.getChampionLevel()),
                    String.valueOf(CommandUtils.convertNumber(mastery.getChampionPoints())),
                    lastPlayTimeStr,
                    String.valueOf(CommandUtils.convertNumber(mastery.getChampionPointsSinceLastLevel())),
                    String.valueOf(CommandUtils.convertNumber(mastery.getChampionPointsUntilNextLevel())),
                    String.valueOf(mastery.getMarkRequiredForNextLevel()),
                    String.valueOf(mastery.getTokensEarned()),
                    String.valueOf(mastery.getChampionSeasonMilestone()),
                    String.join(", ", mastery.getMilestoneGrades())));
        }

        return Pair.of(headers, rows);
    }
}
