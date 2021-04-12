package de.tzimom.wahlkampf_bot.election;

import de.tzimom.wahlkampf_bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class ElectionManager {

    private final Bot bot = Bot.getInstance();
    private Election currentElection;

    public boolean createElection(TextChannel textChannel) {
        if (currentElection != null) {
            Bot.LOGGER.error("There is already an ongoing election");
            return false;
        }

        MessageEmbed embed = new EmbedBuilder().setColor(Bot.EMBED_COLOR).setTitle("Richter Wahl").setDescription
                ("Reagiere mit " + Candidate.CANDIDATE_EMOTE + ", wenn du dich zur Richter Wahl aufstellen möchtest").build();

        Message message = bot.sendMessageEmbed(textChannel, embed);

        if (message == null)
            return false;

        if (!bot.addReaction(message, Candidate.CANDIDATE_EMOTE_CODE))
            return false;

        currentElection = new Election(textChannel.getIdLong(), message.getIdLong());
        return true;
    }

    public boolean finishElection() {
        Election election = currentElection;

        if (election == null) {
            Bot.LOGGER.error("There is no ongoing election");
            return false;
        }

        currentElection = null;
        election.finish();
        return true;
    }

    public Election getCurrentElection() {
        return currentElection;
    }

}
