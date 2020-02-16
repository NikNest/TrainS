package RailWay.utils;

public class Engine extends TrainPart implements SpecialIdable {
    public Engine(String type, String engineClass, String engineName,
                  int length, boolean forwConnected, boolean backConnected) {
        //this.trainPartType = "engine";
        this.type = type;
        this.engineClass = engineClass;
        this.engineName = engineName;
        this.length = length;
        this.forwConnection = forwConnected;
        this.backConnection = backConnected;

    }

    String type;
    String engineClass;
    String engineName;

    public String getType() {
        return type;
    }

    @Override
    public String getSpecialClass() {
        return engineClass;
    }
    @Override
    public String getSpecialName() {
        return engineName;
    }

    @Override
    public String toString() {
        String str = "";
        String shortType = type.equals("electrical") ? "e" : type.equals("diesel") ? "d" : "s";
        if(isTrainPart) str += trainId + " ";
        else str += "none ";
        str += shortType + " " + engineClass + " " + engineName + " " + length + " " + forwConnection + " " + backConnection;
        return str;
    }
}
