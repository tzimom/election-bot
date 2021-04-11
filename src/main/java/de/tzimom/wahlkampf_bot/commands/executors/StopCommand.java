package de.tzimom.wahlkampf_bot.commands.executors;

import de.tzimom.wahlkampf_bot.commands.CommandExecutor;
import de.tzimom.wahlkampf_bot.Bot;

public class StopCommand implements CommandExecutor {

    public void execute(String[] args) {
        Bot.getInstance().shutdown();
    }

}
