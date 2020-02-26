package railway.utils;

/**
 * class for storing the value of command and the command itself in an appropriate form
 * @author Nikita
 * @version 1
 */
public final class Command {
    private final String[] value;
    private final String command;

    /**
     * command's constructor
     * @param value value in command
     * @param command command 3 letters representaion
     */
    public Command(String[] value, String command) {
        this.value = value;
        this.command = command;
    }

    /**
     * getter for value
     * @return command value
     */
    public String[] getValue() {
        return value;
    }

    /**
     * getter for command representation
     * @return comman representation
     */
    public String getCommand() {
        return command;
    }
}
