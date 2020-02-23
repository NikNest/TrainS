package RailWay;

import RailWay.utils.*;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
//32 16 bit check
//убрать паблики потом
//remove redundant casting
public class RWState {
    public RWState() {
        tracks = new ArrayList<>();
        trains = new ArrayList<>();
    }
    ArrayList<Track> tracks;
    ArrayList<TrainOnRoad> trains;
    public void step(short speed) {
        ArrayList<TrainOnRoad> movingTrains = new ArrayList<>();
        if (trains.size() == 0) {
            System.out.println("OK");
            return;
        }
        for(TrainOnRoad train : trains) {
            train = move(train, speed);
            movingTrains.add(train);
        }
        ArrayList<ArrayList<Integer>> crashes = findCrashes(movingTrains);
        String str = createStepString(movingTrains, crashes);
        System.out.println(str);
        removeCrashedTrains(crashes);
    }

    private TrainOnRoad move(TrainOnRoad train, int step) {
        if(step != 0) {
            //in case of negative step turning around
            if(step < 0) {
                train.setStartDirection(!train.isStartDirection());
                train.setMovingBack(true);
                step = -step;
            }
            ArrayList<Track> field = (ArrayList<Track>) tracks.clone();
            int i = 0;
            for (Track track : field) {
                boolean notTrackHead = track.getId() != train.getTrackHead().getId();
                boolean areStartConnected = track.getStart().equals(train.getTrackHead().getStart()) || track.getEnd().equals(train.getTrackHead().getStart());
                boolean areEndConnected = track.getStart().equals(train.getTrackHead().getEnd()) || track.getEnd().equals(train.getTrackHead().getEnd());
                boolean areConnectedEndDirection = train.isStartDirection() ? areEndConnected : areStartConnected;
                if(areConnectedEndDirection && notTrackHead)
                    break;
                i++;
            }
            //удаляем лишний поезд не по ходу движения
            field.remove(i);
            ArrayList<Track> forwardRoad;
            int stepLength = step;
            //counting road as ended struct, counting offset
            if (train.isStartDirection()) {
                forwardRoad = getEndedStruct(field, train.getTrackHead().getEnd(), 0);
                forwardRoad = removeCopies(forwardRoad);
                stepLength += Math.abs(train.getTrackHead().getEnd().getX() + train.getTrackHead().getEnd().getY()
                        - train.getPositionHead().getX() - train.getPositionHead().getY());
            } else {
                forwardRoad = getEndedStruct(field, train.getTrackHead().getStart(), 0);
                forwardRoad = removeCopies(forwardRoad);
                stepLength += Math.abs(train.getTrackHead().getStart().getX() + train.getTrackHead().getStart().getY()
                        - train.getPositionHead().getX() - train.getPositionHead().getY());
            }
            //check for crash with track
            if(stepLength > countLengthEndedStruct(forwardRoad)) {
                System.out.println("CRASH WITH TRACK");
                train.setCrashed(true);
                return train;
            }
            Collections.reverse(forwardRoad);
            //moving
            ArrayList<Track> tracksOfTrain = new ArrayList<>();
            for (Track track : forwardRoad) {
                if (stepLength > 0) {
                    tracksOfTrain.add(track);
                    stepLength -= track.getLength();
                }
            }
            for (Track track : tracksOfTrain) {
                System.out.print(track.getId() + " ");
            }
            //counting headpoint
            Track newHeadTrack;
            boolean startDirection;
            Point newHeadPoint;
            if(tracksOfTrain.size() == 1) {
                newHeadTrack = train.getTrackHead();
                startDirection = train.isStartDirection();
            } else {
                newHeadTrack = tracksOfTrain.get(tracksOfTrain.size() - 1);
                Track beforeHeadTrack = tracksOfTrain.get(tracksOfTrain.size() - 2);
                startDirection = newHeadTrack.getCommonPoint(beforeHeadTrack).equals(newHeadTrack.getEnd());
                System.out.println(newHeadTrack);
                System.out.println(beforeHeadTrack);
            }
            boolean positivDirection = isDirectionPositiv(newHeadTrack, startDirection);
            newHeadPoint = countHeadPoint(startDirection, positivDirection, stepLength, newHeadTrack);
            System.out.println("NEW HEAD POINT: " + newHeadPoint);
            if(train.isMovingBack()) {
                TrainOnRoad temp = new TrainOnRoad(train.getTrain(), newHeadTrack, !startDirection, newHeadPoint);
                if (crashesWithTrack(temp))
                    temp.setCrashed(true);
                return temp;
            } else
                return new TrainOnRoad(train.getTrain(), newHeadTrack, startDirection, newHeadPoint);
        } else
            return train;
    }
    private int countLengthEndedStruct(ArrayList<Track> endedStruct) {
        if(endedStruct.size()==0)
            return 0;
        else {
            int length = 0;
            for(Track track : endedStruct) {
                length += track.getLength();
            }
            return length;
        }
    }
    private Point countHeadPoint(boolean startDirection, boolean positivDirection, int stepLength, Track newHeadTrack) {
        Point newHeadPoint;
        if (newHeadTrack.getStart().getX() == newHeadTrack.getEnd().getX()) {
            System.out.println("SAME - X" + " LENGTH:" + stepLength + " DIRECTION: " + positivDirection + " START: " + startDirection);
            if (startDirection) {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX(),
                        newHeadTrack.getEnd().getY() + Math.abs(stepLength)) : new Point(newHeadTrack.getStart().getX(),
                        Math.abs(newHeadTrack.getStart().getY() - newHeadTrack.getEnd().getY()) + newHeadTrack.getEnd().getY() - Math.abs(stepLength));
            } else {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX(),
                        Math.abs(newHeadTrack.getStart().getY() - newHeadTrack.getEnd().getY()) + newHeadTrack.getStart().getY() - Math.abs(stepLength)) :
                        new Point(newHeadTrack.getStart().getX(), newHeadTrack.getEnd().getY() + Math.abs(stepLength));
            }
        } else {
            System.out.println("SAME - Y" + " LENGTH:" + stepLength + " DIRECTION: " + positivDirection + " START: " + startDirection);
            if (startDirection) {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getEnd().getX() + Math.abs(stepLength), newHeadTrack.getStart().getY()) :
                        new Point(Math.abs(newHeadTrack.getStart().getX() - newHeadTrack.getEnd().getX()) + newHeadTrack.getEnd().getX() - Math.abs(stepLength) , newHeadTrack.getStart().getY());
            } else {
                newHeadPoint = positivDirection ? new Point(Math.abs(newHeadTrack.getStart().getX() - newHeadTrack.getEnd().getX()) + newHeadTrack.getStart().getX() - Math.abs(stepLength), newHeadTrack.getStart().getY()) :
                        new Point(newHeadTrack.getEnd().getX() + Math.abs(stepLength), newHeadTrack.getStart().getY());
            }
        }
        return newHeadPoint;
    }
    private boolean isDirectionPositiv(Track track, boolean startDirection) {
        if (startDirection) {
            return (track.getStart().getX() + track.getStart().getY() - track.getEnd().getX() - track.getEnd().getY()) > 0;
        } else {
            return (track.getStart().getX() + track.getStart().getY() - track.getEnd().getX() - track.getEnd().getY()) < 0;
        }
    }
    private boolean crashesWithTrack(TrainOnRoad train) {
        ArrayList<Track> field = (ArrayList<Track>) tracks.clone();
        int i = 0;
        for (Track track : field) {
            if(track.getId() != train.getTrackHead().getId())
                if(train.isStartDirection())
                    if(track.getStart().equals(train.getTrackHead().getEnd()) || track.getEnd().equals(train.getTrackHead().getEnd())) {
                        break;
                    } else if (track.getStart().equals(train.getTrackHead().getStart()) || track.getEnd().equals(train.getTrackHead().getStart())) {
                        break;
                    } else {
                        continue;
                    }
            i++;
        }
        field.remove(i);
        ArrayList<Track> endedStruct = new ArrayList<>();
        int trainLength = train.getLength();
        if(train.isStartDirection()) {
            endedStruct = getEndedStruct(field, train.getTrackHead().getEnd(), 0);
            trainLength += Math.abs(train.getPositionHead().getX() + train.getPositionHead().getY()
                    - train.getTrackHead().getEnd().getX() - train.getTrackHead().getEnd().getY());
        } else {
            endedStruct = getEndedStruct(field, train.getTrackHead().getStart(), 0);
            trainLength += Math.abs(train.getPositionHead().getX() + train.getPositionHead().getY()
                    - train.getTrackHead().getStart().getX() - train.getTrackHead().getStart().getY());
        }
        Collections.reverse(endedStruct);
        for (Track track : endedStruct) {
            trainLength -= track.getLength();
        }
        if(trainLength>=0)
            return true;
        else
            return false;
    }
    //returns id of trains
    //needed to be sorted
    //crashes with tracks too
    private ArrayList<ArrayList<Integer>> findCrashes(ArrayList<TrainOnRoad> movingTrains) {
        ArrayList<ArrayList<Integer>> crashesId = new ArrayList<>();
        for(TrainOnRoad train1 : movingTrains) {
            ArrayList<Integer> crashIds = new ArrayList<Integer>();
            for (TrainOnRoad train2 : movingTrains) {
                if (haveCommonTracks(train1, train2)) {
                    train1.setCrashed(true);
                    train2.setCrashed(true);
                    crashIds.add(train1.getTrain().getTrainId());
                    crashIds.add(train2.getTrain().getTrainId());
                }
            }
            if (train1.isCrashed()) {
                crashIds.add(train1.getTrain().getTrainId());
            }

            Collections.sort(crashIds);
            HashSet<Integer> temp = new HashSet<>(crashIds);
            crashIds.addAll(temp);
            crashesId.add(crashIds);
        }
        Collections.sort(crashesId, new SortCrashesIds());
        return crashesId;
    }
    private String createStepString(ArrayList<TrainOnRoad> movingTrains, ArrayList<ArrayList<Integer>> idsCrashes) {
        String str = "";
        if(idsCrashes.size() == 0) {
                for (TrainOnRoad train : movingTrains) {
                    str += train + "\n";
                }
        }
        Iterator<TrainOnRoad> trainOnRoadIterator = movingTrains.iterator();
        for (TrainOnRoad train : movingTrains) {
            if (train.isCrashed()) {
                ArrayList<Integer> crashIds = new ArrayList<>();
                for (ArrayList<Integer> crashIdsTemp : idsCrashes) {
                    if (crashIdsTemp.get(0) == train.getTrain().getTrainId()) {
                        crashIds = crashIdsTemp;
                        break;
                    }
                }
                //mean that is already listed
                if (crashIds.size() == 0)
                    continue;
                else {
                    str += "Crash of train " + crashIds.get(0);
                    if (crashIds.size() > 1)
                    {
                        for(int q = 1; q < crashIds.size(); q++) {
                            str += "," + crashIds.get(q);
                        }
                    }
                    str += "\n";
                }
            } else
                str += train + "\n";
        }
        str.trim();
        return str;
    }
    private void removeCrashedTrains(ArrayList<ArrayList<Integer>> crashes) {
        ArrayList<Integer> trainsToDel = new ArrayList<>();
        int i = 0;
        for(TrainOnRoad train : trains) {
            if(train.isCrashed())
                trainsToDel.add(i);
            i++;
        }
        Collections.reverse(trainsToDel);
        for(int num : trainsToDel) {
            trains.remove(num);
        }
    }
    public void addTrack(Point start, Point end) {
        if (!start.equals(end) && ((start.getY() == end.getY()) || (start.getX() == end.getX()))) {
            Track temp = new Track(start, end);
            if(tracks.size()==0) {
                initTrackId(temp);
                tracks.add(temp);
                System.out.println(temp.getId());
            } else {
                for(Track track : tracks) {
                    if (temp.getCommonPoint(track) != null) {
                        if(SwitchTrack.class.isInstance(track)) {
                            ((SwitchTrack) track).connect(temp.getCommonPoint(track));
                        } else
                            track.connect(temp.getCommonPoint(track));
                            temp.connect(temp.getCommonPoint(track));
                            initTrackId(temp);
                            tracks.add(temp);
                            System.out.println(temp.getId());
                            return;
                    }
                }
                System.out.println("wrong connection");
            }
        } else
            System.out.println("invalid track: not straight or 0 length");
    }
    public void addSwitch(Point start, Point end, Point end2){
            if((!start.equals(end) && ((start.getY() == end.getY()) || (start.getX() == end.getX()))) &&
                (!start.equals(end2) && ((start.getY() == end2.getY()) || (start.getX() == end2.getX()))) &&
                    !end.equals(end2)) {
                SwitchTrack temp = new SwitchTrack(start, end, end2);
                if(tracks.size()==0) {
                    initTrackId(temp);
                    tracks.add(temp);
                    System.out.println(temp.getId());
                } else {
                    for (Track track : tracks) {
                        if (temp.getCommonPoint(track) != null) {
                            if (SwitchTrack.class.isInstance(track)) {
                                ((SwitchTrack) track).connect(temp.getCommonPoint(track));
                            } else
                                track.connect(temp.getCommonPoint(track));
                            temp.connect(temp.getCommonPoint(track));
                            initTrackId(temp);
                            tracks.add(temp);
                            System.out.println(temp.getId());
                            return;
                        }
                    }
                    System.out.println("wrong connection");
                }
            } else
                System.out.println("invalid switch-track: not straight");
    }
    public void setSwitch(int id, Point end) {
        for(Track track : tracks) {
            if(track.getId() == id) {
                if(!SwitchTrack.class.isInstance(track)) {
                    System.out.println("wrong switch id");
                    return;
                }
                if(((SwitchTrack)track).getEnd2().equals(end) || track.getEnd().equals(end) || track.getStart().equals(end)) {
                        ((SwitchTrack) track).setDirection(end);
                        System.out.println("OK");
                        return;
                } else {

                    System.out.println("wrong switch point");
                    return;
                }
            }
        }
        System.out.println("Track with this id doesn't exist");
    }
    public String listTracks() {
        String str = "";
        Iterator<Track> iter = tracks.iterator();
        while (iter.hasNext()) {
            Track track = iter.next();
            if(!(SwitchTrack.class.isInstance(track))) str += track;
            else str += (SwitchTrack)track;
            if(iter.hasNext())
                str += "\n";
        }
        if (str.equals(""))
            str = "No track exists";
        return Sorter.sortList(str, new SortNumTrack());
    }
    //TODO
    //add delete track check that no trains stay
    public void deleteTrack(int trackId) {
        for(Track track : tracks) {
            if(trackId == track.getId()) {
                if(isRWValidIfTrackDeleted(track)) {
                    System.out.println("TRACK TO REMOVE: " + track.getId());
                    for (Track track1 : tracks) {
                        System.out.print(track1.getId() + " ");
                    }

                    removeConnections(track);
                    int i = 0;
                    for (Track track1 : tracks) {
                        if (track1.getId() == track.getId())
                            break;
                        i++;
                    }
                    tracks.remove(i);
                    System.out.println("OK");
                    return;
                }
                System.out.println("track with this id couldn't be deleted");
                return;
            }
        }
        System.out.println("track with this id not found");
    }
    private Point getPointFromDirection(Point pointFrom, Point direction) {

        return new Point(pointFrom.getX() + direction.getX(), pointFrom.getY() + direction.getY());
    }
    public void putTrain(Train train, Point pointFrom, Point direction) {
        direction = getPointFromDirection(pointFrom, direction);
        for(Track track : tracks) {
            if(track instanceof SwitchTrack) {
                if(!((SwitchTrack) track).isSwitchSetted()) {
                    System.out.println("Not all the switches are setted!");
                    return;
                }
            }
        }
        if(!pointFrom.equals(direction) && ((pointFrom.getX() == direction.getX()) || (pointFrom.getY() == direction.getY())))
        {
            if (train.isTrainValid()) {
                //проверить как находит на углах
                    Track track = findTrack(pointFrom, direction);
//                System.out.println("TRACK: " + track + " dir: " + direction);
//                    System.out.println("TRACK DETERMINED: " + track.getId());
                    if(track == null) {
                        System.out.println("track not found");
                        return;
                    }
                    TrainOnRoad trainOnRoad = new TrainOnRoad(train, track, isStartDirection(track, pointFrom, direction), pointFrom);
                    ArrayList<Track> temp = (ArrayList<Track>) tracks.clone();
                    //changing direction for reverse search, counting the length on the track
                    int availableTracksLength;
                    if (isStartDirection(track, pointFrom, direction)) {
                        direction = track.getEnd();
                        availableTracksLength = Math.abs(pointFrom.getX() + pointFrom.getY()
                                - track.getEnd().getX() - track.getEnd().getY());

                    } else {
                        direction = track.getStart();
                        availableTracksLength = Math.abs(pointFrom.getX() + pointFrom.getY()
                                - track.getStart().getX() - track.getStart().getY());
                    }
                    int i = 0;
                    for(Track track1 : tracks) {
//                        System.out.println("DIR: " + direction + " Start: " + track1.getStart() + " End: " + track1.getEnd());
//                        if(!track1.equals(track) && (track1.getStart().equals(direction) || track1.getEnd().equals(direction))) {
//                            System.out.println("TRACK: " + track1.getId());
//                            break;
//                        }
                        if (track1.getId() == track.getId()) {
                            break;
                        }
                        i++;

                    }
                    temp.remove(i);

//                    System.out.println("ENDED TRACK SEARCH: ");
//                    for(Track track1 : temp) {
//                        System.out.print(track1.getId() + " ");
//                    }
//                System.out.println("\nDIRECTION SEARCH: " + direction);
                    ArrayList<Track> endedStruct = getEndedStruct(temp, direction, 0);
                    endedStruct = removeCopies(endedStruct);

//                System.out.print("ENDED TRACK: ");
                    for(Track track1 : endedStruct) {
//                        System.out.print(track1.getId() + " ");
                        availableTracksLength += track1.getLength();
                    }
//                    System.out.println("\nLength: " + availableTracksLength);

                    if(availableTracksLength < train.getTrainLength()) {
                        System.out.println("there are not enogh place for this train");
                        return;
                    }
                    for(TrainOnRoad tempTrainOnRoad : trains) {
                        if(haveCommonTracks(tempTrainOnRoad, trainOnRoad)) {
                            System.out.println("there is a train already on these tracks");
                            return;
                        }
                    }
                    trains.add(trainOnRoad);
                    System.out.println("OK");

                } else {
                System.out.println("Train isn't valid");
            }
        } else {
            System.out.println("wrong direction");
        }
    }
    private ArrayList<Track> removeCopies(ArrayList<Track> overextendedField) {
        Set<Track> set = new LinkedHashSet<>(overextendedField);
//        System.out.println("Set reading: ");
//        for(Track track : set) {
//            System.out.print(track.getId() + " ");
//        }
//        System.out.println();
        return new ArrayList<>(set);
    }
    //vorausgesetzt dass der Punkt auf der Strecke vom Track liegt
    private boolean isStartDirection(Track track, Point pointfrom, Point direction) {
        int dirstart = track.getStart().getY() + track.getStart().getX() - pointfrom.getX() - pointfrom.getY() >= 0 ? 1 : 0;
        int dirdir = direction.getX() + direction.getY() - pointfrom.getX() - pointfrom.getY() >= 0 ? 1 : 0;
        return dirstart == dirdir;
    }
    private Track findTrack(Point pointFrom, Point direction) {
        for(Track track : tracks) {
            if(pointBelongsTrack(pointFrom, track)) {
                boolean dirTrack = track.getEnd().getY() == track.getStart().getY();
                boolean dirSetted = pointFrom.getY() == direction.getY();
                if(dirTrack == dirSetted) {
                    if(pointFrom.equals(track.getEnd())) {
                        //positiv direction
                        boolean trackDirection = track.getEnd().getX() + track.getEnd().getY() - track.getStart().getY() - track.getStart().getX() > 0;
                        boolean directionOutside = trackDirection ? direction.getX() + direction.getY() - track.getEnd().getX() - track.getEnd().getY() > 0
                                : direction.getX() + direction.getY() - track.getEnd().getX() - track.getEnd().getY() < 0;
                        if(!directionOutside) {
                            for (Track track1 : tracks) {
                                if ((track1.getId() != track.getId()) &&
                                        (track1.getStart().equals(pointFrom) || track1.getEnd().equals(pointFrom))) {
                                    dirTrack = track1.getEnd().getY() == track1.getStart().getY();
                                    dirSetted = pointFrom.getY() == direction.getY();
                                    if (dirTrack == dirSetted)
                                        return track1;
                                    else
                                        return null;
                                }
                            }
                        } else
                            return track;
                    } else if (pointFrom.equals(track.getStart())) {
//                        System.out.println("side: " + (direction.getX() + direction.getY() - track.getEnd().getX() - track.getEnd().getY())
//                         + "length: " + (-(track.getEnd().getX() + track.getEnd().getY() - track.getStart().getX() - track.getStart().getY())));
                        boolean trackDirection = track.getEnd().getX() + track.getEnd().getY() - track.getStart().getY() - track.getStart().getX() > 0;
                        boolean directionOutside = !trackDirection ? direction.getX() + direction.getY() - track.getStart().getX() - track.getStart().getY() > 0
                                : direction.getX() + direction.getY() - track.getStart().getX() - track.getStart().getY() < 0;
                        if(!directionOutside) {
                            for (Track track1 : tracks) {
                                if ((track1.getId() != track.getId()) &&
                                        (track1.getStart().equals(pointFrom) || track1.getEnd().equals(pointFrom))) {
                                    dirTrack = track1.getEnd().getY() == track1.getStart().getY();
                                    dirSetted = pointFrom.getY() == direction.getY();
                                    if (dirTrack == dirSetted)
                                        return track1;
                                    else
                                        return null;
                                }
                            }
                        } else
                            return track;
                    } else {
                        return track;
                    }
                }
//                System.out.println("Point : " + pointFrom + " belongs to the track: " + track.getId());
//                System.out.println(dirTrack + " " + dirSetted);
            }
        }
        return null;
    }
    //for put
    public boolean haveCommonTracks(TrainOnRoad trainOnRoad1, TrainOnRoad trainOnRoad2) {
        if(crossHappens(trainOnRoad1, trainOnRoad2))
            return true;
        ArrayList<Track> tracksOfFirst = new ArrayList<>();
        ArrayList<Track> tracksOfSecond = new ArrayList<>();
        ArrayList<Track> field = (ArrayList<Track>) tracks.clone();
        int i = 0;
        for(Track track1 : field) {
            if(track1.getId() == trainOnRoad1.getTrackHead().getId())
                break;
            i++;
        }
        field.remove(i);
        ArrayList<Track> endedStructFirst = trainOnRoad1.isStartDirection() ? getEndedStruct(field, trainOnRoad1.getTrackHead().getEnd(), 0) :
                getEndedStruct(field, trainOnRoad1.getTrackHead().getStart(), 0);
        Collections.reverse(endedStructFirst);

        int firstTrainsLength = trainOnRoad1.getLength() - trainOnRoad1.getLengthHead();
        for(Track track1 : endedStructFirst) {
            if(firstTrainsLength > 0) {
                firstTrainsLength -= track1.getLength();
                tracksOfFirst.add(track1);
            }
        }
        field = (ArrayList<Track>) tracks.clone();
        i = 0;
        for(Track track1 : field) {
            if(track1.getId() == trainOnRoad2.getTrackHead().getId())
                break;
            i++;
        }
        field.remove(i);
        ArrayList<Track> endedStructSecond = trainOnRoad2.isStartDirection() ? getEndedStruct(field, trainOnRoad2.getTrackHead().getEnd(), 0) :
                getEndedStruct(field, trainOnRoad2.getTrackHead().getStart(), 0);
        Collections.reverse(endedStructSecond);

        int secondTrainsLength = trainOnRoad2.getLength() - trainOnRoad2.getLengthHead();
        for(Track track1 : endedStructSecond) {
            if(firstTrainsLength > 0) {
                secondTrainsLength -= track1.getLength();
                tracksOfSecond.add(track1);
            }
        }
        tracksOfFirst.add(trainOnRoad1.getTrackHead());
//        System.out.println("\nTracks of first train: ");
//        for (Track track1 : tracksOfFirst) {
//            System.out.print(track1.getId() + " ");
//        }
        tracksOfSecond.add(trainOnRoad2.getTrackHead());
//        System.out.println("\nTracks of second train: ");
//        for (Track track1 : tracksOfSecond) {
//            System.out.print(track1.getId() + " ");
//        }
        for(Track track1 : tracksOfFirst) {
            for (Track track2 : tracksOfSecond) {
                boolean areConnected = track1.getStart().equals(track2.getStart()) || track1.getStart().equals(track2.getEnd()) ||
                        track1.getEnd().equals(track2.getStart()) || track1.getEnd().equals(track2.getEnd());
                if (areConnected && firstTrainsLength == 0 && secondTrainsLength == 0)
                    return true;
                if (track1.getId() == track2.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
    //for step
    public boolean crossHappens(TrainOnRoad trainOnRoad1, TrainOnRoad trainOnRoad2) {
        ArrayList<Point> coordsOfFirst = new ArrayList<>();
        ArrayList<Point> coordsOfSecond = new ArrayList<>();
        int firstlength = trainOnRoad1.getLength();
        int secondlength = trainOnRoad2.getLength();
        Track track = trainOnRoad1.getTrackHead();
        Point lastPoint = trainOnRoad1.getPositionHead();
        boolean directionToStart = trainOnRoad1.isStartDirection();
        while(firstlength > 0) {
            Point nextPoint = track.getNextPoint(lastPoint, directionToStart);
            coordsOfFirst.add(nextPoint);
            if(directionToStart) {
                if (nextPoint.equals(track.getStart())) {
                    track = getNextTrack(track, track.getStart());
                    directionToStart = track.getEnd().equals(nextPoint);
                }
            } else {
                if (nextPoint.equals(track.getEnd())) {
                    track = getNextTrack(track, track.getEnd());
                    directionToStart = track.getEnd().equals(nextPoint);
                }
            }
            firstlength--;
        }

        track = trainOnRoad2.getTrackHead();
        lastPoint = trainOnRoad2.getPositionHead();
        directionToStart = trainOnRoad2.isStartDirection();
        while(secondlength > 0) {
            Point nextPoint = track.getNextPoint(lastPoint, directionToStart);
            coordsOfSecond.add(nextPoint);
            if(directionToStart) {
                if (nextPoint.equals(track.getStart())) {
                    track = getNextTrack(track, track.getStart());
                    directionToStart = track.getEnd().equals(nextPoint);
                }
            } else {
                if (nextPoint.equals(track.getEnd())) {
                    track = getNextTrack(track, track.getEnd());
                    directionToStart = track.getEnd().equals(nextPoint);
                }
            }
            secondlength--;
        }

        for(Point coord1 : coordsOfFirst) {
            for(Point coord2 : coordsOfSecond) {
                if(coord1.equals(coord2))
                    return true;
            }
        }
        return false;
    }
    private Track getNextTrack(Track track, Point lastTrackPoint) {
        for(Track tempTrack : tracks) {
            if(SwitchTrack.class.isInstance(tempTrack)) {
                if(!tempTrack.equals(track) && tempTrack.getStart().equals(lastTrackPoint) || tempTrack.getEnd().equals(lastTrackPoint)
                        || ((SwitchTrack)tempTrack).getEnd2().equals(lastTrackPoint)) {
                    return tempTrack;
                }
            } else {
                if(!tempTrack.equals(track) && (tempTrack.getStart().equals(lastTrackPoint) || tempTrack.getEnd().equals(lastTrackPoint)))
                    return tempTrack;
            }
        }
        return null;
    }
    //works when switch are setted
    private boolean pointBelongsTrack(Point point, Track track) {
        if(track.getSameCoord() == point.getX()) {
            int trackLength = Math.abs(track.getEnd().getY() - track.getStart().getY());
            return Math.abs(point.getY() - track.getEnd().getY()) <= trackLength
                    && Math.abs(point.getY() - track.getStart().getY()) <= trackLength;
        } else if (track.getSameCoord() == point.getY()) {
            int trackLength = Math.abs(track.getEnd().getX() - track.getStart().getX());
            return Math.abs(point.getX() - track.getEnd().getX()) <= trackLength
                    && Math.abs(point.getX() - track.getStart().getX()) <= trackLength;
        } else
            return false;
    }
    private void initTrackId(Track track) {
        if(track.getId()==0) {
            int id = 0;
            for (Track temp : tracks) {
                if (temp.getId() == id + 1)
                    id = temp.getId();
            }
            track.setId(++id);
        }
    }
    private boolean isRWValidIfTrackDeleted(Track track) {
        ArrayList<Track> tempTracks = (ArrayList<Track>) tracks.clone();
        int i = 0;

        for(Track track1 : tempTracks) {
            if(track1.getId() == track.getId())
                break;
            i++;
        }

        tempTracks.remove(i);
        ArrayList<Track> endedStruct;
        if(SwitchTrack.class.isInstance(track)) {
            Point direction1 = track.getEnd();
            Point direction2 = track.getStart();
            Point direction3 = null;
            if(!((SwitchTrack)track).isSwitchSetted())
                direction3 = ((SwitchTrack)track).getEnd2();

            ArrayList<Track> dir1struct = getEndedStruct(tempTracks, direction1, 0);
            ArrayList<Track> dir2struct = getEndedStruct(tempTracks, direction2, 0);
            ArrayList<Track> dir3struct = getEndedStruct(tempTracks, direction3, 0);
            endedStruct = combineEndedStructs(dir3struct, combineEndedStructs(dir1struct, dir2struct));
        } else {
            Point direction1 = track.getStart();
            Point direction2 = track.getEnd();
            endedStruct = combineEndedStructs(getEndedStruct(tempTracks, direction1, 0), getEndedStruct(tempTracks, direction2, 0));
        }
        endedStruct.add(track);


        tempTracks = (ArrayList<Track>) tracks.clone();
        //remove track
        tempTracks.remove(i);
        ArrayList<Track> endedStructCheck = getEndedStruct(tempTracks, track.getStart(), 0);
        if (endedStructCheck.size() == 0)
            return true;
        else {
            endedStructCheck.add(track);
//            System.out.println("FOUND: ");
//            for(Track track1 : endedStructCheck) {
//                System.out.print(track1.getId() + " ");
//            }
//            System.out.println("\nCHECK: ");
//            for(Track track1 : endedStruct) {
//                System.out.print(track1.getId() + " ");
//            }
            return areSameRWStructs(endedStruct, endedStructCheck);
        }
//        System.out.println(":");
//        System.out.println("\nstruct:");
//        for (Track track1 : endedStruct) {
//            System.out.print(track1.getId() + " ");
//        }
//        System.out.println();
//        for (Track track1 : tracks) {
//            System.out.print(track1.getId() + " ");
//        }
//        System.out.println();
    }
    private boolean isPartOf(ArrayList<Track> endedStruct, ArrayList<ArrayList<Track>> endedStructs) {
        for(ArrayList<Track> temp : endedStructs) {
            if(!areSameRWStructs(temp, endedStruct))
                return false;
        }
        return true;
    }
    private ArrayList<ArrayList<Track>> getEndedStructsOfRW() {
        ArrayList<ArrayList<Track>> endedStructs = new ArrayList<>();
        ArrayList<Track> field = (ArrayList<Track>) tracks.clone();
        for(Track track : field) {
            ArrayList<Track> tempField = (ArrayList<Track>) tracks.clone();
//            System.out.println("\nTRACK ES: " + track.getId());
//            tempField.remove(tempField.indexOf(track));
            int i = 0;
            for (Track track1 : tempField) {
                if (track1.getId() == track.getId())
                    break;
                i++;
            }
            tempField.remove(i);
            tempField = combineEndedStructs(getEndedStruct(tempField, track.getStart(), 0),
                    getEndedStruct(tempField, track.getEnd(), 0));
            tempField.add(track);
//            System.out.println("ES: ");
//            for(Track track1 : tempField) {
//                System.out.print(track1.getId() + " ");
//            }
//            endedStructs.add(tempField);
            if(endedStructs != null)
            for(ArrayList<Track> temp : endedStructs) {
                if(!areSameRWStructs(temp, tempField)) {
                    endedStructs.add(tempField);
                    break;
                }
            }
            else
                endedStructs.add(tempField);
        }
        return endedStructs;
    }
    private boolean areSameRWStructs(ArrayList<Track> struct1, ArrayList<Track> struct2) {
        if(struct1 == null || struct2 == null)
            return false;

        for(Track track1 : struct1) {
            boolean common = false;
            for(Track track2 : struct2) {
                if(track1.getId()==track2.getId())
                    common = true;
            }
            if(!common)
                return false;
        }
        for(Track track2 : struct2) {
            boolean common = false;
            for(Track track1 : struct1) {
                if(track1.getId()==track2.getId())
                    common = true;
            }
            if(!common)
                return false;
        }
        return true;
    }
    private ArrayList<Track> combineEndedStructs(ArrayList<Track> struct1, ArrayList<Track> struct2) {
        if(struct1 == null)
            return struct2;
        else if(struct2 == null)
            return struct1;
        Set<Track> set = new LinkedHashSet<>(struct1);
        set.addAll(struct2);
        return new ArrayList<>(set);
    }
    //there are no track from one side of the point
    //=> we dont need track as an argument
    //переписать или проверить вевести логику. нормальный вывод а не буквы
    public ArrayList<Track> getTracks() {
        return tracks;
    }
    public ArrayList<Track> getEndedStruct(ArrayList<Track> field, Point lastPoint, int depth) {
//        System.out.println("g" + depth);
//        for(Track track : field) {
//            System.out.print(track.getId() + " ");
//        }
//        System.out.println();
        if(field.size()==0)
            return new ArrayList<>();
        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
        for(Track tempTrack : field) {
            if (SwitchTrack.class.isInstance(tempTrack)) {
                if(((SwitchTrack) tempTrack).isSwitchSetted()) {
                    if (((SwitchTrack) tempTrack).getStart().equals(lastPoint)) {
//                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = tempTrack.getEnd();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
//                        System.out.println("switch setted, end " + tempTrack.getId());
                        temp.remove(i);
                        ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1, 1 + depth);
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (((SwitchTrack) tempTrack).getEnd().equals(lastPoint)) {
//                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = tempTrack.getStart();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
//                        System.out.println("switch setted, start " + tempTrack.getId());
                        temp.remove(i);
//                        for(Track track : temp) {
//                            System.out.print(track.getId() + " ");
//                        }
//                        System.out.println();
                        ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1, 1 + depth);
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if(((SwitchTrack) tempTrack).getEnd2().equals(lastPoint))
                        return new ArrayList<>();
                } else {
                    if ((tempTrack.getStart().equals(lastPoint))) {
//                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();

                        int i = 0;
                        Point direction1 = tempTrack.getEnd();
                        Point direction2 = ((SwitchTrack)tempTrack).getEnd2();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
//                        System.out.println("switch, end1,2 " + tempTrack.getId());
                        temp.remove(i);

                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct(temp, direction1, 1 + depth),
                                getEndedStruct((ArrayList<Track>) temp.clone(), direction2, 1 + depth));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (tempTrack.getEnd().equals(lastPoint)) {
//                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = tempTrack.getStart();
                        Point direction2 = ((SwitchTrack)tempTrack).getEnd2();

                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
//                        System.out.println("switch, start, end2 " + tempTrack.getId());
//                        for(Track track : temp) {
//                            System.out.print(track.getId() + " ");
//                        }
//                        System.out.println();
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct((ArrayList<Track>) temp.clone(), direction1, ++depth),
                                getEndedStruct((ArrayList<Track>) temp.clone(), direction2, ++depth));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (((SwitchTrack) tempTrack).getEnd2().equals(lastPoint)) {
//                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = tempTrack.getStart();
                        Point direction2 = tempTrack.getEnd();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
//                        System.out.println("switch, start end " + tempTrack.getId());
                        temp.remove(i);
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct((ArrayList<Track>) temp.clone(), direction1, 1 + depth),
                                getEndedStruct(temp, direction2, 1 + depth));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    }
                }
            } else {
                if (tempTrack.getStart().equals(lastPoint)) {
//                    ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                    int i = 0;
                    Point direction1 = tempTrack.getEnd();
                    for(Track trackInTemp : temp) {
                        if(trackInTemp.getId() == tempTrack.getId()) {
                            break;
                        }
                        i++;
                    }
//                    System.out.println("end " + tempTrack.getId());
                    temp.remove(i);
                    ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1, 1+depth);
                    endedStruct.add(tempTrack);
                    return endedStruct;
                } else if (tempTrack.getEnd().equals(lastPoint)) {
//                    ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                    int i = 0;
                    Point direction1 = tempTrack.getStart();
                    for(Track trackInTemp : temp) {
                        if(trackInTemp.getId() == tempTrack.getId()) {
                            break;
                        }
                        i++;
                    }
//                    System.out.println("start "+ tempTrack.getId());
                    temp.remove(i);
                    ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1, 1+depth);
                    endedStruct.add(tempTrack);
                    return endedStruct;
                }
            }
        }
//        System.out.println("nothing was found");
        //case when no connections where found
        return new ArrayList<>();

    }
    private void removeConnections(Track track) {

            if(SwitchTrack.class.isInstance(track)) {
                Point point1 = track.getStart();
                Point point2 = track.getEnd();
                Point point3 = ((SwitchTrack) track).getEnd2();
                for(Track track1 : tracks) {
                    if ((SwitchTrack.class.isInstance(track1)) && (((SwitchTrack) track1).getEnd2().equals(point1) ||
                            ((SwitchTrack) track1).getEnd2().equals(point2) || ((SwitchTrack) track1).getEnd2().equals(point3))) {
                        ((SwitchTrack)track1).setSecondendConnected(false);
                    }
                    if (track1.getEnd().equals(point1) || track1.getEnd().equals(point2) || track1.getEnd().equals(point3)) {
                        track1.setEndConnected(false);
                    }
                    if (track1.getStart().equals(point1) || track1.getStart().equals(point2) || track1.getStart().equals(point3)) {
                        track1.setStartConnected(false);
                    }
                }
            } else {
                Point point1 = track.getStart();
                Point point2 = track.getEnd();
                for(Track track1 : tracks) {
                    if ((SwitchTrack.class.isInstance(track1)) && (((SwitchTrack) track1).getEnd2().equals(point1) ||
                            ((SwitchTrack) track1).getEnd2().equals(point2))) {
                        ((SwitchTrack)track1).setSecondendConnected(false);
                    }
                    if (track1.getEnd().equals(point1) || track1.getEnd().equals(point2)) {
                        track1.setEndConnected(false);
                    }
                    if (track1.getStart().equals(point1) || track1.getStart().equals(point2)) {
                        track1.setStartConnected(false);
                    }
                }
            }

    }
}
