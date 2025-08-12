package com.jukisawa.discordBot.event;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Event representing a Discord command that needs to be processed
 */
public record CommandEvent(
        String command,
        String arguments,
        boolean isSlash,
        boolean isButton,
        boolean isMessage,
        String userName,
        long userId,
        long guildId,
        Member member,
        Guild guild,
        MessageChannel channel,
        GenericEvent discordEvent) {
    public static CommandEvent from(String command, String arguments, GenericEvent event) {
        String userName = null;
        long userId = 0;
        long guildId = 0;
        Member member = null;
        Guild guild = null;
        MessageChannel channel = null;
        boolean isSlash = false;
        boolean isButton = false;
        boolean isMessage = false;

        if (event instanceof MessageReceivedEvent e) {
            userName = e.getAuthor().getName();
            userId = e.getAuthor().getIdLong();
            guildId = e.getGuild().getIdLong();
            guild = e.getGuild();
            member = e.getMember();
            channel = e.getChannel();
            isMessage = true;
        } else if (event instanceof SlashCommandInteractionEvent e) {
            userName = e.getUser().getName();
            userId = e.getUser().getIdLong();
            guildId = e.getGuild().getIdLong();
            member = e.getMember();
            guild = e.getGuild();
            channel = e.getChannel();
            isSlash = true;
        } else if (event instanceof ButtonInteractionEvent e) {
            userName = e.getUser().getName();
            userId = e.getUser().getIdLong();
            guildId = e.getGuild().getIdLong();
            member = e.getMember();
            guild = e.getGuild();
            channel = e.getChannel();
            isButton = true;
        } else {
            throw new IllegalArgumentException("Unsupported event type: " + event.getClass());
        }

        return new CommandEvent(
                command,
                arguments,
                isSlash,
                isButton,
                isMessage,
                userName,
                userId,
                guildId,
                member,
                guild,
                channel,
                event);
    }
}
