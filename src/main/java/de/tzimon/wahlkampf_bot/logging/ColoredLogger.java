package de.tzimon.wahlkampf_bot.logging;

public class ColoredLogger implements Logger {

    public void info(String log) {
//        String date = new SimpleDateFormat("HH:MM:SS").format(new Date());
//        System.out.println("[" + date + "] " + log + "\u001B[0m");
        System.out.println("[BOT] " + log + "\u001B[0m");
    }

    public void error(String log) {
        info("\u001B[31m" + log);
    }

}
