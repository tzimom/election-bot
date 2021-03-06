package de.tzimom.election_bot;

import de.tzimom.election_bot.commands.CommandManager;
import de.tzimom.election_bot.election.ElectionManager;
import de.tzimom.election_bot.logging.ColoredLogger;
import de.tzimom.election_bot.logging.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.PermissionException;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Scanner;

public class Bot {

    public static final Logger LOGGER = new ColoredLogger();
    public static final Color EMBED_COLOR = new Color(0xeccc68);

    private static Bot instance;
    private boolean enabled = true;
    private JDA jda;

    private ElectionManager electionManager;
    private CommandManager commandManager;

    public static void main(String[] args) {
        instance = new Bot();
        instance.onEnable(args);
    }

    private void onEnable(String[] args) {
        String token;

        if (args.length == 1) token = args[0];
        else token = requestToken();

        while (!login(token)) {
            LOGGER.error("Failed to login!");
            token = requestToken();
        }

        electionManager = new ElectionManager();
        commandManager = new CommandManager();

        commandManager.startAsync();
        jda.addEventListener(new EventHandler());
    }

    public void shutdown() {
        LOGGER.info("Shutting down...");
        enabled = false;
        jda.shutdown();
    }

    private boolean login(String token) {
        try {
            jda = JDABuilder.createDefault(token).build();
            return true;
        } catch (LoginException ignored) {
            return false;
        }
    }

    private static String requestToken() {
        LOGGER.info("Please enter your token:");

        String token;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            token = scanner.nextLine();

            if (token == null || token.equals("") || token.contains(" ")) {
                LOGGER.error("Invalid token!");
                continue;
            }

            break;
        }

        return token;
    }

    public Message sendMessageEmbed(TextChannel textChannel, MessageEmbed embed) {
        try {
            return textChannel.sendMessage(embed).complete();
        } catch (PermissionException ignored) {
            sendInsufficientPermissions();
            return null;
        }
    }

    public Message retrieveMessage(TextChannel textChannel, long messageId) {
        try {
            return textChannel.retrieveMessageById(messageId).complete();
        } catch (PermissionException ignored) {
            sendInsufficientPermissions();
            return null;
        }
    }

    public void deleteMessage(Message message) {
        try {
            message.delete().complete();
        } catch (PermissionException ignored) {
            sendInsufficientPermissions();
        }
    }

    public boolean addReaction(Message message, String emote) {
        try {
            message.addReaction(emote).complete();
            return true;
        } catch (PermissionException ignored) {
            sendInsufficientPermissions();
            return false;
        }
    }

    public void removeReaction(Message message, String emote, long userId) {
        try {
            User user = jda.retrieveUserById(userId).complete();

            if (user == null)
                return;

            message.removeReaction(emote, user).complete();
        } catch (PermissionException ignored) {
            sendInsufficientPermissions();
        }
    }

    private void sendInsufficientPermissions() {
        LOGGER.error("Insufficient permission");
    }

    public TextChannel getTextChannel(long id) {
        GuildChannel channel = jda.getGuildChannelById(id);

        if (channel == null)
            return null;

        if (channel.getType() != ChannelType.TEXT)
            return null;

        return (TextChannel) channel;
    }

    public static Bot getInstance() {
        return instance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public JDA getJda() {
        return jda;
    }

    public ElectionManager getElectionManager() {
        return electionManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

}
