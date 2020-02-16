package RailWay.utils;

import java.util.ArrayList;

public class Train {
    public Train(int trainId) {
     this.trainId = trainId;
     trainPartsIds = new ArrayList<>();
     trainPartisCouch = new ArrayList<>();
    }
    int trainId = 0;
    //возможно вообще без этой переменной можно
    static int lastTrainId = 0;
    ArrayList<String> trainPartsIds;
    ArrayList<Boolean> trainPartisCouch;

    //retrun num for W, smth-smth else
    public String[] getTrainPartsIds() {
        String[] str = new String[trainPartsIds.size()];
        int i = 0;
        for(String trainPartId : trainPartsIds) {
            str[i] = trainPartId;
            i++;
        }
        return str;
    }
//    public ArrayList<Boolean> getTranPartsStatuses() {
//        return (ArrayList<Boolean>) trainPartisCouch.clone();
//    }
    public int getTrainId() {
        return trainId;
    }
    //without W
    public void addTrainPart(String trainPartId, boolean isCouch) {
        trainPartsIds.add(trainPartId);
        trainPartisCouch.add(isCouch);
    }
    //without W
    public String getLastTrainPartId() {
        return trainPartsIds.get(trainPartsIds.size()-1);
    }
    public static int getLastTrainId() {
        return lastTrainId;
    }
    public static void inrcLastTrainId() {
        lastTrainId++;
    }
//    public String showTrain() {
//
//        for(String trainPartId : trainPartsIds) {
//            Depot.
//        }
//    }
    //returns W
    @Override
    public String toString() {
        String str = trainId + " ";
        for(int i = 0; i < trainPartsIds.size()-1; i++) {
            if(trainPartisCouch.get(i))
                str += "W" + trainPartsIds.get(i) + " ";
            else
                str += trainPartsIds.get(i) + " ";
        }
        if(trainPartisCouch.get(trainPartisCouch.size()-1))
            str += "W" + trainPartsIds.get(trainPartsIds.size()-1);
        else
            str += trainPartsIds.get(trainPartsIds.size()-1);
        return str;
    }
}
