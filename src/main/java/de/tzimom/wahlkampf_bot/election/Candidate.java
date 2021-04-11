package de.tzimom.wahlkampf_bot.election;

import de.tzimom.wahlkampf_bot.Bot;
import net.dv8tion.jda.api.entities.Message;

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
    private final Message message;
    private final Set<Long> voters = new HashSet<>();

    public Candidate(Election election, long userId, Message message) {
        this.election = election;
        this.userId = userId;
        this.message = message;
    }

    public void vote(long userId) {
        if (!Election.SELF_VOTES && userId == this.userId) {
            bot.removeReaction(message, VOTE_EMOTE_CODE, userId);
            return;
        }

        for (Candidate candidate : this.election.getCandidates()) {
            if (candidate.voters.remove(userId))
                bot.removeReaction(candidate.message, VOTE_EMOTE_CODE, userId);
        }

        voters.add(userId);
    }

    public long getUserId() {
        return userId;
    }

    public Message getMessage() {
        return message;
    }

}
