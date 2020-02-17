package RailWay.utils;

import java.util.ArrayList;

public class Train {
    public Train(int trainId) {
     this.trainId = trainId;
     trainParts = new ArrayList<>();
    }
    int trainId = 0;
    //возможно вообще без этой переменной можно
//    static int lastTrainId = 0;
    ArrayList<TrainPart> trainParts;


//    //retrun num for W, smth-smth else
//    public ArrayList<TrainPart> getTrainParts() {
//        return trainParts;
//    }

    public int getTrainId() {
        return trainId;
    }
    public void addTrainPart(TrainPart trainPart) {
        //trainPart.setTrainId(this.trainId);
        trainParts.add(trainPart);
    }
    public TrainPart getLastTrainPart() {
        return trainParts.get(trainParts.size()-1);
    }

//    public static int getLastTrainId() {
//        return lastTrainId;
//    }
//    public static void inrcLastTrainId() {
//        lastTrainId++;
//    }

    public void freeUsedTrainParts() {
        trainParts.stream().forEach(tp -> tp.setTrainId(0));
        trainParts.clear();
    }
    //returns W
    @Override
    public String toString() {
        String str = trainId + " ";
        for(int i = 0; i < trainParts.size()-1; i++) {
            if(Coach.class.isInstance(trainParts.get(i)))
                str += "W" + ((Coach)trainParts.get(i)).getId() + " ";
            else
                str += ((SpecialIdable)trainParts.get(i)).getSpecialClass() + "-" +
                        ((SpecialIdable)trainParts.get(i)).getSpecialName() +  " ";
        }
        if(Coach.class.isInstance(trainParts.get(trainParts.size() - 1)))
            str += "W" + ((Coach)trainParts.get(trainParts.size() - 1)).getId();
        else
            str += ((SpecialIdable)trainParts.get(trainParts.size() - 1)).getSpecialClass() + "-" +
                    ((SpecialIdable)trainParts.get(trainParts.size() - 1)).getSpecialName();
        return str;
    }

    public String showTrain() {
        return null;
    }
}
