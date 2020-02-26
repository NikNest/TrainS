package railway.utils;

/**
 * class for coach and logic of this trainpart
 * @author Nikita
 * @version 1
 */
public final class Engine extends TrainPart implements SpecialIdable {
    private final String type;
    private final String engineClass;
    private final String engineName;

    /**
     * constructor of engine
     * @param type engine type
     * @param engineClass engine class
     * @param engineName engine name
     * @param length train part length
     * @param forwConnected is forward connected
     * @param backConnected is back connected
     */
    public Engine(String type, String engineClass, String engineName,
                  int length, boolean forwConnected, boolean backConnected) {
        this.type = type;
        this.engineClass = engineClass;
        this.engineName = engineName;
        this.setLength(length);
        this.setForwConnection(forwConnected);
        this.setBackConnection(backConnected);

    }

    /**
     * getter for type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * getter for class
     * @return class of engine
     */
    @Override
    public String getSpecialClass() {
        return engineClass;
    }

    /**
     * getter for name
     * @return engine name
     */
    @Override
    public String getSpecialName() {
        return engineName;
    }

    /**
     * string representation of the train
     * @return train info
     */
    @Override
    public String toString() {
        String str = "";
        String shortType = type.equals("electrical") ? "e" : type.equals("diesel") ? "d" : "s";
        if (getTrainId() != 0) str += getTrainId() + " ";
        else str += "none ";
        str += shortType + " " + engineClass + " " + engineName + " " + getLength() + " "
                + isForwConnection() + " " + isBackConnection();
        return str;
    }
}
