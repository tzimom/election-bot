package de.tzimon.wahlkampf_bot.election;

import de.tzimon.wahlkampf_bot.Bot;
import de.tzimon.wahlkampf_bot.logging.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

public class Election {

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

        candidates.add(new Candidate(userId, textChannel.getIdLong(), message.getIdLong()));
    }

    public void removeCandidate(long userId) {
        for (Candidate candidate : candidates) {
            if (candidate.getUserId() == userId)
                removeCandidate(candidate);
        }
    }

    private void removeCandidate(Candidate candidate) {
        GuildChannel channel = bot.getJda().getGuildChannelById(candidate.getTextChannelId());

        if (channel == null)
            return;

        if (channel.getType() != ChannelType.TEXT)
            return;

        TextChannel textChannel = (TextChannel) channel;
        bot.deleteMessage(textChannel, candidate.getMessageId());
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public long getMessageId() {
        return messageId;
    }

}
