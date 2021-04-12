package de.tzimom.election_bot.election;

import de.tzimom.election_bot.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashSet;
import java.util.Set;

public class Candidate {

    public static final String CANDIDATE_EMOTE = ":person_raising_hand:";
    public static final String CANDIDATE_EMOTE_CODE = "U+1F64B";

    public static final String VOTE_EMOTE = ":raised_hand:";
    public static final String VOTE_EMOTE_CODE = "U+270B";

    private Bot bot = Bot.getInstance();

    private final Election election;
    private final long userId;
    private final long messageId;
    private final Set<Long> voters = new HashSet<>();

    public Candidate(Election election, long userId, long messageId) {
        this.election = election;
        this.userId = userId;
        this.messageId = messageId;
    }

    public void vote(long userId) {
        if (!Election.SELF_VOTES && userId == this.userId) {
            bot.removeReaction(getMessage(), VOTE_EMOTE_CODE, userId);
            return;
        }

        for (Candidate candidate : this.election.getCandidates())
            if (candidate.removeVote(userId))
                bot.removeReaction(candidate.getMessage(), VOTE_EMOTE_CODE, userId);

        voters.add(userId);
    }

    public boolean removeVote(long userId) {
        return voters.remove(userId);
    }

    public Message getMessage() {
        TextChannel textChannel = bot.getTextChannel(election.getTextChannelId());

        if (textChannel == null)
            return null;

        return bot.retrieveMessage(textChannel, messageId);
    }

    public long getUserId() {
        return userId;
    }

    public long getMessageId() {
        return messageId;
    }

    public int getVotes() {
        return voters.size();
    }

}
