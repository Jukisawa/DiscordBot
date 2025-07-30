package com.jukisawa.discordBot.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jukisawa.discordBot.dto.lol.LoLCommandInput;
import com.jukisawa.discordBot.dto.lol.QueueType;
import com.jukisawa.discordBot.exceptions.InputParseException;
import com.jukisawa.discordBot.repository.LoLAccountRepository;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUtils {

    private static Pattern LOL_COMMAND_PATTERN = Pattern.compile("^([A-Za-z0-9 ]{1,16}#[A-Za-z0-9]{3,5})?(?:\\s*(\\d+))?(?:\\s*(\\w+))?$");

    public static String extractOrFetchRiotId(String user, MessageReceivedEvent event,
            LoLAccountRepository repository) {
        if (user == null || user.trim().isEmpty()) {
            Long discordUserId = event.getAuthor().getIdLong();
            return repository.findByDiscordUserId(discordUserId).stream()
                    .findFirst()
                    .map(account -> account.getGameName() + "#" + account.getTagLine())
                    .orElse(null); // or throw an exception, log warning, etc.
        } else {
            return user.trim();
        }
    }

    public static LoLCommandInput parseLoLCommandInput(String arguments) {
        String user = null;
        int gameCount = 20;
        QueueType queueType = null;
        Matcher matcher = LOL_COMMAND_PATTERN.matcher(arguments);
        if (matcher.matches()) {
            user = matcher.group(1);
            if (matcher.group(2) != null) {
                try {
                    gameCount = Math.min(Integer.parseInt(matcher.group(2)), 100);
                } catch (NumberFormatException e) {
                    throw new InputParseException("Ungültige Anzahl an Spielen. Bitte eine Zahl eingeben.");
                }
            }
            if (matcher.group(3) != null) {
                queueType = QueueType.getByName(matcher.group(3));
                if (queueType == null) {
                    throw new InputParseException("Ungültiger Warteschlangentyp. Bitte einen gültigen Typ eingeben.");
                }
            }
        }
        return new LoLCommandInput(user, gameCount, queueType);
    }

    public static String convertNumber(int value) {
        DecimalFormat form = new DecimalFormat("#,###");
        form.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.GERMAN));
        return form.format(value);
    }

}
