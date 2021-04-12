package de.tzimom.wahlkampf_bot.commands;

public class Command {

    private String name;
    private CommandExecutor executor;

    public Command(String name) {
        this.name = name;
    }

    public Command(String name, CommandExecutor executor) {
        this(name);
        this.executor = executor;
    }

    public void execute(String[] args) {
        if (executor != null)
            executor.execute(args);
    }

    public String getName() {
        return name;
    }

}
