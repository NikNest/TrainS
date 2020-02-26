package railway.utils;

import edu.kit.informatik.Terminal;

/**
 * class of the custom exception for wrong user input(both semantic and logic)
 * @author  Nikita
 * @version 1
 */
public final class IncorrectInputException extends Exception {
    /**
     * constructor for semantic exceptions
     */
    public IncorrectInputException() {
        Terminal.printError("Incorrect input");
    }

    /**
     * constructor for logic exceptions
     * @param string error msg
     */
    public IncorrectInputException(String string) {
        Terminal.printError(string);
    }
}
