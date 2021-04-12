package de.tzimom.wahlkampf_bot.commands.executors;

import de.tzimom.wahlkampf_bot.commands.CommandExecutor;
import de.tzimom.wahlkampf_bot.Bot;
import de.tzimom.wahlkampf_bot.election.Election;
import net.dv8tion.jda.api.entities.*;

public class ElectionCommand implements CommandExecutor {

    private final Bot bot = Bot.getInstance();

    public void execute(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("finish")) {
            if (bot.getElectionManager().finishElection())
                Bot.LOGGER.info("Election finished");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            long channelId;

            try {
                channelId = Long.parseLong(args[1]);
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
                Bot.LOGGER.info("Sent election message into #" + textChannel.getName() + "@" + textChannel.getGuild().getName());
        } else
            sendUsage();
    }

    private void sendUsage() {
        Bot.LOGGER.error("Usage: election [create <TextChannelID>]|finish");
    }

}
