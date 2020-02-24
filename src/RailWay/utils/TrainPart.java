package RailWay.utils;

public class TrainPart {
    private int trainId = 0;
    private int length;
    private boolean forwConnection;
    private boolean backConnection;

    public final void setTrainId(int trainId) {
        this.trainId = trainId;
    }
    public final void setLength(int length) {
        this.length = length;
    }
    public final void setForwConnection(boolean forwConnection) {
        this.forwConnection = forwConnection;
    }
    public final void setBackConnection(boolean backConnection) {
        this.backConnection = backConnection;
    }

    public final boolean isForwConnection() {
        return forwConnection;
    }
    public final boolean isBackConnection() {
        return backConnection;
    }
    public final int getLength() {
        return length;
    }
    public final int getTrainId() {
        return trainId;
    }
}
