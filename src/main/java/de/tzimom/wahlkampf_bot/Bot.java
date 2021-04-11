package de.tzimom.wahlkampf_bot;

import de.tzimom.wahlkampf_bot.commands.CommandManager;
import de.tzimom.wahlkampf_bot.election.ElectionManager;
import de.tzimom.wahlkampf_bot.logging.ColoredLogger;
import de.tzimom.wahlkampf_bot.logging.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.PermissionException;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class Bot {

    public static final Logger LOGGER = new ColoredLogger();

    private static Bot instance;
    private boolean enabled = true;
    private JDA jda;

    private ElectionManager electionManager;

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

        new CommandManager().startAsync();
        jda.addEventListener(new EventHandler());
    }

    public void shutdown() {
        LOGGER.info("Shutting down...");
        enabled = false;
        jda.shutdown();
    }

    public Message sendMessageEmbed(TextChannel textChannel, MessageEmbed embed) {
        try {
            return textChannel.sendMessage(embed).complete();
        } catch (PermissionException ignored) {
            LOGGER.error("Insufficient permission");
            return null;
        }
    }

    public boolean addReaction(Message message, String emote) {
        try {
            message.addReaction(emote).complete();
            return true;
        } catch (PermissionException ignored) {
            LOGGER.error("Insufficient permission");
            return false;
        }
    }

    public void deleteMessage(TextChannel textChannel, long messageId) {
        try {
            textChannel.deleteMessageById(messageId).complete();
        } catch (ErrorResponseException ignored) {
            LOGGER.error("Message not found");
        } catch (PermissionException ignored) {
            LOGGER.error("Insufficient permission");
        }
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

}
