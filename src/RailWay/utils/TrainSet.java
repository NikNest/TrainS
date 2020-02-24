package RailWay.utils;

public final class TrainSet extends TrainPart implements SpecialIdable {
    public TrainSet(String trainsetClass, String trainsetName,
                    int length, boolean forwConnected, boolean backConnected) {
        this.trainsetClass = trainsetClass;
        this.trainsetName = trainsetName;
        this.setLength(length);
        this.setForwConnection(forwConnected);
        this.setBackConnection(backConnected);
    }
    private final String trainsetClass;
    private final String trainsetName;

    @Override
    public final String getSpecialClass() {
        return trainsetClass;
    }
    @Override
    public final String getSpecialName() {
        return trainsetName;
    }
    @Override
    public final String toString() {
        String str = "";
        if(getTrainId() != 0) str += getTrainId() + " ";
        else str += "none ";
        str += trainsetClass + " " + trainsetName + " " + getLength() + " " + isForwConnection() + " " + isBackConnection();
        return str;
    }
}
