package RailWay;

import RailWay.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
//можно ли заменить итерацию по фор лупу обычным форичем
public class Depot {
    public Depot() {
        trainParts = new ArrayList<>();
        trains = new ArrayList<>();
    }
    ArrayList<TrainPart> trainParts;
    ArrayList<Train> trains;
    //need regex
    public void createCoach(String coachType, int length, boolean forwConnected, boolean backConnected) {
        TrainPart coach = new Coach(coachType, length, forwConnected, backConnected);
        //возможно перенести в конструктор  если далее более не используется
        ((Coach)coach).initId();
        System.out.println(((Coach)coach).getId());
        trainParts.add(coach);
    }
    //need regex
    public void createEngine(String type, String engineClass, String engineName,
                             int length, boolean forwConnected, boolean backConnected) {
        TrainPart engine = new Engine(type, engineClass, engineName, length, forwConnected, backConnected);
        if(trainParts.size()!=0)
        for(TrainPart trainPart : trainParts) {
            if(SpecialIdable.class.isInstance(trainPart)) {
                if (SpecialIdable.haveSameSpecialId((SpecialIdable) trainPart,(SpecialIdable) engine)) {
                    System.out.println("There is already a trainpart with the same id");
                    return;
                }
            }
        }
        System.out.println(engineClass + "-" + engineName);
        trainParts.add(engine);
    }
    //need regex
    public void createTrainSet(String trainsetClass, String trainsetName,
                         int length, boolean forwConnected, boolean backConnected) {
        TrainPart trainset = new TrainSet(trainsetClass, trainsetName, length, forwConnected, backConnected);
        if(trainParts.size()!=0)
        for(TrainPart trainPart : trainParts) {
           if(SpecialIdable.class.isInstance(trainPart)) {

               if (SpecialIdable.haveSameSpecialId((SpecialIdable) trainPart,(SpecialIdable) trainset)) {
                   System.out.println("There is already a trainpart with the same id");
                   return;
               }
           }
        }
        System.out.println(trainsetClass + "-" + trainsetName);
        trainParts.add(trainset);
   }
    public String listEngines() {
        String str = "";
        Iterator<TrainPart> iter = trainParts.iterator();
        while(iter.hasNext()) {
            TrainPart trainPart = iter.next();
            if(Engine.class.isInstance(trainPart)) {
                str += (Engine)trainPart;
                if(iter.hasNext())
                    str += "\n";
            }
        }
        if(str.equals("")) return "No engine exists";
        return sortLexSpecialId(str);
    }
    //train parts id here should be without 'W' prefix
    public String listCoaches() {
        String str = "";
        Iterator<TrainPart> iter = trainParts.iterator();
        while(iter.hasNext()) {
            TrainPart trainPart = iter.next();
            if(Coach.class.isInstance(trainPart)) {
                str += (Coach)trainPart;
                if(iter.hasNext())
                    str += "\n";
            }
        }
        if(str.equals("")) return "No coach exists";
        return sortCouchId(str);
    }
    public String listTrainSets() {
        String str = "";
        Iterator<TrainPart> iter = trainParts.iterator();
        while(iter.hasNext()) {
            TrainPart trainPart = iter.next();
            if(TrainSet.class.isInstance(trainPart)) {
                str += (TrainSet)trainPart;
                if(iter.hasNext())
                    str += "\n";
            }
        }
        if(str.equals("")) return "No train-set exists";
        return sortLexSpecialId(str);
    }
    public static String sortLexSpecialId(String listTrainParts) {
        String temp[] = listTrainParts.split("\n");
        ArrayList<String> lines = new ArrayList(Arrays.asList(temp));
        Collections.sort(lines, new SortSpecialId());
        String str = "";
        Iterator<String> iter = lines.iterator();
        while(iter.hasNext()) {
            str += iter.next();
            if(iter.hasNext())
                str += "\n";
        }
        return str;
    }
    public static String sortCouchId(String listTrainParts) {
        String temp[] = listTrainParts.split("\n");
        ArrayList<String> lines = new ArrayList(Arrays.asList(temp));
        Collections.sort(lines, new SortNumCoach());
        String str = "";
        Iterator<String> iter = lines.iterator();
        while(iter.hasNext()) {
            str += iter.next();
            if(iter.hasNext())
                str += "\n";
        }
        return str;
    }
    public void deleteRollingStock(String id) {
        Iterator<TrainPart> iter = trainParts.iterator();
        for(int i = 0; i < trainParts.size(); i++) {
            TrainPart trainpart = iter.next();
            //means that trainpart isn't a part of the train
            if (trainpart.getTrainId() == 0) {
                if (Coach.class.isInstance(trainpart)) {
                    if (id.equals(Integer.toString(((Coach) trainpart).getId()))) {
                        trainParts.remove(i);
                        System.out.println("OK");
                        return;
                    }
                } else if (Engine.class.isInstance(trainpart)) {
                    if ((((Engine) trainpart).getSpecialClass() + "-" + ((Engine) trainpart).getSpecialName()).equals(id)) {
                        trainParts.remove(i);
                        System.out.println("OK");
                        return;
                    }
                } else if (TrainSet.class.isInstance(trainpart)) {
                    if ((((TrainSet) trainpart).getSpecialClass() + "-" + ((TrainSet) trainpart).getSpecialName()).equals(id)) {
                        trainParts.remove(i);
                        System.out.println("OK");
                        return;
                    }
                }
            }
        }
        System.out.println("there are no free train part with a such id");
    }
    //если поезд удален то его ид можно занять
    //должен ли id начинаться с 1 или может быть другое начало
    //нужно ли проверять train-set здесь
    //
    //became W-num
    public void addTrainWrap(int trainId, String trainPartId) {
        if(!istrainIdValid(trainId)) {
            System.out.println("Incorrect train id");
            return;
        }
        if(getTrainPart(trainPartId) == null) {
            System.out.println("Train Part with this Id doesn't exist");
            return;
        } else if(getTrainPart(trainPartId).getTrainId() != 0) {
            System.out.println("Train Part with this id is already in use");
            return;
        }
        //no train-id check
        Iterator<TrainPart> trainPartIterator = trainParts.iterator();
        for (int i = 0; i < trainParts.size(); i++) {
            TrainPart trainPart = trainPartIterator.next();
            if (Coach.class.isInstance(trainPart)) {
                if (trainPartId.equals("W" + ((Coach) trainPart).getId())) {
                    if (addTrain(trainId, trainPartId)) {
                        trainParts.get(i).setTrainId(trainId);
                        System.out.println(((Coach) trainPart).getType() + " coach " + trainPartId
                                + " added to " + trainId);
                        return;
                    } else {
                        System.out.println("Train Parts Connections don't pass");
                        return;
                    }
                }
            } else if (Engine.class.isInstance(trainPart)) {
                if ((((Engine) trainPart).getSpecialClass() + "-" + ((Engine) trainPart).getSpecialName()).equals(trainPartId)) {
                    if (addTrain(trainId, trainPartId)) {
                        trainParts.get(i).setTrainId(trainId);
                        System.out.println(((Engine) trainPart).getType() + " engine " + trainPartId
                                + " added to " + trainId);
                        return;
                    } else {
                        System.out.println("Train Parts Connections don't pass");
                        return;
                    }
                }
            } else if (TrainSet.class.isInstance(trainPart)) {
                if ((((TrainSet) trainPart).getSpecialClass() + "-" + ((TrainSet) trainPart).getSpecialName()).equals(trainPartId)) {
                    if (addTrain(trainId, trainPartId)) {
                        trainParts.get(i).setTrainId(trainId);
                        System.out.println("train-set " + trainPartId
                            + " added to " + trainId);
                        return;
                    } else {
                        System.out.println("Train Parts Connections don't pass");
                        return;
                    }
                }
            }
        }
        System.out.println("you shouldn't see this massage");
    }
    public String listTrains() {
        if(trains.size()==0)
            return "No train exists";
        String str = "";
        for(int i = 0; i < trains.size()-1;i++) {
            str += trains.get(i) + "\n";
        }
        str += trains.get(trains.size() - 1);
        return str;
    }
    public void deleteTrain(int trainId) {
        for (Train train: trains)
            if (train.getTrainId() == trainId) {
                freeUsedTrainParts(train);
                trains.remove(train);
            }
    }
    public TrainPart getTrainPart(String trainPartId) {
        if(trainPartId.matches("^W\\d+$"))
            trainPartId = trainPartId.substring(1);

        for(TrainPart trainPart : trainParts) {
            if(Coach.class.isInstance(trainPart))
                if(trainPartId.equals(Integer.toString(((Coach) trainPart).getId())))
                    return trainPart;
            if(Engine.class.isInstance(trainPart))
                if((((Engine) trainPart).getSpecialClass() + "-" + ((Engine) trainPart).getSpecialName()).equals(trainPartId))
                    return trainPart;
            if(TrainSet.class.isInstance(trainPart))
                if((((TrainSet) trainPart).getSpecialClass() + "-" + ((TrainSet) trainPart).getSpecialName()).equals(trainPartId))
                    return trainPart;
        }
        System.out.println("Train Part wasn't found");
        return null;
    }
    //returns false if connections dont fit
    //arg with W
    private boolean addTrain(int trainId, String trainPartId) {
        if(trainPartId.matches("^W\\d$"))
            trainPartId = trainPartId.substring(1);
        Iterator<Train> iterTrain = trains.iterator();
        //add train part to already existing train
        for(int i = 0; i < trains.size(); i++) {
            Train train = iterTrain.next();
            if(train.getTrainId() == trainId)
                if (getTrainPart(trains.get(i).getLastTrainPartId()).isBackConnection() &&
                        getTrainPart(trainPartId).isForwConnection()) {
                    if(Coach.class.isInstance(getTrainPart(trainPartId)))
                        trains.get(i).addTrainPart(trainPartId, true);
                    else
                        trains.get(i).addTrainPart(trainPartId, false);
                    return true;
                } else
                    return false;
        }
        Train train = new Train(trainId);
        if(Coach.class.isInstance(getTrainPart(trainPartId)))
            train.addTrainPart(trainPartId, true);
        else
            train.addTrainPart(trainPartId, false);
        trains.add(train);
        if(trainId == Train.getLastTrainId() + 1)
            Train.inrcLastTrainId();
        return true;
    }
    private boolean istrainIdValid(int trainId) {
        for(Train train : trains) {
            if(train.getTrainId() == trainId)
                return true;
        }
        if(trainId == getNextValidTrainId())
            return true;
        //System.out.println("incorrect trainId");
        return false;
    }
    private int getNextValidTrainId() {
        for(int i = 1; i < Train.getLastTrainId(); i++) {
            boolean idisUsed = false;
            for(Train train : trains) {
                if(train.getTrainId() == i)
                    idisUsed = true;
            }
            if(idisUsed)
                continue;
            return i;
        }
        return Train.getLastTrainId() + 1;
    }

    private void freeUsedTrainParts(Train deletedTrain) {
        String trainPartIds[] = deletedTrain.getTrainPartsIds();
        for(String trainPartId : trainPartIds) {
            getTrainPart(trainPartId).setTrainId(0);
        }
    }
}
