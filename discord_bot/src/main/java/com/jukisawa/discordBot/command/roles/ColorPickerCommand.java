package com.jukisawa.discordBot.command.roles;

import java.awt.Color;
import java.util.Map;

import com.jukisawa.discordBot.command.CommandHandler;
import com.jukisawa.discordBot.event.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ColorPickerCommand implements CommandHandler {

    private final Map<String, Color> colors = Map.of(
            "white", Color.WHITE,
            "black", Color.BLACK,
            "red", Color.RED,
            "blue", Color.BLUE,
            "brown", new Color(42, 86, 46),
            "purple", new Color(136, 108, 228),
            "green", Color.GREEN,
            "yellow", Color.YELLOW,
            "orange", Color.ORANGE);

    public void handleSlashCommand(SlashCommandInteractionEvent event) {

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("🎨 Color Picker")
                .setDescription("Choose a color using one of the buttons below:")
                .addField("Selected Color", "None yet", true)
                .setColor(Color.GRAY);

        event.replyEmbeds(eb.build())
                .addActionRow(
                        Button.primary("colorpick:white", "⚪ White"),
                        Button.primary("colorpick:black", "⚫ Black"),
                        Button.primary("colorpick:red", "🔴 Red"),
                        Button.primary("colorpick:blue", "🔵 Blue"),
                        Button.primary("colorpick:brown", "🟤 Brown"))
                .addActionRow(
                        Button.primary("colorpick:purple", "🟣 Purple"),
                        Button.primary("colorpick:green", "🟢 Green"),
                        Button.primary("colorpick:yellow", "🟡 Yellow"),
                        Button.primary("colorpick:orange", "🟠 Orange"))
                .addActionRow(
                        Button.secondary("colorpick:custom", "🎨 Custom Color"),
                        Button.secondary("colorpick:random", "🎲 Random Color"))
                .setEphemeral(true)
                .queue();
    }

    public void handleButtonInteraction(ButtonInteractionEvent event, String arguments) {
        String choice = arguments;
        Color chosenColor;

        switch (choice) {
            case "custom":
                event.reply("Please enter a hex code (like `#FF5733`):").setEphemeral(true).queue();
                return;
            case "random":
                chosenColor = new Color((int) (Math.random() * 0x1000000));
                break;
            default:
                chosenColor = colors.get(choice);
        }

        if (chosenColor != null) {
            var member = event.getMember();
            if (member != null && event.getGuild() != null) {
                var existingRole = member.getRoles().stream()
                        .filter(r -> r.getName().startsWith("color-"))
                        .findFirst()
                        .orElse(null);

                if (existingRole != null) {
                    existingRole.getManager().setColor(chosenColor).queue();
                } else {
                    event.getGuild().createRole()
                            .setName("color-" + member.getId())
                            .setColor(chosenColor)
                            .queue(role -> event.getGuild().addRoleToMember(member, role).queue());
                }
            }

            EmbedBuilder updated = new EmbedBuilder()
                    .setTitle("🎨 Color Picker")
                    .addField("Selected Color", String.format("#%06X", chosenColor.getRGB() & 0xFFFFFF), true)
                    .addField("RGB",
                            String.format("%d, %d, %d", chosenColor.getRed(), chosenColor.getGreen(),
                                    chosenColor.getBlue()),
                            true)
                    .setColor(chosenColor);

            event.editMessageEmbeds(updated.build()).queue();
        }
    }

    @Override
    public void handle(CommandEvent event, String arguments) {
        if (event.isSlash()) {
            handleSlashCommand((SlashCommandInteractionEvent) event.discordEvent());
        } else if (event.isButton()) {
            handleButtonInteraction((ButtonInteractionEvent) event.discordEvent(), event.arguments());
        }
    }

    @Override
    public String getDescription() {
        return "Wähle eine Frabe für dein Name";
    }
}
