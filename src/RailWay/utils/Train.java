package RailWay.utils;

import RailWay.Depot;
import RailWay.utils.TrainPart;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;

public class Train {
    public Train(int trainId) {
     this.trainId = trainId;
    }
    int trainId = 0;
    //возможно вообще без этой переменной можно
    static int lastTrainId = 0;
    ArrayList<String> trainPartsIds;

    public String[] getTrainPartsIds() {
        String[] str = new String[trainPartsIds.size()];
        int i = 0;
        for(String trainPartId : trainPartsIds) {
            str[i] = trainPartId;
            i++;
        }
        return str;
    }

    public int getTrainId() {
        return trainId;
    }

    public void addTrainPart(String trainPartId) {
        trainPartsIds.add(trainPartId);
    }
    public String getLastTrainPartId() {
        return trainPartsIds.get(trainPartsIds.size()-1);
    }
    public static int getLastTrainId() {
        return lastTrainId;
    }
    public static void inrcLastTrainId() {
        lastTrainId++;
    }

    @Override
    public String toString() {
        String str = Integer.toString(trainId);
        for(int i = 0; i < trainPartsIds.size()-1; i++) {
            str += trainPartsIds.get(i) + " ";
        }
        str += trainPartsIds.get(trainPartsIds.size()-1);
        return str;
    }
}
