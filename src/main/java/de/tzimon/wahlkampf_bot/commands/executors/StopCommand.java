package de.tzimon.wahlkampf_bot.commands.executors;

import de.tzimon.wahlkampf_bot.Bot;
import de.tzimon.wahlkampf_bot.commands.CommandExecutor;

public class StopCommand implements CommandExecutor {

    public void execute(String[] args) {
        Bot.getInstance().shutdown();
    }

}
