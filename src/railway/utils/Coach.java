package railway.utils;

/**
 * class for coach and logic of this trainpart
 * @author Nikita
 * @version 1
 */
public final class Coach extends TrainPart {
    private final String type;
    private final int id;

    /**
     * coach's constructor
     * @param coachType coach's type
     * @param length coach's length
     * @param forwConnected is forward connected
     * @param backConnected is back connected
     * @param id id of the coach
     */
    public Coach(String coachType, int length, boolean forwConnected, boolean backConnected, int id) {
        this.type = coachType;
        this.setLength(length);
        this.setForwConnection(forwConnected);
        this.setBackConnection(backConnected);
        this.id = id;
    }

    /**
     * getter for coach id
     * @return coach's id
     */
    public int getId() {
        return id;
    }

    /**
     * getter for coach type
     * @return coach's type
     */
    public String getType() {
        return type;
    }

    /**
     * representation of coach
     * @return info about coach
     */
    @Override
    public String toString() {
        String str = "";
        str += id + " ";
        if (getTrainId() != 0) str += getTrainId() + " ";
        else str += "none ";
        if (type.equals("passenger"))
            str += "p";
        else if (type.equals("freight"))
            str += "f";
        else
            str += "s";
        str += " " + getLength() + " " + isForwConnection() + " " + isBackConnection();
        return str;
    }
}
