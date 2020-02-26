package railway.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * class for train = list of train parts
 * @author Nikita
 * @version 1
 */
public final class Train {
    private int trainId = 0;
    private ArrayList<TrainPart> trainParts;

    /**
     * train constructor
     * @param trainId id of train
     */
    public Train(int trainId) {
        this.trainId = trainId;
        trainParts = new ArrayList<TrainPart>();
    }

    /**
     * line of emptyspaces
     * @param size of line
     * @return emptyline
     */
    private String generateLine(int size) {
        String line = "";
        int temp = size;
        while (temp > 0) {
            line += " ";
            --temp;
        }
        return line;
    }

    /**
     * add train part picture to the picture of th train
     * @param globalPicture picture of the train
     * @param trainPartPicure picture of the train part
     */
    private void addTrainPartPicture(ArrayList<String> globalPicture, String[] trainPartPicure) {

        if (globalPicture.size() == 0) {
            for (int i = trainPartPicure.length - 1; i >= 0; i--) {
                globalPicture.add(trainPartPicure[i]);
            }
        } else {
            if (globalPicture.size() >= trainPartPicure.length) {
                for (int i = trainPartPicure.length - 1; i >= 0; i--) {
                    globalPicture.set(trainPartPicure.length - 1 - i,
                            globalPicture.get(trainPartPicure.length - 1 - i) + " " + trainPartPicure[i]);
                }
                for (int i = trainPartPicure.length; i < globalPicture.size(); i++) {
                    globalPicture.set(i, globalPicture.get(i) + " " + generateLine(trainPartPicure[0].length()));
                }
            } else {
                for (int q = trainPartPicure.length - globalPicture.size(); q > 0; q--)
                {
                    globalPicture.add(generateLine(globalPicture.get(0).length()));
                }
                for (int i = trainPartPicure.length - 1; i >= 0; i--) {
                    globalPicture.set(trainPartPicure.length - 1 - i,
                            globalPicture.get(trainPartPicure.length - 1 - i) + " " + trainPartPicure[i]);
                }

            }
        }
    }

    /**
     * set id = 0 for train parts not in trains
     */
    public void freeUsedTrainParts() {
        trainParts.stream().forEach(tp -> tp.setTrainId(0));
        trainParts.clear();
    }

    /**
     * add train part to train
     * @param trainPart to add
     */
    public void addTrainPart(TrainPart trainPart) {
        //trainPart.setTrainId(this.trainId);
        trainParts.add(trainPart);
    }

    /**
     * count train length
     * @return length
     */
    public int getTrainLength() {
        int length = 0;
        for (TrainPart part : trainParts) {
            length += part.getLength();
        }
        return length;
    }

    /**
     * getter for train id
     * @return id
     */
    public int getTrainId() {
        return trainId;
    }

    /**
     * getter for the last train part of the train
     * @return train part
     */
    public TrainPart getLastTrainPart() {
        return trainParts.get(trainParts.size() - 1);
    }

    /**
     * show train
     * @return picture as string
     */
    public String showTrain() {
        ArrayList<String> picture = new ArrayList<String>();
        Iterator<TrainPart> trainPartIterator = trainParts.iterator();
        for (int i = 0; i < trainParts.size(); i++) {
            TrainPart trainPart = trainPartIterator.next();
            if (Coach.class.isInstance(trainPart))
                if (((Coach) trainPart).getType().equals("passenger")) {
                    addTrainPartPicture(picture, TrainRepres.PASSENGER_COACH);
                } else if (((Coach) trainPart).getType().equals("freight")) {
                    addTrainPartPicture(picture, TrainRepres.FREIGHT_COACH);
                } else {
                    addTrainPartPicture(picture, TrainRepres.SPECIAL_COACH);
                }
            else if (Engine.class.isInstance(trainPart)) {
                if (((Engine) trainPart).getType().equals("steam")) {
                    addTrainPartPicture(picture, TrainRepres.STEAM_ENGINE);
                } else if (((Engine) trainPart).getType().equals("electrical")) {
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
        for (int i = 0; i < picture.size() - 1; i++) {
            pic += picture.get(i) + "\n";
        }
        pic += picture.get(picture.size() - 1);
        return pic;
    }

    /**
     * check if train crosses other train
     * @return legality status
     */
    public boolean isTrainValid() {
        if (Engine.class.isInstance(trainParts.get(0))
                || Engine.class.isInstance(trainParts.get(trainParts.size() - 1))
                || TrainSet.class.isInstance(trainParts.get(0)))
            return true;
        else
            return false;
    }

    /**
     * string repr of the train
     * @return train info
     */
    @Override
    public String toString() {
        String str = trainId + " ";
        for (int i = 0; i < trainParts.size(); i++) {
            if (Coach.class.isInstance(trainParts.get(i)))
                str += "W" + ((Coach) trainParts.get(i)).getId() + " ";
            else
                str += ((SpecialIdable) trainParts.get(i)).getSpecialClass() + "-"
                        + ((SpecialIdable) trainParts.get(i)).getSpecialName() +  " ";
        }
        return str.trim();
    }

}
