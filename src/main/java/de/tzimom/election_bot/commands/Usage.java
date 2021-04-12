package de.tzimom.election_bot.commands;

import de.tzimom.election_bot.Bot;

public enum Usage {

    HELP,
    STOP,
    ELECTION;

    public void sendUsage() {
        sendUsage(true);
    }

    private void sendUsage(boolean sendHead) {
        if (sendHead)
            sendHead();

        switch (this) {
            case HELP:
                sendLine("help", "Displays help for the commands");
                break;
            case STOP:
                sendLine("stop", "Shuts down the Discord bot");
                break;
            case ELECTION:
                sendLine("election create <ChannelID>", "Creates an election");
                sendLine("election finish", "Finishes an ongoing election and displays the results");
                break;
            default:
                Bot.LOGGER.error("No help available");
                break;
        }
    }

    private void sendLine(String usage, String description) {
        Bot.LOGGER.info("- " + usage + " » " + description);
    }

    private static void sendHead() {
        Bot.LOGGER.info("Help:");
    }

    public static void sendAllUsages() {
        sendHead();

        for (Usage usage : Usage.values())
            usage.sendUsage(false);
    }

}
