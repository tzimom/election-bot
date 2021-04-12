package de.tzimom.election_bot.commands.executors;

import de.tzimom.election_bot.commands.CommandExecutor;
import de.tzimom.election_bot.commands.Usage;

public class HelpCommand implements CommandExecutor {

    public void execute(String[] args) {
        Usage.sendAllUsages();
    }

}
