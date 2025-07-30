package com.jukisawa.discordBot.command.lol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.entity.LoLAccount;
import com.jukisawa.discordBot.repository.LoLAccountRepository;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoLProfileCommandHandler implements CommandHandler {

    LoLAccountRepository repo = new LoLAccountRepository();
    private static final Logger logger = LoggerFactory.getLogger(LoLProfileCommandHandler.class);

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        try {
            if (arguments.contains("?") || arguments.contains("help")) {
                event.getChannel().sendMessage("Der Commands geht folgendermaßen: #lolMastery 'Name#Tag'.").queue();
                return;
            }

            String commandAddon = arguments.substring(0, arguments.indexOf(" "));
            String user = arguments.substring(commandAddon.length());
            String[] lolName = user.split("#");
            if (lolName.length != 2)
                throw new IllegalArgumentException("Format muss Name#Tag sein!");

            Long discordUserId = event.getAuthor().getIdLong();

            switch (commandAddon) {
                case "add":
                    addLoLAccountToDiscordUser(discordUserId, lolName[0], lolName[1]);
                    event.getChannel().sendMessage("LoL account linked!").queue();
                    break;
                case "remove":
                    removeLoLAccountFromDiscordUser(discordUserId, lolName[0], lolName[1]);
                    event.getChannel().sendMessage("LoL account unlinked!").queue();
                    break;
                case "list":
                    String accountList = getLoLAccountList(discordUserId);
                    if (accountList.isEmpty()) {
                        event.getChannel().sendMessage("No LoL accounts linked.").queue();
                    } else {
                        event.getChannel().sendMessage("Linked LoL accounts:\n" + accountList).queue();
                    }
                    break;
                default:
                    break;
            }

        } catch (IllegalArgumentException e) {
            logger.error("Invalid format for nameWithTag: " + e.getMessage());
            event.getChannel().sendMessage("Bitte gib den Namen folgendermaßen an: 'Name#Tag'.").queue();
        }

    }

    private void addLoLAccountToDiscordUser(Long discordUserId, String gameName, String tagLine) {
        LoLAccount account = new LoLAccount();
        account.setDiscordUserId(discordUserId);
        account.setGameName(gameName);
        account.setTagLine(tagLine);
        repo.save(account);
    }

    private void removeLoLAccountFromDiscordUser(Long discordUserId, String GameName, String TagLine) {
        repo.deleteByLoLName(discordUserId, GameName, TagLine);
    }

    private String getLoLAccountList(Long discordUserId) {
        StringBuilder sb = new StringBuilder();
        for (LoLAccount account : repo.findByDiscordUserId(discordUserId)) {
            sb.append(account.getGameName()).append("#").append(account.getTagLine()).append("\n");
        }
        return sb.toString();
    }

}
