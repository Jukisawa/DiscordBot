package com.jukisawa.discordBot.bot;

import java.util.Map;

import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceiveListener extends ListenerAdapter {
  private final Map<String, CommandHandler> commandHandlers;
  private final String commandPrefix;
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageReceiveListener.class);

  public MessageReceiveListener(Map<String, CommandHandler> commandHandlers, String commandPrefix) {
    this.commandHandlers = commandHandlers;
    this.commandPrefix = commandPrefix;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    String message = event.getMessage().getContentRaw();
    logger.info("Received message: " + message);
    if (event.getAuthor().isBot() || !event.isFromGuild()) {
      return;
    }

    if (!message.startsWith(commandPrefix)) {
      return;
    }

    String withoutPrefix = message.substring(commandPrefix.length()).trim();
    String command = getCommand(withoutPrefix);
    String arguments = withoutPrefix.substring(command.length()).trim();

    CommandHandler handler = commandHandlers.get(command);
    if (handler != null) {
      handler.handle(event, arguments);
    } else {
      logger.info("Unknown command: " + command);
    }

  }

  private String getCommand(String message) {
    int spaceIndex = message.indexOf(' ');
    if (spaceIndex == -1) {
      return message;
    }
    return message.substring(0, spaceIndex);
  }

}