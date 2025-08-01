package com.jukisawa.discordBot.command.middleware;

import java.util.Set;

import com.jukisawa.discordBot.event.CommandEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

/**
 * Middleware that checks if user has permission to execute admin commands
 */
public class AuthenticationMiddleware implements CommandMiddleware {
    private final Set<String> adminCommands = Set.of("admin", "config", "shutdown", "stop");

    @Override
    public void execute(CommandEvent event, Runnable next) {
        if (adminCommands.contains(event.command().toLowerCase())) {
            Member member = event.discordEvent().getMember();
            if (member != null && member.hasPermission(Permission.ADMINISTRATOR)) {
                next.run();
            } else {
                event.discordEvent().getChannel()
                    .sendMessage("❌ You don't have permission to use this command.").queue();
            }
        } else {
            next.run();
        }
    }
}
