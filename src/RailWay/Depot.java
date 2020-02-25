package RailWay;

import RailWay.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class Depot {
    public Depot() {
        trainParts = new ArrayList<>();
        trains = new ArrayList<>();
    }
    private ArrayList<TrainPart> trainParts;
    private ArrayList<Train> trains;
    //need regex
    public final void createCoach(String coachType, int length, boolean forwConnected, boolean backConnected) {
        int idnext=0;
        List<TrainPart> coaches = trainParts.stream().filter(tp -> Coach.class.isInstance(tp)).collect(Collectors.toList());
        for(TrainPart temp : coaches) {
            int id = ((Coach)temp).getId();
            if(id==idnext+1)
                idnext = id;
        }
        idnext++;
        TrainPart coach = new Coach(coachType, length, forwConnected, backConnected, idnext);
        System.out.println(((Coach)coach).getId());
        trainParts.add(coach);
    }
    //need regex
    public final void createEngine(String type, String engineClass, String engineName,
                             int length, boolean forwConnected, boolean backConnected) throws IncorrectInputException {
        TrainPart engine = new Engine(type, engineClass, engineName, length, forwConnected, backConnected);
        if(trainParts.size()!=0)
        for(TrainPart trainPart : trainParts) {
            if(SpecialIdable.class.isInstance(trainPart)) {
                if (SpecialIdable.haveSameSpecialId((SpecialIdable) trainPart,(SpecialIdable) engine)) {
                    throw new IncorrectInputException("There is already a trainpart with the same id");
                }
            }
        }
        System.out.println(engineClass + "-" + engineName);
        trainParts.add(engine);
    }
    //need regex
    public final void createTrainSet(String trainsetClass, String trainsetName,
                         int length, boolean forwConnected, boolean backConnected) throws IncorrectInputException{
        TrainPart trainset = new TrainSet(trainsetClass, trainsetName, length, forwConnected, backConnected);
        if(trainParts.size()!=0)
        for(TrainPart trainPart : trainParts) {
           if(SpecialIdable.class.isInstance(trainPart)) {

               if (SpecialIdable.haveSameSpecialId((SpecialIdable) trainPart,(SpecialIdable) trainset)) {
                   throw new IncorrectInputException("There is already a trainpart with the same id");
               }
           }
        }
        System.out.println(trainsetClass + "-" + trainsetName);
        trainParts.add(trainset);
   }
    public final String listEngines() {
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
        return Sorter.sortList(str, new SortSpecialId());
    }
    //train parts id here should be without 'W' prefix
    public final String listCoaches() {
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
        return Sorter.sortList(str, new SortNumCoach());
    }
    public final String listTrainSets() {
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
        return Sorter.sortList(str, new SortSpecialId());
    }
    public final void deleteRollingStock(String id) throws IncorrectInputException {
        TrainPart trainPart = getTrainPart(id);
        if(trainPart.getTrainId() != 0) {
            throw new IncorrectInputException("there are no free train part with a such id");
        } else {
            trainParts.remove(trainPart);
            System.out.println("OK");
        }
    }
    public final void addTrain(int trainId, String trainPartId) throws IncorrectInputException{
        if(!istrainIdValid(trainId)) {
            throw new IncorrectInputException("Incorrect input id");
        }
        if(trainPartId.matches("^W\\d$") && !trainPartId.matches("^\\w+-\\w+"))
            trainPartId = trainPartId.substring(1);
        TrainPart trainPart = getTrainPart(trainPartId);
        if(trainPart == null) {
            throw new IncorrectInputException("Train Part with this Id doesn't exist");
        } else if(trainPart.getTrainId() != 0) {
            throw new IncorrectInputException("Train Part with this id is already in use");
        }
        if (Coach.class.isInstance(trainPart)) {
                    if (connectTrainPart(trainId, trainPart)) {
                        //TODO чтобы нельзя было одну часть ав поезд дважды запихнуть                        trainPart.setTrainId(trainId);
                        System.out.println(((Coach) trainPart).getType() + " coach " + trainPartId
                                + " added to train " + trainId);
                        return;
                    } else {
                        throw new IncorrectInputException("Train Parts Connections don't pass");
                    }
        } else if (Engine.class.isInstance(trainPart)) {
                    if (connectTrainPart(trainId, trainPart)) {
                        trainPart.setTrainId(trainId);
                        System.out.println(((Engine) trainPart).getType() + " engine " + trainPartId
                                + " added to " + trainId);
                        return;
                    } else {
                        throw new IncorrectInputException("Train Parts Connections don't pass");
                                            }
        } else if (TrainSet.class.isInstance(trainPart)) {
                    if (connectTrainPart(trainId, trainPart)) {
                        trainPart.setTrainId(trainId);
                        System.out.println("train-set " + trainPartId
                            + " added to " + trainId);
                        return;
                    } else {
                        throw new IncorrectInputException("Train Parts Connections don't pass");
                    }
        }
    }
    public final String listTrains() {
        if(trains.size()==0)
            return "No train exists";
        String str = "";
        for(int i = 0; i < trains.size()-1;i++) {
            str += trains.get(i) + "\n";
        }
        str += trains.get(trains.size() - 1);
        return Sorter.sortList(str, new SortTrains());
    }
    public final void deleteTrain(int trainId) throws IncorrectInputException{
        for (Train train: trains)
            if (train.getTrainId() == trainId) {
                train.freeUsedTrainParts();
                trains.remove(train);
                System.out.println("OK");
                return;
            }
        throw new IncorrectInputException("There are no train with such id");
    }
    public final TrainPart getTrainPart(String trainPartId) throws IncorrectInputException{
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
        throw new IncorrectInputException("Train part wasn't found");
    }
    public final void showTrain(int trainId) throws IncorrectInputException{
        Optional<Train> trainToPrint = trains.stream().filter(train -> train.getTrainId() == trainId).findFirst();
        if(trainToPrint.isPresent())
            System.out.println(trainToPrint.get().showTrain());
        else
            throw new IncorrectInputException("Wrong Train Id");
    }
    //use this method for train finding
    public final Train getTrain(int trainId) {
        for(Train train : trains) {
            if(train.getTrainId() == trainId)
                return train;
        }
        System.out.println("Train not found");
        return null;
    }
    //returns false if connections dont fit
    private boolean connectTrainPart(int trainId, TrainPart trainPart) {
        for(Train train : trains) {
            if(train.getTrainId() == trainId)
                if (train.getLastTrainPart().isBackConnection() &&
                        trainPart.isForwConnection()) {
                    if(TrainSet.class.isInstance(train.getLastTrainPart())) {
                        if (TrainSet.class.isInstance(trainPart)) {
                            String specialIdprev = ((TrainSet) train.getLastTrainPart()).getSpecialClass();
                            String specialId = ((TrainSet) trainPart).getSpecialClass();
                            if (specialIdprev.equals(specialId)) {
                                train.addTrainPart(trainPart);
                                return true;
                            } else {
                                //train-set classes don't match
                                return false;
                            }
                        } else
                            //case train-set + no  train-set
                            return false;
                    } else
                        if(TrainSet.class.isInstance(trainPart))
                            //case no train-set + train - set
                            return false;
                    //engine and coach case
                    train.addTrainPart(trainPart);
                    return true;
                } else
                    //case wrong connection
                    return false;
        }
        Train train = new Train(trainId);
        train.addTrainPart(trainPart);
        trains.add(train);
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
       int id = 0;
       for(Train train : trains) {
           if(train.getTrainId() == id + 1)
               id = train.getTrainId();
       }
       return ++id;
    }

}
