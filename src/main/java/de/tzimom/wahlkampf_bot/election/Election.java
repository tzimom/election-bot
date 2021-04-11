package de.tzimom.wahlkampf_bot.election;

import de.tzimom.wahlkampf_bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Election {

    public static final boolean SELF_VOTES = true;

    private Bot bot = Bot.getInstance();

    private final long textChannelId;
    private final long messageId;
    private final List<Candidate> candidates = new ArrayList<>();

    public Election(long textChannelId, long messageId) {
        this.textChannelId = textChannelId;
        this.messageId = messageId;
    }

    public void createCandidate(TextChannel textChannel, long userId) {
        MessageEmbed embed = new EmbedBuilder().setColor(new Color(0xeccc68)).setTitle("Kandidatur").setDescription
                ("Reagiere mit " + Candidate.VOTE_EMOTE + ", um für <@" + userId + "> zu stimmen").build();

        Message message = bot.sendMessageEmbed(textChannel, embed);

        if (message == null)
            return;

        if (!bot.addReaction(message, Candidate.VOTE_EMOTE_CODE))
            return;

        candidates.add(new Candidate(this, userId, message));
    }

    public void removeCandidate(long userId) {
        for (Candidate candidate : candidates) {
            if (candidate.getUserId() == userId)
                removeCandidate(candidate);
        }
    }

    private void removeCandidate(Candidate candidate) {
        bot.deleteMessage(candidate.getMessage());
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public long getMessageId() {
        return messageId;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

}
