package de.tzimom.election_bot.commands;

import de.tzimom.election_bot.commands.executors.ElectionCommand;
import de.tzimom.election_bot.commands.executors.HelpCommand;
import de.tzimom.election_bot.commands.executors.StopCommand;
import de.tzimom.election_bot.Bot;

import java.util.*;

public class CommandManager {

    private Bot bot = Bot.getInstance();

    private Set<Command> commands = new HashSet<>();

    public CommandManager() {
        registerCommands();
    }

    private void registerCommands() {
        commands.add(new Command("help", new HelpCommand()));
        commands.add(new Command("stop", new StopCommand()));
        commands.add(new Command("election", new ElectionCommand()));
    }

    public void startAsync() {
        new Thread(this::start).start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (bot.isEnabled()) {
            readLine(scanner);
        }
    }

    private void readLine(Scanner scanner) {
        String input = scanner.nextLine();

        if (input.isEmpty())
            return;

        processInput(input);
    }

    public void processInput(String input) {
        String[] parts = input.split(" ");

        if (parts.length == 0)
            return;

        String commandName = "";
        List<String> argList = new ArrayList<>();

        for (String part : parts) {
            if (part.isEmpty())
                continue;

            if (commandName.isEmpty())
                commandName = part;
            else
                argList.add(part);
        }

        String[] args = new String[argList.size()];
        args = argList.toArray(args);

        Command command = getCommand(commandName);

        if (command == null) {
            Bot.LOGGER.error("Unknown command!");
            return;
        }

        command.execute(args);
    }

    public Command getCommand(String name) {
        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(name))
                return command;
        }

        return null;
    }

    public Set<Command> getCommands() {
        return commands;
    }

}
