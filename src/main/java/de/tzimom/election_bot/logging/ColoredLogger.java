package de.tzimom.election_bot.logging;

public class ColoredLogger implements Logger {

    public void info(String log) {
        System.out.println("[BOT] " + log + "\u001B[0m");
    }

    public void error(String log) {
        info("\u001B[31m" + log);
    }

}
