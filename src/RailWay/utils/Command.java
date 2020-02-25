package RailWay.utils;

public class Command {
    public Command(String[] value, String command) {
        this.value = value;
        this.command = command;
    }
    String[] value;
    String command;

    public String[] getValue() {
        return value;
    }

    public String getCommand() {
        return command;
    }
}
