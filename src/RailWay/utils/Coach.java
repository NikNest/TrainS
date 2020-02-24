package RailWay.utils;

public final class Coach extends TrainPart {
    public Coach(String coachType, int length, boolean forwConnected, boolean backConnected, int id) {
        this.type = coachType;
        this.setLength(length);
        this.setForwConnection(forwConnected);
        this.setBackConnection(backConnected);
        this.id = id;
    }
    private final String type;
    private final int id;
    static int lastCoachId = 0;
    public final int getId() {
        return id;
    }

    public final String getType() {
        return type;
    }

    @Override
    public final String toString() {
        String str = "";
        str += id + " ";
        if(getTrainId() != 0) str += getTrainId() + " ";
        else str += "none ";
        str += type + " " + getLength() + " " + isForwConnection() + " " + isBackConnection();
        return str;
    }
}
