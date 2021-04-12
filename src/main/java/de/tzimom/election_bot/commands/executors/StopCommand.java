package de.tzimom.election_bot.commands.executors;

import de.tzimom.election_bot.commands.CommandExecutor;
import de.tzimom.election_bot.Bot;

public class StopCommand implements CommandExecutor {

    public void execute(String[] args) {
        Bot.getInstance().shutdown();
    }

}
