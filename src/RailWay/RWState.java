package RailWay;

import RailWay.utils.*;
import org.w3c.dom.ls.LSOutput;

import java.util.*;
//32 16 bit check
//убрать паблики потом
//remove redundant casting
public final class RWState {
    public RWState() {
        tracks = new ArrayList<>();
        trains = new ArrayList<>();
    }
    private ArrayList<Track> tracks;
    private ArrayList<TrainOnRoad> trains;
    public final ArrayList<TrainOnRoad> updTrainsState() {
        ArrayList<TrainOnRoad> temp = new ArrayList<>();
        for(TrainOnRoad train : trains) {
            if (train.getTrain() != null)
                temp.add(train);
        }
        return temp;
    }
    public final void step(short speed) throws IncorrectInputException{
        for(Track track : tracks) {
            if (SwitchTrack.class.isInstance(track))
                if (!((SwitchTrack)track).isSwitchSetted())
                    throw new IncorrectInputException("not all the switches are setted");
        }
        trains = updTrainsState();
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
        trains = removeCrashedTrains(movingTrains);
    }
    public final void addTrack(Point start, Point end) throws IncorrectInputException{
        if (!start.equals(end) && ((start.getY() == end.getY()) || (start.getX() == end.getX()))) {
            Track temp = new Track(start, end);
            if(tracks.size()==0) {
                initTrackId(temp);
                tracks.add(temp);
                System.out.println(temp.getId());
            } else {
                for(Track track : tracks) {
                    if(temp.areIlligalConnected(track))
                        throw new IncorrectInputException("There is a track already on these points");
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
                throw new IncorrectInputException("wrong connection");
            }
        } else
            throw new IncorrectInputException("invalid track: not straight or 0 length");
    }
    public final void addSwitch(Point start, Point end, Point end2) throws IncorrectInputException{
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
                        if(temp.areIlligalConnected(track))
                            throw new IncorrectInputException("There is a track already on these points");
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
                    throw new IncorrectInputException("wrong connection");
                }
            } else
                throw new IncorrectInputException("invalid switch-track: not straight");
    }
    public final void setSwitch(int id, Point end) throws IncorrectInputException{
        trains = updTrainsState();
        for(Track track : tracks) {
            if(track.getId() == id) {
                if(!SwitchTrack.class.isInstance(track)) {
                  throw new IncorrectInputException("Wrong switch id");
                }
                if(((SwitchTrack)track).getEnd2().equals(end) || track.getEnd().equals(end) || track.getStart().equals(end)) {
                        ((SwitchTrack) track).setDirection(end);
                        if(!crashesTrainOnSwitch(track))
                            System.out.println("OK");
                        else {
                            int i = 0;
                            for (TrainOnRoad train : trains) {
                                if(train.isCrashed())
                                    break;
                                i++;
                            }
                            System.out.println("Crash of train " + trains.get(i).getTrain().getTrainId());
                            trains.remove(i);
                        }
                        return;
                } else {
                    throw new IncorrectInputException("wrong switch point");
                }
            }
        }
        throw new IncorrectInputException("Track with this id doesn't exist");
    }
    public final String listTracks() {
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
    public final void deleteTrack(int trackId) throws IncorrectInputException{
        trains = updTrainsState();
        for(Track track : tracks) {
            if(trackId == track.getId()) {
                if(isRWValidIfTrackDeleted(track)) {
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
                throw new IncorrectInputException("track with this id couldn't be deleted");
            }
        }
        throw new IncorrectInputException("track with this id not found");
    }
    public final void putTrain(Train train, Point pointFrom, Point direction) throws IncorrectInputException{
        trains = updTrainsState();
        direction = getPointFromDirection(pointFrom, direction);
        for(Track track : tracks) {
            if(track instanceof SwitchTrack) {
                if(!((SwitchTrack) track).isSwitchSetted()) {
                    throw new IncorrectInputException("Not all the switches are setted");
                }
            }
        }
        if(!pointFrom.equals(direction) && ((pointFrom.getX() == direction.getX()) || (pointFrom.getY() == direction.getY())))
        {
            if (train.isTrainValid()) {
                    Track track = findTrack(pointFrom, direction);
                    if(track == null) {
                        throw new IncorrectInputException("track not found");
                    }
                    TrainOnRoad trainOnRoad = new TrainOnRoad(train, track, isStartDirection(track, pointFrom, direction), pointFrom);
                    ArrayList<Track> temp = (ArrayList<Track>) tracks.clone();
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
                        if (track1.getId() == track.getId()) {
                            break;
                        }
                        i++;
                    }
                    temp.remove(i);
                    ArrayList<Track> endedStruct = getEndedStruct(temp, direction);
                    endedStruct = removeCopies(endedStruct);
                    for(Track track1 : endedStruct) {
                        availableTracksLength += track1.getLength();
                    }
                    if(availableTracksLength < train.getTrainLength()) {
                        throw new IncorrectInputException("there are not enough space on the track for this train");
                    }
                    for(TrainOnRoad tempTrainOnRoad : trains) {
                        if(haveCommonTracks(tempTrainOnRoad, trainOnRoad)) {
                            throw new IncorrectInputException("there is a train already on these tracks");
                        }
                    }
                    trains.add(trainOnRoad);
                    System.out.println("OK");
                } else {
                throw new IncorrectInputException("train isn't valid");
            }
        } else {
            throw new IncorrectInputException("wrong direction");
        }
    }
    public final ArrayList<Track> getTracksFromTrainOnRoad(TrainOnRoad train) {
        ArrayList<Track> field = (ArrayList<Track>) tracks.clone();
        Point pointStartSearch = null;
        int i = 0;
        for (Track track : field) {
            boolean notTrackHead = track.getId() != train.getTrackHead().getId();
            boolean areStartConnected = track.getStart().equals(train.getTrackHead().getStart())
                    || track.getEnd().equals(train.getTrackHead().getStart());
            boolean areEndConnected = track.getStart().equals(train.getTrackHead().getEnd())
                    || track.getEnd().equals(train.getTrackHead().getEnd());
            boolean isForwardTrack = train.isStartDirection() ? areStartConnected : areEndConnected;
            if(isForwardTrack && notTrackHead) {
                pointStartSearch = areStartConnected ? train.getTrackHead().getStart() : train.getTrackHead().getEnd();
                break;
            }
            i++;
        }
        ArrayList<Track> tracksOfTrain = new ArrayList<>();
        if(i!=field.size()) {
            field.remove(i);
            ArrayList<Track> forwardRoad;
            forwardRoad = getEndedStruct(field, pointStartSearch);
            Collections.reverse(forwardRoad);
            int lengthExtended = train.getLength() + Math.abs(pointStartSearch.getX() + pointStartSearch.getY()
                    - train.getPositionHead().getX() - train.getPositionHead().getY());
            for (Track track : forwardRoad) {
                if (lengthExtended > 0) {
                    tracksOfTrain.add(track);
                    lengthExtended -= track.getLength();
                }
            }
        } else
            tracksOfTrain.add(train.getTrackHead());
        return tracksOfTrain;
    }
    //for delete
    public final boolean haveCommonTrack(TrainOnRoad trainOnRoad1, Track track) {
        if(trainOnRoad1.getPositionHead().equals(track.getStart()) || trainOnRoad1.getPositionHead().equals(track.getEnd()))
            return true;
        ArrayList<Track> tracksOfFirst = getTracksFromTrainOnRoad(trainOnRoad1);
        for(Track track1 : tracksOfFirst) {
                if(track1.getId() == track.getId())
                    return true;
        }
        return false;
    }
    //for put
    public final boolean haveCommonTracks(TrainOnRoad trainOnRoad1, TrainOnRoad trainOnRoad2) {
        if(trainOnRoad1.getPositionHead().equals(trainOnRoad2.getPositionHead()))
            return true;
        ArrayList<Track> tracksOfFirst = getTracksFromTrainOnRoad(trainOnRoad1);
        ArrayList<Track> tracksOfSecond = getTracksFromTrainOnRoad(trainOnRoad2);
        for(Track track1 : tracksOfFirst) {
            for (Track track2 : tracksOfSecond) {
                if(track1.getId() == track2.getId())
                    return true;
            }
        }
        return false;
    }
    public final ArrayList<Track> getEndedStruct(ArrayList<Track> field, Point lastPoint) {
        if(field.size()==0)
            return new ArrayList<>();
        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
        for(Track tempTrack : field) {
            if (SwitchTrack.class.isInstance(tempTrack)) {
                if(((SwitchTrack) tempTrack).isSwitchSetted()) {
                    if (((SwitchTrack) tempTrack).getStart().equals(lastPoint)) {
                        int i = 0;
                        Point direction1 = tempTrack.getEnd();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
                        ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1);
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (((SwitchTrack) tempTrack).getEnd().equals(lastPoint)) {
                        int i = 0;
                        Point direction1 = tempTrack.getStart();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
                        ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1);
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    }
                } else {
                    if ((tempTrack.getStart().equals(lastPoint))) {
                        int i = 0;
                        Point direction1 = tempTrack.getEnd();
                        Point direction2 = ((SwitchTrack)tempTrack).getEnd2();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct(temp, direction1),
                                getEndedStruct((ArrayList<Track>) temp.clone(), direction2));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (tempTrack.getEnd().equals(lastPoint)) {
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
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct((ArrayList<Track>) temp.clone(), direction1),
                                getEndedStruct((ArrayList<Track>) temp.clone(), direction2));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (((SwitchTrack) tempTrack).getEnd2().equals(lastPoint)) {
                        int i = 0;
                        Point direction1 = tempTrack.getStart();
                        Point direction2 = tempTrack.getEnd();
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct((ArrayList<Track>) temp.clone(), direction1),
                                getEndedStruct(temp, direction2));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    }
                }
            } else {
                if (tempTrack.getStart().equals(lastPoint)) {
                    int i = 0;
                    Point direction1 = tempTrack.getEnd();
                    for(Track trackInTemp : temp) {
                        if(trackInTemp.getId() == tempTrack.getId()) {
                            break;
                        }
                        i++;
                    }
                    temp.remove(i);
                    ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1);
                    endedStruct.add(tempTrack);
                    return endedStruct;
                } else if (tempTrack.getEnd().equals(lastPoint)) {
                    int i = 0;
                    Point direction1 = tempTrack.getStart();
                    for(Track trackInTemp : temp) {
                        if(trackInTemp.getId() == tempTrack.getId()) {
                            break;
                        }
                        i++;
                    }
                    temp.remove(i);
                    ArrayList<Track> endedStruct = getEndedStruct((ArrayList<Track>) temp.clone(), direction1);
                    endedStruct.add(tempTrack);
                    return endedStruct;
                }
            }
        }
        //case when no connections where found
        return new ArrayList<>();
    }
    //works when switch are setted(убрать эту надпись)
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
        for(TrainOnRoad train : trains) {
            if(haveCommonTrack(train, track)) {
                 return false;
            }
        }
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
            ArrayList<Track> dir1struct = getEndedStruct(tempTracks, direction1);
            ArrayList<Track> dir2struct = getEndedStruct(tempTracks, direction2);
            ArrayList<Track> dir3struct = getEndedStruct(tempTracks, direction3);
            endedStruct = combineEndedStructs(dir3struct, combineEndedStructs(dir1struct, dir2struct));
        } else {
            Point direction1 = track.getStart();
            Point direction2 = track.getEnd();
            endedStruct = combineEndedStructs(getEndedStruct(tempTracks, direction1), getEndedStruct(tempTracks, direction2));
        }
        endedStruct.add(track);
        tempTracks = (ArrayList<Track>) tracks.clone();
        //remove track
        tempTracks.remove(i);
        ArrayList<Track> endedStructCheck = getEndedStruct(tempTracks, track.getStart());
        if (endedStructCheck.size() == 0)
            return true;
        else {
            endedStructCheck.add(track);
            return areSameRWStructs(endedStruct, endedStructCheck);
        }
    }
    private boolean areSameRWStructs(ArrayList<Track> struct1, ArrayList<Track> struct2) {
        if(struct1 == null || struct2 == null)
            return false;
        for(Track track1 : struct1) {
            boolean common = false;
            for(Track track2 : struct2) {
                if (track1.getId() == track2.getId()) {
                    common = true;
                    break;
                }
            }
            if(!common)
                return false;
        }
        for(Track track2 : struct2) {
            boolean common = false;
            for(Track track1 : struct1) {
                if (track1.getId() == track2.getId()) {
                    common = true;
                    break;
                }
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
            if(i != field.size()) {
                //removing track forward from train
                field.remove(i);
            }
            ArrayList<Track> forwardRoad;
            int stepLength = step;
            //counting road as ended struct, counting offset
            if (train.isStartDirection()) {
                forwardRoad = getEndedStruct(field, train.getTrackHead().getEnd());
                forwardRoad = removeCopies(forwardRoad);
                stepLength += Math.abs(train.getTrackHead().getEnd().getX() + train.getTrackHead().getEnd().getY()
                        - train.getPositionHead().getX() - train.getPositionHead().getY());
            } else {
                forwardRoad = getEndedStruct(field, train.getTrackHead().getStart());
                forwardRoad = removeCopies(forwardRoad);
                stepLength += Math.abs(train.getTrackHead().getStart().getX() + train.getTrackHead().getStart().getY()
                        - train.getPositionHead().getX() - train.getPositionHead().getY());
            }
            //check for crash with track
            if(stepLength > countLengthEndedStruct(forwardRoad)) {
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
            }
            boolean positivDirection = isDirectionPositiv(newHeadTrack, startDirection);
            newHeadPoint = countHeadPoint(startDirection, positivDirection, stepLength, newHeadTrack);
            if(train.isMovingBack()) {
                train.setMovingBack(false);
                TrainOnRoad temp = new TrainOnRoad(train.getTrain(), newHeadTrack, !startDirection, newHeadPoint);
                if (crashesWithTrack(temp)) {
                    temp.setCrashed(true);
                }
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
    private Track findTrack(Point pointFrom, Point direction) {
        for(Track track : tracks) {
            //flat situation
            if(pointBelongsTrack(pointFrom, track)) {
                boolean dirTrack = track.getEnd().getY() == track.getStart().getY();
                boolean dirSetted = pointFrom.getY() == direction.getY();
                if(dirTrack == dirSetted) {
                    if(pointFrom.equals(track.getEnd())) {
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
            }
            //corner situation
            for(Track tempTrack : tracks) {
                if(tempTrack.getStart().equals(pointFrom) || tempTrack.getEnd().equals(pointFrom)) {
                    boolean dirVertTemp = (tempTrack.getEnd().getX() == tempTrack.getStart().getX());
                    boolean dirVertTrack = (pointFrom.getX() == direction.getX());
                    if(dirVertTemp == dirVertTrack)
                        return tempTrack;
                }
            }
        }
        return null;
    }
    //vorausgesetzt dass der Punkt auf der Strecke vom Track liegt
    private boolean isStartDirection(Track track, Point pointfrom, Point direction) {
        boolean dirTrack = (track.getStart().getY() + track.getStart().getX()) - (track.getEnd().getY() + track.getEnd().getX()) > 0;
        boolean dirPoint =  (direction.getX() + direction.getY()) - (pointfrom.getX() + pointfrom.getY()) > 0;
        return dirTrack == dirPoint;
    }
    private ArrayList<Track> removeCopies(ArrayList<Track> overextendedField) {
        Set<Track> set = new LinkedHashSet<>(overextendedField);
        return new ArrayList<>(set);
    }
    private Point getPointFromDirection(Point pointFrom, Point direction) {

        return new Point(pointFrom.getX() + direction.getX(), pointFrom.getY() + direction.getY());
    }
    private boolean crashesTrainOnSwitch(Track switchTrack) {
        for(TrainOnRoad train : trains) {
            if(haveCommonTrack(train, switchTrack)) {
                train.setCrashed(true);
                return true;
            }
        }
        return false;
    }
    private ArrayList<TrainOnRoad> removeCrashedTrains(ArrayList<TrainOnRoad> movingTrains) {
        ArrayList<TrainOnRoad> temp = new ArrayList<>();
        for(TrainOnRoad train : movingTrains) {
            if(!train.isCrashed())
                temp.add(train);
        }
        return temp;
    }
    private String createStepString(ArrayList<TrainOnRoad> movingTrains, ArrayList<ArrayList<Integer>> idsCrashes) {
        String str = "";
        if(idsCrashes.size() == 0) {
            for (TrainOnRoad train : movingTrains) {
                str += train + "\n";
            }
            return str.trim();
        }
        movingTrains.sort(Comparator.comparingInt(t -> t.getTrain().getTrainId()));
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
        return str.trim();
    }
    private ArrayList<ArrayList<Integer>> findCrashes(ArrayList<TrainOnRoad> movingTrains) {
        ArrayList<ArrayList<Integer>> crashesId = new ArrayList<>();
        for(TrainOnRoad train1 : movingTrains) {
            ArrayList<Integer> crashIds = new ArrayList<Integer>();
            for (TrainOnRoad train2 : movingTrains) {
                if ((train2.getTrain().getTrainId() != train1.getTrain().getTrainId()) && haveCommonTracks(train1, train2)) {
                    train1.setCrashed(true);
                    train2.setCrashed(true);
                    crashIds.add(train1.getTrain().getTrainId());
                    crashIds.add(train2.getTrain().getTrainId());
                }
            }
            if (train1.isCrashed()) {
                crashIds.add(train1.getTrain().getTrainId());
            }

            if(crashIds.size() != 0) {
                Collections.sort(crashIds);
                HashSet<Integer> temp = new HashSet<>(crashIds);
                crashIds.clear();
                crashIds.addAll(temp);
                crashesId.add(crashIds);
            }
        }
        if(crashesId.size()!=0) {
            HashSet<ArrayList<Integer>> temp = new HashSet<>(crashesId);
            crashesId.clear();
            crashesId.addAll(temp);
            crashesId.sort(Comparator.comparingInt(l -> l.get(0)));
            return crashesId;
        } else
            return new ArrayList<ArrayList<Integer>>();
    }
    private boolean crashesWithTrack(TrainOnRoad train) {
        ArrayList<Track> field = (ArrayList<Track>) tracks.clone();
        int i = 0;
        for (Track track : field) {
            if(track.getId() != train.getTrackHead().getId())
                if(train.isStartDirection()) {
                    boolean trackConnectedWithHeadTrackStart = track.getStart().equals(train.getTrackHead().getStart())
                            || track.getEnd().equals(train.getTrackHead().getStart());
                    if (trackConnectedWithHeadTrackStart) {
                        break;
                    }
                } else {
                    boolean trackConnectedWithHeadTrackEnd = track.getStart().equals(train.getTrackHead().getEnd())
                            || track.getEnd().equals(train.getTrackHead().getEnd());
                    if (trackConnectedWithHeadTrackEnd) {
                        break;
                    }
                }
            i++;
        }
        if(i != field.size())
            field.remove(i);
        ArrayList<Track> endedStruct = new ArrayList<>();
        int trainLength = train.getLength();
        if(train.isStartDirection()) {
            endedStruct = getEndedStruct(field, train.getTrackHead().getStart());
            trainLength += Math.abs(train.getPositionHead().getX() + train.getPositionHead().getY()
                    - train.getTrackHead().getStart().getX() - train.getTrackHead().getStart().getY());
        } else {
            endedStruct = getEndedStruct(field, train.getTrackHead().getEnd());
            trainLength += Math.abs(train.getPositionHead().getX() + train.getPositionHead().getY()
                    - train.getTrackHead().getEnd().getX() - train.getTrackHead().getEnd().getY());
        }
        Collections.reverse(endedStruct);
        for (Track track : endedStruct) {
            trainLength -= track.getLength();
        }
        if(trainLength>0)
            return true;
        else
            return false;
    }
    private Point countHeadPoint(boolean startDirection, boolean positivDirection, int stepLength, Track newHeadTrack) {
        Point newHeadPoint;
        if (stepLength == 0) {
            if (startDirection)
                return newHeadTrack.getStart();
            else
                return newHeadTrack.getEnd();
        }
        int absLength = Math.abs(stepLength);
        if (newHeadTrack.getStart().getX() == newHeadTrack.getEnd().getX()) {
            if (startDirection) {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX(),
                        newHeadTrack.getStart().getY()  - absLength) : new Point(newHeadTrack.getStart().getX(),
                        newHeadTrack.getStart().getY() + absLength);
            } else {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX(),
                        newHeadTrack.getEnd().getY() - absLength) :
                        new Point(newHeadTrack.getStart().getX(), newHeadTrack.getEnd().getY() + absLength);
            }

        } else {
            if (startDirection) {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX() - absLength, newHeadTrack.getStart().getY()) :
                        new Point(newHeadTrack.getStart().getX() + absLength , newHeadTrack.getStart().getY());
            } else {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getEnd().getX() - absLength, newHeadTrack.getStart().getY()) :
                        new Point(newHeadTrack.getEnd().getX() + absLength, newHeadTrack.getStart().getY());
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
}
