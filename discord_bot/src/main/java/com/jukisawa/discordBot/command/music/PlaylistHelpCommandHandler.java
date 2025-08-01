package com.jukisawa.discordBot.command.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukisawa.discordBot.command.CommandHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlaylistHelpCommandHandler implements CommandHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(PlaylistHelpCommandHandler.class);

    @Override
    public void handle(MessageReceivedEvent event, String arguments) {
        logger.info("Handling playlist help command");
        
        String helpText = "**Playlist Commands:**\n" +
                "`!playlist add <song>` - Add a song to the playlist\n" +
                "`!playlist remove <song>` - Remove a song from the playlist\n" +
                "`!playlist list` - Show the current playlist\n" +
                "`!playlist play` - Start playing the playlist\n" +
                "`!playlist pause` - Pause playback\n" +
                "`!playlist resume` - Resume playback\n" +
                "`!playlist stop` - Stop playback and disconnect\n" +
                "`!playlist skip` - Skip to the next song\n" +
                "`!playlist clear` - Clear the entire playlist\n" +
                "`!playlist shuffle` - Shuffle the playlist\n" +
                "`!playlist repeat` - Toggle repeat mode\n" +
                "`!playlist volume <0-100>` - Set the volume\n" +
                "`!playlist help` - Show this help message";
        
        event.getChannel().sendMessage(helpText).queue();
    }
}
