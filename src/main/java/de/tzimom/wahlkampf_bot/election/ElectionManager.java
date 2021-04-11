package de.tzimom.wahlkampf_bot.election;

import de.tzimom.wahlkampf_bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ElectionManager {

    private Bot bot = Bot.getInstance();
    private List<Election> elections = new ArrayList<>();

    public boolean createElection(TextChannel textChannel) {
        MessageEmbed embed = new EmbedBuilder().setColor(new Color(0xeccc68)).setTitle("Richter Wahl").setDescription
                ("Reagiere mit " + Candidate.CANDIDATE_EMOTE + ", wenn du dich zur Richter Wahl aufstellen möchtest").build();

        Message message = bot.sendMessageEmbed(textChannel, embed);

        if (message == null)
            return false;

        if (!bot.addReaction(message, Candidate.CANDIDATE_EMOTE_CODE))
            return false;

        elections.add(new Election(textChannel.getIdLong(), message.getIdLong()));
        return true;
    }

    public Election getElection(long textChannelId, long messageId) {
        for (Election election : elections) {
            if (election.getTextChannelId() == textChannelId && election.getMessageId() == messageId)
                return election;
        }

        return null;
    }

    public List<Election> getElections() {
        return elections;
    }

}
