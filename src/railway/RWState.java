package railway;

import edu.kit.informatik.Terminal;
import railway.utils.*;

import java.util.*;

public final class RWState {
    private ArrayList<Track> tracks;
    private ArrayList<TrainOnRoad> trains;

    public RWState() {
        tracks = new ArrayList<Track>();
        trains = new ArrayList<TrainOnRoad>();
    }

    public ArrayList<TrainOnRoad> updTrainsState() {
        ArrayList<TrainOnRoad> temp = new ArrayList<TrainOnRoad>();
        for (TrainOnRoad train : trains) {
            if (train.getTrain() != null)
                temp.add(train);
        }
        return temp;
    }

    public void step(short speed) throws IncorrectInputException {
        for (Track track : tracks) {
            if (SwitchTrack.class.isInstance(track))
                if (!((SwitchTrack) track).isSwitchSetted())
                    throw new IncorrectInputException("not all the switches are setted");
        }
        trains = updTrainsState();
        ArrayList<TrainOnRoad> movingTrains = new ArrayList<TrainOnRoad>();
        if (trains.size() == 0) {
            Terminal.printLine("OK");
            return;
        }
        for (int i = 0; i < trains.size(); i++) {
            TrainOnRoad train = trains.get(i);
            movingTrains.add(move(train, speed));
        }
        ArrayList<ArrayList<Integer>> crashes = RWstateUtils.findCrashes(movingTrains, tracks);
        String str = RWstateUtils.createStepString(movingTrains, crashes);
        Terminal.printLine(str);
        trains = RWstateUtils.removeCrashedTrains(movingTrains);
    }

    public void addTrack(Point start, Point end) throws IncorrectInputException {
        if (!start.equals(end) && ((start.getY() == end.getY()) || (start.getX() == end.getX()))) {
            Track temp = new Track(start, end);
            if (tracks.size() == 0) {
                initTrackId(temp);
                tracks.add(temp);
                Terminal.printLine(temp.getId());
            } else {
                for (Track track : tracks) {
                    if (temp.areIlligalConnected(track))
                        throw new IncorrectInputException("There is a track already on these points");
                    if (temp.getCommonPoint(track) != null) {
                        if (SwitchTrack.class.isInstance(track)) {
                            ((SwitchTrack) track).connect(temp.getCommonPoint(track));
                        } else
                        track.connect(temp.getCommonPoint(track));
                        temp.connect(temp.getCommonPoint(track));
                        initTrackId(temp);
                        tracks.add(temp);
                        Terminal.printLine(temp.getId());
                        return;
                    }
                }
                throw new IncorrectInputException("wrong connection");
            }
        } else
            throw new IncorrectInputException("invalid track: not straight or 0 length");
    }

    public void addSwitch(Point start, Point end, Point end2) throws IncorrectInputException {
        if ((!start.equals(end) && (start.getY() == end.getY() || (start.getX() == end.getX()))) && (!start.equals(end2)
              && ((start.getY() == end2.getY()) || (start.getX() == end2.getX()))) && !end.equals(end2)) {
            SwitchTrack temp = new SwitchTrack(start, end, end2);
            if (tracks.size() == 0) {
                initTrackId(temp);
                tracks.add(temp);
                Terminal.printLine(temp.getId());
            } else {
                for (Track track : tracks) {
                    if (temp.areIlligalConnected(track))
                         throw new IncorrectInputException("There is a track already on these points");
                    if (temp.getCommonPoint(track) != null) {
                        if (SwitchTrack.class.isInstance(track)) {
                            ((SwitchTrack) track).connect(temp.getCommonPoint(track));
                        } else
                            track.connect(temp.getCommonPoint(track));
                        temp.connect(temp.getCommonPoint(track));
                        initTrackId(temp);
                        tracks.add(temp);
                        Terminal.printLine(temp.getId());
                        return;
                    }
                }
                throw new IncorrectInputException("wrong connection");
            }
        } else
                throw new IncorrectInputException("invalid switch-track: not straight");
    }

    public void setSwitch(int id, Point end) throws IncorrectInputException {
        trains = updTrainsState();
        for (Track track : tracks) {
            if (track.getId() == id) {
                if (!SwitchTrack.class.isInstance(track)) {
                    throw new IncorrectInputException("Wrong switch id");
                }
                if (((SwitchTrack) track).getEnd2().equals(end) || track.getEnd().equals(end)
                        || track.getStart().equals(end)) {
                    ((SwitchTrack) track).setDirection(end);
                    if (!crashesTrainOnSwitch(track))
                          Terminal.printLine("OK");
                    else {
                        int i = 0;
                        for (TrainOnRoad train : trains) {
                            if (train.isCrashed())
                                break;
                            i++;
                        }
                        Terminal.printLine("Crash of train " + trains.get(i).getTrain().getTrainId());
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

    public String listTracks() {
        String str = "";
        Iterator<Track> iter = tracks.iterator();
        while (iter.hasNext()) {
            Track track = iter.next();
            if (!(SwitchTrack.class.isInstance(track)))
                str += track;
            else str += (SwitchTrack) track;
            if (iter.hasNext())
                str += "\n";
        }
        if (str.equals(""))
            str = "No track exists";
        return Sorter.sortList(str, new SortNumTrack());
    }

    public void deleteTrack(int trackId) throws IncorrectInputException {
        trains = updTrainsState();
        for (Track track : tracks) {
            if (trackId == track.getId()) {
                if (isRWValidIfTrackDeleted(track)) {
                    removeConnections(track);
                    int i = 0;
                    for (Track track1 : tracks) {
                        if (track1.getId() == track.getId())
                            break;
                        i++;
                    }
                    tracks.remove(i);
                    Terminal.printLine("OK");
                    return;
                }
                throw new IncorrectInputException("track with this id couldn't be deleted");
            }
        }
        throw new IncorrectInputException("track with this id not found");
    }

    public void putTrain(Train train, Point pointFrom, Point dir) throws IncorrectInputException {
        trains = updTrainsState();
        Point direction =  RWstateUtils.getPointFromDirection(pointFrom, dir);
        for (Track track : tracks) {
            if (track instanceof SwitchTrack) {
                if (!((SwitchTrack) track).isSwitchSetted()) {
                    throw new IncorrectInputException("Not all the switches are setted");
                }
            }
        }
        if (!pointFrom.equals(direction) && ((pointFrom.getX() == direction.getX())
                || (pointFrom.getY() == direction.getY())))
        {
            if (train.isTrainValid()) {
                Track track = RWstateUtils.findTrack(pointFrom, direction, tracks);
                if (track == null) {
                    throw new IncorrectInputException("track not found");
                }
                TrainOnRoad trainOnRoad = new TrainOnRoad(
                        train, track, isStartDirection(track, pointFrom, direction), pointFrom);
                ArrayList<Track> temp = new ArrayList<Track>(tracks);
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
                for (Track track1 : tracks) {
                    if (track1.getId() == track.getId()) {
                        break;
                    }
                    i++;
                }
                temp.remove(i);
                ArrayList<Track> endedStruct = RWstateUtils.getEndedStruct(temp, direction);
                endedStruct = removeCopies(endedStruct);
                for (Track track1 : endedStruct) {
                    availableTracksLength += track1.getLength();
                }
                if (availableTracksLength < train.getTrainLength()) {
                    throw new IncorrectInputException("there are not enough space on the track for this train");
                }
                for (TrainOnRoad tempTrainOnRoad : trains) {
                    if (RWstateUtils.haveCommonTracks(tempTrainOnRoad, trainOnRoad, tracks)) {
                        throw new IncorrectInputException("there is a train already on these tracks");
                    }
                }
                trains.add(trainOnRoad);
                Terminal.printLine("OK");
            } else {
                throw new IncorrectInputException("train isn't valid");
            }
        } else {
            throw new IncorrectInputException("wrong direction");
        }
    }

    private void initTrackId(Track track) {
        if (track.getId() == 0) {
            int id = 0;
            for (Track temp : tracks) {
                if (temp.getId() == id + 1)
                    id = temp.getId();
            }
            track.setId(++id);
        }
    }

    private boolean isRWValidIfTrackDeleted(Track track) {
        for (TrainOnRoad train : trains) {
            if (RWstateUtils.haveCommonTrack(train, track, tracks)) {
                return false;
            }
        }
        ArrayList<Track> tempTracks = new ArrayList<Track>(tracks);
        int i = 0;
        for (Track track1 : tempTracks) {
            if (track1.getId() == track.getId())
                break;
            i++;
        }
        tempTracks.remove(i);
        ArrayList<Track> endedStruct;
        if (SwitchTrack.class.isInstance(track)) {
            Point direction1 = track.getEnd();
            Point direction2 = track.getStart();
            Point direction3 = null;
            if (!((SwitchTrack) track).isSwitchSetted())
                direction3 = ((SwitchTrack) track).getEnd2();
            ArrayList<Track> dir1struct = RWstateUtils.getEndedStruct(tempTracks, direction1);
            ArrayList<Track> dir2struct = RWstateUtils.getEndedStruct(tempTracks, direction2);
            ArrayList<Track> dir3struct = RWstateUtils.getEndedStruct(tempTracks, direction3);
            endedStruct = RWstateUtils.combineEndedStructs(dir3struct,
                    RWstateUtils.combineEndedStructs(dir1struct, dir2struct));
        } else {
            Point direction1 = track.getStart();
            Point direction2 = track.getEnd();
            endedStruct = RWstateUtils.combineEndedStructs(
                    RWstateUtils.getEndedStruct(tempTracks, direction1), RWstateUtils.getEndedStruct(
                            tempTracks, direction2));
        }
        endedStruct.add(track);
        tempTracks = new ArrayList<Track>(tracks);
        //remove track
        tempTracks.remove(i);
        ArrayList<Track> endedStructCheck = RWstateUtils.getEndedStruct(tempTracks, track.getStart());
        if (endedStructCheck.size() == 0)
            return true;
        else {
            endedStructCheck.add(track);
            return areSameRWStructs(endedStruct, endedStructCheck);
        }
    }

    private boolean areSameRWStructs(ArrayList<Track> struct1, ArrayList<Track> struct2) {
        if (struct1 == null || struct2 == null)
            return false;
        for (Track track1 : struct1) {
            boolean common = false;
            for (Track track2 : struct2) {
                if (track1.getId() == track2.getId()) {
                    common = true;
                    break;
                }
            }
            if (!common)
                return false;
        }
        for (Track track2 : struct2) {
            boolean common = false;
            for (Track track1 : struct1) {
                if (track1.getId() == track2.getId()) {
                    common = true;
                    break;
                }
            }
            if (!common)
                return false;
        }
        return true;
    }

    private void removeConnections(Track track) {
        if (SwitchTrack.class.isInstance(track)) {
            Point point1 = track.getStart();
            Point point2 = track.getEnd();
            Point point3 = ((SwitchTrack) track).getEnd2();
            for (Track track1 : tracks) {
                if ((SwitchTrack.class.isInstance(track1)) && (((SwitchTrack) track1).getEnd2().equals(point1)
                        || ((SwitchTrack) track1).getEnd2().equals(point2)
                        || ((SwitchTrack) track1).getEnd2().equals(point3))) {
                    ((SwitchTrack) track1).setSecondendConnected(false);
                }
                if (track1.getEnd().equals(point1) || track1.getEnd().equals(point2)
                        || track1.getEnd().equals(point3)) {
                    track1.setEndConnected(false);
                }
                if (track1.getStart().equals(point1) || track1.getStart().equals(point2)
                        || track1.getStart().equals(point3)) {
                    track1.setStartConnected(false);
                }
            }
        } else {
            Point point1 = track.getStart();
            Point point2 = track.getEnd();
            for (Track track1 : tracks) {
                if ((SwitchTrack.class.isInstance(track1)) && (((SwitchTrack) track1).getEnd2().equals(point1)
                        || ((SwitchTrack) track1).getEnd2().equals(point2))) {
                    ((SwitchTrack) track1).setSecondendConnected(false);
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

    private TrainOnRoad move(TrainOnRoad train, int speed) {
        int step = speed;
        if (step != 0) {
            //in case of negative step turning around
            if (step < 0) {
                train.setStartDirection(!train.isStartDirection());
                train.setMovingBack(true);
                step = -step;
            }
            ArrayList<Track> field = new ArrayList<Track>(tracks);
            int i = 0;
            for (Track track : field) {
                boolean notTrackHead = track.getId() != train.getTrackHead().getId();
                boolean areStartConnected = track.getStart().equals(train.getTrackHead().getStart())
                        || track.getEnd().equals(train.getTrackHead().getStart());
                boolean areEndConnected = track.getStart().equals(train.getTrackHead().getEnd())
                        || track.getEnd().equals(train.getTrackHead().getEnd());
                boolean areConnectedEndDirection = train.isStartDirection() ? areEndConnected : areStartConnected;
                if (areConnectedEndDirection && notTrackHead)
                    break;
                i++;
            }
            if (i != field.size()) {
                //removing track forward from train NOT BACK?
                field.remove(i);
            }
            ArrayList<Track> forwardRoad;
            int stepLength = step;
            //counting road as ended struct, counting offset
            if (train.isStartDirection()) {
                forwardRoad = RWstateUtils.getEndedStruct(field, train.getTrackHead().getEnd());
                forwardRoad = removeCopies(forwardRoad);
                stepLength += Math.abs(train.getTrackHead().getEnd().getX() + train.getTrackHead().getEnd().getY()
                        - train.getPositionHead().getX() - train.getPositionHead().getY());
            } else {
                forwardRoad = RWstateUtils.getEndedStruct(field, train.getTrackHead().getStart());
                forwardRoad = removeCopies(forwardRoad);
                stepLength += Math.abs(train.getTrackHead().getStart().getX() + train.getTrackHead().getStart().getY()
                        - train.getPositionHead().getX() - train.getPositionHead().getY());
            }
            //check for crash with track
            if (stepLength > RWstateUtils.countLengthEndedStruct(forwardRoad)) {
                train.setCrashed(true);
                return train;
            }
            Collections.reverse(forwardRoad);
            //moving
            ArrayList<Track> tracksOfTrain = new ArrayList<Track>();
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
            if (tracksOfTrain.size() == 1) {
                newHeadTrack = train.getTrackHead();
                startDirection = train.isStartDirection();
            } else {
                newHeadTrack = tracksOfTrain.get(tracksOfTrain.size() - 1);
                Track beforeHeadTrack = tracksOfTrain.get(tracksOfTrain.size() - 2);
                startDirection = newHeadTrack.getCommonPoint(beforeHeadTrack).equals(newHeadTrack.getEnd());
            }
            boolean positivDirection = RWstateUtils.isDirectionPositiv(newHeadTrack, startDirection);
            newHeadPoint = TrainOnRoad.countHeadPoint(startDirection, positivDirection, stepLength, newHeadTrack);
            if (train.isMovingBack()) {
                train.setMovingBack(false);
                TrainOnRoad temp = new TrainOnRoad(train.getTrain(), newHeadTrack, !startDirection, newHeadPoint);
                if (RWstateUtils.crashesWithTrack(temp, tracks)) {
                    temp.setCrashed(true);
                }
                return temp;
            } else
                return new TrainOnRoad(train.getTrain(), newHeadTrack, startDirection, newHeadPoint);
        } else
            return train;
    }

    private boolean isStartDirection(Track track, Point pointfrom, Point direction) {
        boolean dirTrack = (track.getStart().getY() + track.getStart().getX()) - (track.getEnd().getY()
                + track.getEnd().getX()) > 0;
        boolean dirPoint =  (direction.getX() + direction.getY()) - (pointfrom.getX() + pointfrom.getY()) > 0;
        return dirTrack == dirPoint;
    }

    private ArrayList<Track> removeCopies(ArrayList<Track> overextendedField) {
        Set<Track> set = new LinkedHashSet<Track>(overextendedField);
        return new ArrayList<Track>(set);
    }

    private boolean crashesTrainOnSwitch(Track switchTrack) {
        for (TrainOnRoad train : trains) {
            if (RWstateUtils.haveCommonTrack(train, switchTrack, tracks)) {
                train.setCrashed(true);
                return true;
            }
        }
        return false;
    }

}
