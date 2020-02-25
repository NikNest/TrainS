package RailWay.utils;

import edu.kit.informatik.Terminal;

public class IncorrectInputException extends Exception {
    public IncorrectInputException() {
            super();
            Terminal.printError("Incorrect input");
    }
    public IncorrectInputException(String string) {
        super(string);
        Terminal.printError(string);
    }
}
