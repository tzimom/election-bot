package de.tzimon.wahlkampf_bot;

import de.tzimon.wahlkampf_bot.election.Candidate;
import de.tzimon.wahlkampf_bot.election.Election;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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
