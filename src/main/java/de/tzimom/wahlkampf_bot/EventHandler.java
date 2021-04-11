package de.tzimom.wahlkampf_bot;

import de.tzimom.wahlkampf_bot.election.Election;
import de.tzimom.wahlkampf_bot.election.Candidate;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter {

    private Bot bot = Bot.getInstance();

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getMember().getId().equals(Bot.getInstance().getJda().getSelfUser().getId()))
            return;

        if (!event.getReactionEmote().getAsCodepoints().equalsIgnoreCase(Candidate.CANDIDATE_EMOTE_CODE))
            return;

        Election election = bot.getElectionManager().getElection(event.getChannel().getIdLong(), event.getMessageIdLong());

        if (election == null)
            return;

        election.createCandidate(event.getChannel(), event.getUserIdLong());
    }

    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        if (event.getMember().getId().equals(Bot.getInstance().getJda().getSelfUser().getId()))
            return;

        if (!event.getReactionEmote().getAsCodepoints().equalsIgnoreCase(Candidate.CANDIDATE_EMOTE_CODE))
            return;

        Election election = bot.getElectionManager().getElection(event.getChannel().getIdLong(), event.getMessageIdLong());

        if (election == null)
            return;

        election.removeCandidate(event.getUserIdLong());
    }

}