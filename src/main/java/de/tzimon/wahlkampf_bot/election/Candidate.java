package de.tzimon.wahlkampf_bot.election;

public class Candidate {

    public static final String CANDIDATE_EMOTE = ":person_raising_hand:";
    public static final String CANDIDATE_EMOTE_CODE = "U+1F64B";

    public static final String VOTE_EMOTE = ":raised_hand:";
    public static final String VOTE_EMOTE_CODE = "U+270B";

    private final long userId;
    private final long textChannelId;
    private final long messageId;

    public Candidate(long userId, long textChannelId, long messageId) {
        this.userId = userId;
        this.textChannelId = textChannelId;
        this.messageId = messageId;
    }

    public long getUserId() {
        return userId;
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public long getMessageId() {
        return messageId;
    }

}
