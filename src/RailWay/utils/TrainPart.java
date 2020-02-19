package RailWay.utils;

public class TrainPart {
    int trainId = 0;
    //boolean isTrainPart = false;
    int length;
    boolean forwConnection;
    boolean backConnection;
//    String trainPartType;
//    public String getTrainPartType() {
//        return trainPartType;
//    }
    public int getLength() {
        return length;
    }

    public boolean isForwConnection() {
        return forwConnection;
    }

    public boolean isBackConnection() {
        return backConnection;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public int getTrainId() {
        return trainId;
    }
}
