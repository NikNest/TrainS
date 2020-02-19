package RailWay.utils;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

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
    public int getTrainLength() {
        int length = 0;
        for(TrainPart part : trainParts) {
            length += part.getLength();
        }
        return length;
    }
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

    public void freeUsedTrainParts() {
        trainParts.stream().forEach(tp -> tp.setTrainId(0));
        trainParts.clear();
    }
    private String generateLine(int size) {
        String line = "";
        while(size>0) {
            line += " ";
            size--;
        }
        return line;
    }
    private void addTrainPartPicture(ArrayList<String> globalPicture, String[] trainPartPicure) {

        if(globalPicture.size()==0) {
            for (int i = trainPartPicure.length-1; i>=0; i--) {
                globalPicture.add(trainPartPicure[i]);
            }
        } else {
            if(globalPicture.size() >= trainPartPicure.length){
                for (int i = trainPartPicure.length-1; i >= 0; i--) {
                     globalPicture.set(trainPartPicure.length - 1 - i,
                             globalPicture.get(trainPartPicure.length - 1 - i) + " " + trainPartPicure[i]);
                }
                for (int i = trainPartPicure.length; i < globalPicture.size();i++) {
                    globalPicture.set(i, globalPicture.get(i) + " " + generateLine(trainPartPicure[0].length()));
                }
            } else {
                for(int q = trainPartPicure.length - globalPicture.size(); q > 0; q--)
                {
                    globalPicture.add(generateLine(globalPicture.get(0).length()));
                }
                for (int i = trainPartPicure.length-1; i>=0; i--) {
                        globalPicture.set(trainPartPicure.length - 1 - i,
                            globalPicture.get(trainPartPicure.length - 1 - i) + " " + trainPartPicure[i]);
                }

            }
        }
    }
    public String showTrain() {
        ArrayList<String> picture = new ArrayList<>();
        Iterator<TrainPart> trainPartIterator = trainParts.iterator();
        for(int i = 0; i < trainParts.size() ; i++) {
            TrainPart trainPart = trainPartIterator.next();
            if (Coach.class.isInstance(trainPart))
                if(((Coach) trainPart).getType().equals("passenger")) {
                    addTrainPartPicture(picture, TrainRepres.PASSENGER_COUCH);
                } else if(((Coach) trainPart).getType().equals("freight")) {
                    addTrainPartPicture(picture, TrainRepres.FREIGHT_COUCH);
                } else {
                    addTrainPartPicture(picture, TrainRepres.SPECIAL_COUCH);
                }
            else if (Engine.class.isInstance(trainPart)) {
                if(((Engine) trainPart).getType().equals("steam")) {
                    addTrainPartPicture(picture, TrainRepres.STEAM_ENGINE);
                } else if(((Engine) trainPart).getType().equals("electrical")) {
                    addTrainPartPicture(picture, TrainRepres.ELECTRICAL_ENGINE);
                } else {
                    addTrainPartPicture(picture, TrainRepres.DIESEL_ENGINE);
                }
            } else {
                addTrainPartPicture(picture, TrainRepres.TRAIN_SET);
            }
        }
        Collections.reverse(picture);
        String pic = "";
        for(int i = 0; i < picture.size()-1; i++) {
            pic += picture.get(i) + "\n";
        }
            pic += picture.get(picture.size()-1);
        return pic;
    }
    public boolean isTrainValid() {
        if (Engine.class.isInstance(trainParts.get(0)) ||
                Engine.class.isInstance(trainParts.get(trainParts.size()-1)) ||
                TrainSet.class.isInstance(trainParts.get(0)))
            return true;
        else
            return false;
    }
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

}
