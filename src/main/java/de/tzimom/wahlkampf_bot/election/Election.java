package de.tzimom.wahlkampf_bot.election;

import de.tzimom.wahlkampf_bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.time.ZoneId;
import java.util.*;

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
        MessageEmbed embed = new EmbedBuilder().setColor(Bot.EMBED_COLOR).setTitle("Kandidatur").setDescription
                ("Reagiere mit " + Candidate.VOTE_EMOTE + ", um für <@" + userId + "> zu stimmen").build();

        Message message = bot.sendMessageEmbed(textChannel, embed);

        if (message == null)
            return;

        if (!bot.addReaction(message, Candidate.VOTE_EMOTE_CODE))
            return;

        candidates.add(new Candidate(this, userId, message.getIdLong()));
    }

    public void removeCandidate(long userId) {
        Set<Candidate> remove = new HashSet<>();

        for (Candidate candidate : candidates) {
            if (candidate.getUserId() == userId) {
                removeCandidate(candidate);
                remove.add(candidate);
            }
        }

        candidates.removeAll(remove);
    }

    private void removeCandidate(Candidate candidate) {
        bot.deleteMessage(candidate.getMessage());
    }

    public void finish() {
        GuildChannel channel = bot.getJda().getGuildChannelById(textChannelId);

        if (channel == null)
            return;

        if (channel.getType() != ChannelType.TEXT)
            return;

        TextChannel textChannel = (TextChannel) channel;

        StringBuilder description = new StringBuilder("Die Richter Wahl ist beendet.\n\n");

        if (candidates.size() == 0)
            description.append("Es gab keine Kandidaten.");
        else {
            candidates.stream().sorted(Comparator.comparing(Candidate::getVotes).reversed()).forEach(candidate ->
                    description.append("<@").append(candidate.getUserId()).append("> » ").append(candidate.getVotes())
                            .append(candidate.getVotes() == 1 ? " Stimme" : " Stimmen").append("\n"));
        }

        MessageEmbed embed = new EmbedBuilder().setColor(Bot.EMBED_COLOR).setTitle("Richter Wahl Ergebnisse")
                .setDescription(description.toString()).setTimestamp(new Date().toInstant().atZone(ZoneId.systemDefault())).build();
        bot.sendMessageEmbed(textChannel, embed);
        cleanUp(textChannel);
    }

    private void cleanUp(TextChannel textChannel) {
        Message message = bot.retrieveMessage(textChannel, messageId);

        if (message != null)
            bot.deleteMessage(message);

        for (Candidate candidate : candidates) {
            Message candidateMessage = bot.retrieveMessage(textChannel, candidate.getMessageId());

            if (candidateMessage != null)
                bot.deleteMessage(candidateMessage);
        }
    }

    public Candidate findCandidate(long messageId) {
        for (Candidate candidate : candidates) {
            if (candidate.getMessageId() == messageId)
                return candidate;
        }

        return null;
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

}
