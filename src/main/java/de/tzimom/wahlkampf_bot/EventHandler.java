package de.tzimom.wahlkampf_bot;

import de.tzimom.wahlkampf_bot.election.Election;
import de.tzimom.wahlkampf_bot.election.Candidate;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter {

    private final Bot bot = Bot.getInstance();

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().getId().equals(bot.getJda().getSelfUser().getId()))
            return;

        TextChannel textChannel = event.getChannel();
        long userId = event.getUserIdLong();
        long messageId = event.getMessageIdLong();
        String reactionCode = event.getReactionEmote().getAsCodepoints();

        if (reactionCode.equalsIgnoreCase(Candidate.CANDIDATE_EMOTE_CODE))
            reactionAddCandidate(textChannel, userId);
        else if (reactionCode.equalsIgnoreCase(Candidate.VOTE_EMOTE_CODE))
            reactionAddVote(userId, messageId);
    }

    private void reactionAddCandidate(TextChannel textChannel, long userId) {
        Election election = bot.getElectionManager().getCurrentElection();

        if (election == null)
            return;

        election.createCandidate(textChannel, userId);
    }

    private void reactionAddVote(long userId, long messageId) {
        Election election = bot.getElectionManager().getCurrentElection();

        if (election == null)
            return;

        Candidate candidate = election.findCandidate(messageId);

        if (candidate == null)
            return;

        candidate.vote(userId);
    }

    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        if (event.getUserIdLong() == Bot.getInstance().getJda().getSelfUser().getIdLong())
            return;

        long userId = event.getUserIdLong();
        long messageId = event.getMessageIdLong();
        String reactionCode = event.getReactionEmote().getAsCodepoints();

        if (reactionCode.equalsIgnoreCase(Candidate.CANDIDATE_EMOTE_CODE))
            reactionRemoveCandidate(userId);
        else if (reactionCode.equalsIgnoreCase(Candidate.VOTE_EMOTE_CODE))
            reactionRemoveVote(userId, messageId);
    }

    private void reactionRemoveCandidate(long userId) {
        Election election = bot.getElectionManager().getCurrentElection();

        if (election == null)
            return;

        election.removeCandidate(userId);
    }

    private void reactionRemoveVote(long userId, long messageId) {
        Election election = bot.getElectionManager().getCurrentElection();

        if (election == null)
            return;

        Candidate candidate = election.findCandidate(messageId);

        if (candidate == null)
            return;

        candidate.removeVote(userId);
    }

}
