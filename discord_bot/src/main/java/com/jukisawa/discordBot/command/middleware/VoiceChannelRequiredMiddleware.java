package com.jukisawa.discordBot.command.middleware;

import com.jukisawa.discordBot.event.CommandEvent;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

/**
 * Middleware that requires user to be in a voice channel for music commands
 */
public class VoiceChannelRequiredMiddleware implements CommandMiddleware {

    @Override
    public void execute(CommandEvent event, Runnable next) {
        Member member = event.member();
        
        if (member == null) {
            event.channel()
                .sendMessage("❌ This command can only be used in a server.").queue();
            return;
        }

        AudioChannelUnion voiceChannel = member.getVoiceState() != null ? 
            member.getVoiceState().getChannel() : null;

        if (voiceChannel == null) {
            event.channel()
                .sendMessage("🔊 You need to be in a voice channel to use music commands.").queue();
            return;
        }

        next.run();
    }
}
