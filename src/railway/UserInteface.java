package railway;

import railway.utils.Command;
import railway.utils.IncorrectInputException;
import railway.utils.Point;
import edu.kit.informatik.Terminal;

/**
 * class implement user interface
 * @author Nikita
 * @version 1
 */
public class UserInteface {
    private static final String POINT_REGEX = "^\\(-{0,1}\\d+,{1}-{0,1}\\d+\\)$";
    private static final String SPOINT_REGEX = "^\\(-{0,1}\\d+.-{0,1}\\d+\\),\\(-{0,1}\\d+,-{0,1}\\d+\\)$";
    private static final String SCLASS_REGEX = "^W{1}\\w+|[^W]\\w+";
    private static final String TRAIN_P_ID_REGEXW = "^W{1}\\d+|[^W]\\w+-\\w+|^W{1}\\w+-\\w+";
    private static final String TRAIN_P_ID_REGEX = "^\\d+|[^W]\\w+-\\w+|^W{1}\\w+-\\w+";
    private Depot depot;
    private RWState state;
    private boolean gameStatus;

    /**
     * UI constructor
     */
    public UserInteface() {
        depot = new Depot();
        state = new RWState();
        gameStatus = true;
    }

    /**
     * method calls nest command until exit
     */
    public void startGame() {
        while (gameStatus) {
            String userInput = Terminal.readLine();
            try {
                Command command = readCoomand(userInput);
                makeAction(command);
            } catch (IncorrectInputException ignored) { }
        }
    }

    /**
     * extract Point from String
     * @param string str
     * @return point
     */
    private Point extractPoint(String string) {
        String temp = string.substring(1, string.length() - 1);
        String[] coord = temp.split(",");
        return new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
    }

    /**
     * make action according to command
     * @param command processed command
     * @throws IncorrectInputException logic exception
     */
    private void makeAction(Command command) throws IncorrectInputException {
        if (command.getCommand().equals("ext")) {
            gameStatus = false;
            return;
        } else if (command.getCommand().matches("atk|asw|dtk|ltr|set|put|stp"))
            runCommandState(command);
        else
            runCommandDepot(command);
    }

    /**
     * run commands of the Depot class
     * @param command processed command
     * @throws IncorrectInputException logic exception
     */
    private void runCommandDepot(Command command) throws IncorrectInputException {
        switch(command.getCommand()) {
            case "cre": //create engine <engineType> <class> <name> <length> <couplingFront> <couplingBack> + cre
                depot.createEngine(command.getValue()[0], command.getValue()[1], command.getValue()[2],
                        Integer.parseInt(command.getValue()[3]),
                        command.getValue()[4].equals("true"), command.getValue()[5].equals("true"));
                break;
            case "lte": //list engines + lte
                Terminal.printLine(depot.listEngines());
                break;
            case "crc": //create coach <coachType> <length> <couplingFront> <couplingBack> + crc
                depot.createCoach(command.getValue()[0], Integer.parseInt(command.getValue()[1]),
                        command.getValue()[2].equals("true"), command.getValue()[3].equals("true"));
                break;
            case "ltc": //list coaches + ltc
                Terminal.printLine(depot.listCoaches());
                break;
            case "cts": //create train-set <class> <name> <length> <couplingFront> <couplingBack> + cts
                depot.createTrainSet(command.getValue()[0], command.getValue()[1],
                        Integer.parseInt(command.getValue()[2]), command.getValue()[3].equals("true"),
                        command.getValue()[4].equals("true"));
                break;
            case "lts": //list train-sets + lts
                Terminal.printLine(depot.listTrainSets());
                break;
            case "dls": //delete rolling stock <id> + dls
                depot.deleteRollingStock(command.getValue()[0]);
                break;
            case "dlt": //delete train <ID> + dlt
                depot.deleteTrain(Integer.parseInt(command.getValue()[0]));
                break;
            case "lit": //list trains + lit
                Terminal.printLine(depot.listTrains());
                break;
            case "swt": //show train <trainID> + swt
                depot.showTrain(Integer.parseInt(command.getValue()[0]));
                break;
            case  "atn": //add train <trainID> <rollingStockID> + atn
                depot.addTrain(Integer.parseInt(command.getValue()[0]), command.getValue()[1]);
                break;
            default:
                throw new IncorrectInputException();
        }
    }

    /**
     * run commands of the State class
     * @param command processed command
     * @throws IncorrectInputException logic exception
     */
    private void runCommandState(Command command) throws IncorrectInputException {
        switch(command.getCommand()) {
            case "atk": //add track <startpoint> -> <endpoint> + atk
                state.addTrack(extractPoint(command.getValue()[0]), extractPoint(command.getValue()[1]));
                break;
            case "asw": //add switch <startpoint> -> <endpoint1>,<endpoint2> + asw
                String[] temp = command.getValue()[1].split(",");
                Point point2 = extractPoint(temp[0] + "," + temp[1]);
                Point point3 = extractPoint(temp[2] + "," + temp[3]);
                state.addSwitch(extractPoint(command.getValue()[0]), point2, point3);
                break;
            case "dtk": //delete track <trackID> + dtk
                state.deleteTrack(Integer.parseInt(command.getValue()[0]));
                break;
            case "ltr":  //list tracks + ltr
                Terminal.printLine(state.listTracks());
                break;
            case "set": //set switch <trackID> position <point> + set
                state.setSwitch(Integer.parseInt(command.getValue()[0]), extractPoint(command.getValue()[1]));
                break;
            case "put": //put train <trainID> at <point> in direction <x>,<y> + put
                state.putTrain(depot.getTrain(Integer.parseInt(command.getValue()[0])),
                        extractPoint(command.getValue()[1]), extractPoint(command.getValue()[2]));
                break;
            case "stp": //step <speed> + stp
                state.step(Short.parseShort(command.getValue()[0]));
                break;
            default:
                throw new IncorrectInputException();
        }
    }

    /**
     * create processed command from the input String
     * @param input raw input
     * @return processed command
     * @throws IncorrectInputException logic exception
     */
    private Command readCoomand(String input) throws IncorrectInputException {
        String userInput = input;
        userInput = userInput.trim();
        if (userInput.equals(""))
            throw new IncorrectInputException();
        String[] str = userInput.split(" ");
        switch (str[0]) {
            case "add":
                return readAdd(str);
            case "delete":
                return readDelete(str);
            case "list":
                return readList(str);
            case "set":
                return readSet(str);
            case "create":
                return readCreate(str);
            case "show":
                return readShow(str);
            case "put":
                return readPut(str);
            case "step":
                return readStep(str);
            case "exit":
                return new Command(new String[]{}, "ext");
            default:
                throw new IncorrectInputException();
        }
    }

    /**
     * read step-type command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readStep(String[] str) throws IncorrectInputException {
        if (str.length == 2 && str[1].matches("^-{0,1}\\d+"))
            return new Command(new String[]{str[1]}, "stp");
        else
            throw new IncorrectInputException();
    }

    /**
     *  read put-type command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readPut(String[] str) throws IncorrectInputException {
        if (str.length == 8 && str[1].equals("train") && str[2].matches("\\d+")
                && str[3].equals("at") && str[4].matches(POINT_REGEX)
                && str[5].equals("in") && str[6].equals("direction") && str[7].matches("-{0,1}\\d+,-{0,1}\\d+")) {
            return new Command(new String[]{str[2], str[4], "(" + str[7] + ")"}, "put");
        } else
            throw new IncorrectInputException();
    }

    /**
     * read show-typr command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readShow(String[] str) throws IncorrectInputException {
        if (str.length == 3) {
            if (str[1].equals("train") && str[2].matches("\\d+"))
                return new Command(new String[]{str[2]}, "swt");
            else
                throw new IncorrectInputException();
        } else
            throw new IncorrectInputException();
    }

    /**
     * read add-type command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readAdd(String[] str) throws IncorrectInputException {
        if (str.length == 1)
            throw new IncorrectInputException();
        else
            switch(str[1]) {
                case "track":
                    if (str.length == 5 && str[2].matches(POINT_REGEX)
                            && str[3].equals("->") && str[4].matches(POINT_REGEX))
                        return new Command(new String[]{str[2], str[4]}, "atk");
                case "switch":
                    if (str.length == 5 && str[2].matches(POINT_REGEX)
                            && str[3].matches("->") && str[4].matches(SPOINT_REGEX))
                        return new Command(new String[]{str[2], str[4]}, "asw");
                case "train":
                    if (str.length == 4 && str[2].matches("^\\d+") && str[3].matches(TRAIN_P_ID_REGEXW))
                        return new Command(new String[]{str[2], str[3]}, "atn");
                default:
                    throw new IncorrectInputException();
            }
    }

    /**
     * read delete-type command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readDelete(String[] str) throws IncorrectInputException {
        if (str.length == 1)
            throw new IncorrectInputException();
        else
            switch(str[1]) {
                case "track":
                    if (str.length == 3 && str[2].matches("-{0,1}\\d+"))
                        return new Command(new String[]{str[2]}, "dtk");
                case "rolling":
                    if (str.length == 4 && str[2].equals("stock") && str[3].matches(TRAIN_P_ID_REGEX))
                        return new Command(new String[]{str[3]}, "dls");
                case "train":
                    if (str.length == 3 && str[2].matches("-{0,1}\\d+"))
                        return new Command(new String[]{str[2]}, "dlt");
                default:
                    throw new IncorrectInputException();
            }
    }

    /**
     * read  list-type command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readList(String[] str) throws IncorrectInputException {
        if (str.length == 2)
            if (str[1].equals("tracks"))
                return new Command(new String[]{}, "ltr");
            else if (str[1].equals("engines"))
                return new Command(new String[]{}, "lte");
            else if (str[1].equals("coaches"))
                return new Command(new String[]{}, "ltc");
            else if (str[1].equals("train-sets"))
                return new Command(new String[]{}, "lts");
            else if (str[1].equals("trains"))
                return new Command(new String[]{}, "lit");
            else
                throw new IncorrectInputException();
        else
            throw new IncorrectInputException();
    }

    /**
     * read set-type command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readSet(String[] str) throws IncorrectInputException {
        if (str.length == 5 && str[1].equals("switch") && str[2].matches("^\\d+")
                && str[3].equals("position") && str[4].matches(POINT_REGEX)) {
            return new Command(new String[]{str[2], str[4]}, "set");
        } else
            throw new IncorrectInputException();
    }

    /**
     * read create-type command
     * @param str input
     * @return processed command
     * @throws IncorrectInputException semantic exception
     */
    private Command readCreate(String[] str) throws IncorrectInputException {
        if (str.length < 2)
            throw new IncorrectInputException();
        else {
            if (str[1].equals("engine") && str.length == 8 && str[2].matches("electrical|steam|diesel")
                    && str[3].matches(SCLASS_REGEX) && str[4].matches("\\w+")
                    && str[5].matches("\\d+") && str[6].matches("true|false") && str[7].matches("true|false"))
                return new Command(new String[]{str[2], str[3], str[4], str[5], str[6], str[7]}, "cre");
            else if (str[1].equals("coach") && str.length == 6
                    && str[2].matches("passenger|freight|special") && str[3].matches("\\d+")
                    && str[4].matches("true|false") && str[5].matches("true|false"))
                    return new Command(new String[]{str[2], str[3], str[4], str[5]}, "crc");
            else if (str[1].equals("train-set") && str.length == 7 && str[2].matches(SCLASS_REGEX)
                    && str[3].matches("\\w+")
                    && str[4].matches("\\d+") && str[5].matches("true|false") && str[6].matches("true|false"))
                return new Command(new String[]{str[2], str[3], str[4], str[5], str[6]}, "cts");
            else
                throw new IncorrectInputException();
        }
    }
}
