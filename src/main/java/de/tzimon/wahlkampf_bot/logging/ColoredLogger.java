package de.tzimon.wahlkampf_bot.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ColoredLogger implements Logger {

    public void info(String log) {
        String date = new SimpleDateFormat("HH:MM:SS").format(new Date());
        System.out.println("[" + date + "] " + log + "\u001B[0m");
    }

    public void error(String log) {
        info("\u001B[31m" + log);
    }

}
