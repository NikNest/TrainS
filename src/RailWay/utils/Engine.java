package RailWay.utils;

public final class Engine extends TrainPart implements SpecialIdable {
    public Engine(String type, String engineClass, String engineName,
                  int length, boolean forwConnected, boolean backConnected) {
        this.type = type;
        this.engineClass = engineClass;
        this.engineName = engineName;
        this.setLength(length);
        this.setForwConnection(forwConnected);
        this.setBackConnection(backConnected);

    }

    private final String type;
    private final String engineClass;
    private final String engineName;

    public final String getType() {
        return type;
    }

    @Override
    public final String getSpecialClass() {
        return engineClass;
    }
    @Override
    public final String getSpecialName() {
        return engineName;
    }
    @Override
    public final String toString() {
        String str = "";
        String shortType = type.equals("electrical") ? "e" : type.equals("diesel") ? "d" : "s";
        if(getTrainId() != 0) str += getTrainId() + " ";
        else str += "none ";
        str += shortType + " " + engineClass + " " + engineName + " " + getLength() + " " + isForwConnection() + " " + isBackConnection();
        return str;
    }
}
