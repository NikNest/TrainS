package RailWay.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public final class Train {
    public Train(int trainId) {
     this.trainId = trainId;
     trainParts = new ArrayList<>();
    }
    private int trainId = 0;
    private ArrayList<TrainPart> trainParts;

    private final String generateLine(int size) {
        String line = "";
        while(size>0) {
            line += " ";
            size--;
        }
        return line;
    }
    private final void addTrainPartPicture(ArrayList<String> globalPicture, String[] trainPartPicure) {

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
    public final void freeUsedTrainParts() {
        trainParts.stream().forEach(tp -> tp.setTrainId(0));
        trainParts.clear();
    }
    public final void addTrainPart(TrainPart trainPart) {
        //trainPart.setTrainId(this.trainId);
        trainParts.add(trainPart);
    }
    public final int getTrainLength() {
        int length = 0;
        for(TrainPart part : trainParts) {
            length += part.getLength();
        }
        return length;
    }
    public final int getTrainId() {
        return trainId;
    }
    public final TrainPart getLastTrainPart() {
        return trainParts.get(trainParts.size()-1);
    }
    public final String showTrain() {
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
    public final boolean isTrainValid() {
        if (Engine.class.isInstance(trainParts.get(0)) ||
                Engine.class.isInstance(trainParts.get(trainParts.size()-1)) ||
                TrainSet.class.isInstance(trainParts.get(0)))
            return true;
        else
            return false;
    }
    @Override
    public final String toString() {
        String str = trainId + " ";
        for(int i = 0; i < trainParts.size(); i++) {
            if(Coach.class.isInstance(trainParts.get(i)))
                str += "W" + ((Coach)trainParts.get(i)).getId() + " ";
            else
                str += ((SpecialIdable)trainParts.get(i)).getSpecialClass() + "-" +
                        ((SpecialIdable)trainParts.get(i)).getSpecialName() +  " ";
        }
        return str.trim();
    }

}
