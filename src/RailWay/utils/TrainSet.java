package RailWay.utils;

public class TrainSet extends TrainPart implements SpecialIdable {
    public TrainSet(String trainsetClass, String trainsetName,
                    int length, boolean forwConnected, boolean backConnected) {
        this.trainsetClass = trainsetClass;
        this.trainsetName = trainsetName;
        this.length = length;
        this.forwConnection = forwConnected;
        this.backConnection = backConnected;
    }
    String trainsetClass;
    String trainsetName;

    @Override
    public String getSpecialClass() {
        return trainsetClass;
    }

    @Override
    public String getSpecialName() {
        return trainsetName;
    }

    @Override
    public String toString() {
        String str = "";
        if(isTrainPart) str += trainId + " ";
        else str += "none ";
        str += trainsetClass + " " + trainsetName + " " + length + " " + forwConnection + " " + backConnection;
        return str;
    }
}
