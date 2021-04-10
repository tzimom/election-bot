package de.tzimon.wahlkampf_bot.commands.executors;

import de.tzimon.wahlkampf_bot.Bot;
import de.tzimon.wahlkampf_bot.commands.CommandExecutor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.requests.Route;

import java.awt.*;

public class ElectionCommand implements CommandExecutor {

    private Bot bot = Bot.getInstance();

    public void execute(String[] args) {
        if (args.length != 1)
            return;

        GuildChannel channel = bot.getJda().getGuildChannelById(args[0]);

        if (channel == null) {
            Bot.LOGGER.error("Invalid channel id");
            return;
        }

        if (channel.getType() != ChannelType.TEXT)
            return;

        TextChannel textChannel = (TextChannel) channel;
        MessageEmbed embed = new EmbedBuilder().setColor(new Color(0xeccc68)).setTitle("Richter Wahl")
                .setDescription("Reagiere mit :person_raising_hand:, wenn du dich zur Richter Wahl aufstellen möchtest")
                .build();

        Message message = bot.sendMessageEmbed(textChannel, embed);

        if (message == null)
            return;

        bot.addReaction(message, "U+1F64B");
    }

}
