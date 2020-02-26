package railway.utils;

/**
 * string representations of the train parts
 * @author Nikita
 * @version 1
 */
public final class TrainRepres {
    /**
     * char representation of steam engine
     */
    public static final String[] STEAM_ENGINE = {
        "     ++      +------",
        "     ||      |+-+ | ",
        "   /---------|| | | ",
        "  + ========  +-+ | ",
        " _|--/~\\------/~\\-+ ",
        "//// \\_/      \\_/   "
    };
    /**
     * char representation of electrical engine
     */
    public static final String[] ELECTRICAL_ENGINE = {
        "               ___    ",
        "                 \\    ",
        "  _______________/__  ",
        " /_| ____________ |_\\ ",
        "/   |____________|   \\",
        "\\                    /",
        " \\__________________/ ",
        "  (O)(O)      (O)(O)  "
    };
    /**
     * char representation of diesel engine
     */
    public static final String[] DIESEL_ENGINE = {
        "  _____________|____  ",
        " /_| ____________ |_\\ ",
        "/   |____________|   \\",
        "\\                    /",
        " \\__________________/ ",
        "  (O)(O)      (O)(O)  "
    };
    /**
     * char representation of passenger coach
     */
    public static final String[] PASSENGER_COACH = {
        "____________________",
        "|  ___ ___ ___ ___ |",
        "|  |_| |_| |_| |_| |",
        "|__________________|",
        "|__________________|",
        "   (O)        (O)   "
    };
    /**
     * char representation of freight coach
     */
    public static final String[] FREIGHT_COACH = {
        "|                  |",
        "|                  |",
        "|                  |",
        "|__________________|",
        "   (O)        (O)   "
    };
    /**
     * char representation of special coach
     */
    public static final String[] SPECIAL_COACH = {
        "               ____",
        "/--------------|  |",
        "\\--------------|  |",
        "  | |          |  |",
        " _|_|__________|  |",
        "|_________________|",
        "   (O)       (O)   "
    };
    /**
     * char representation of train-set
     * */
    public static final String[] TRAIN_SET = {
        "         ++         ",
        "         ||         ",
        "_________||_________",
        "|  ___ ___ ___ ___ |",
        "|  |_| |_| |_| |_| |",
        "|__________________|",
        "|__________________|",
        "   (O)        (O)   "
    };

}
