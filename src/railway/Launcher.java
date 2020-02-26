package railway;

import railway.utils.IncorrectInputException;

public class Launcher {
    public static void main(String[] args) throws IncorrectInputException {
        UserInteface ui = new UserInteface();
        ui.startGame();
    }
}
