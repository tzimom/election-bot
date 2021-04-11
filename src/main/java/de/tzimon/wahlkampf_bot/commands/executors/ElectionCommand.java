package de.tzimon.wahlkampf_bot.commands.executors;

import de.tzimon.wahlkampf_bot.Bot;
import de.tzimon.wahlkampf_bot.commands.CommandExecutor;
import net.dv8tion.jda.api.entities.*;

public class ElectionCommand implements CommandExecutor {

    private Bot bot = Bot.getInstance();

    public void execute(String[] args) {
        if (args.length != 1)
            return;

        long channelId;

        try {
            channelId = Long.parseLong(args[0]);
        } catch (NumberFormatException ignored) {
            Bot.LOGGER.error("Invalid number");
            return;
        }

        GuildChannel channel = bot.getJda().getGuildChannelById(channelId);

        if (channel == null) {
            Bot.LOGGER.error("Invalid channel id");
            return;
        }

        if (channel.getType() != ChannelType.TEXT)
            return;

        TextChannel textChannel = (TextChannel) channel;

        if (bot.getElectionManager().createElection(textChannel))
            Bot.LOGGER.info("Sent election message into #" + textChannel.getName());
    }

}
