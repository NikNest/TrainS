package RailWay;

import RailWay.utils.*;

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
                    if (isRWValidIfSwitchSetted(id, end)) {
                        ((SwitchTrack) track).setDirection(end);
                        System.out.println("OK");
                        return;
                    } else  {
                        System.out.println("not valid switch");
                    }
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
            if(!(track instanceof SwitchTrack)) str += track;
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
        Iterator<Track> iter = tracks.iterator();
        for(int i = 0; i < tracks.size(); i++) {
            Track track = iter.next();
            if(trackId == track.getId()) {
                if(track.isStartConnected() && track.isEndConnected()) {
                    System.out.println("track with this id is found but is two side connected");
                    return;
                }
                removeConnections(track.getStart(), track.getEnd());
                tracks.remove(i);
                System.out.println("OK");
                return;
            }
        }
        System.out.println("track with this id not found");
    }
    //TODO
    public void putTrain(Train train, Point pointFrom, Point direction) {
        for(Track track : tracks) {
            if(track instanceof SwitchTrack) {
                if(!((SwitchTrack) track).isSwitchSetted()) {
                    System.out.println("Not all the switches are setted!");
                    return;
                }
            }
        }
        if(!pointFrom.equals(direction) && (pointFrom.getX() == direction.getX()) || (pointFrom.getY() == direction.getY()))
        {
            if (train.isTrainValid()) {
                    Track track = findTrack(pointFrom, direction);
                    if(track == null) {
                        System.out.println("no track found");
                        return;
                    }
                    TrainOnRoad trainOnRoad = new TrainOnRoad(train, track, isStartDirection(track, direction), pointFrom);
                    int totalRWLength = 0;
                    for(Track tempTrack : tracks) {
                        totalRWLength += tempTrack.getLength();
                    }
                    if(totalRWLength<train.getTrainLength()) {
                        System.out.println("Train is larger then the hall RW");
                        return;
                    }
                    //check that the space is free
                    for(TrainOnRoad tempTrainOnRoad : trains) {
                        if(crossHappens(tempTrainOnRoad, trainOnRoad)) {
                            System.out.println("there is a train already on these points");
                            return;
                        }
                    }
                    trains.add(trainOnRoad);

                }
        } else {
            System.out.println("wrong direction");
        }
    }
    //vorausgesetzt dass der Punkt auf der Strecke vom Track liegt
    private boolean isStartDirection(Track track, Point direction) {
        return Math.abs(direction.getX() - track.getEnd().getX() + direction.getY() - track.getEnd().getY()) >
                Math.abs(direction.getX() - track.getStart().getX() + direction.getY() - track.getStart().getY());
    }
    //
    private Track findTrack(Point pointFrom, Point direction) {
        for(Track track : tracks) {
            if(pointBelongsTrack(pointFrom, track)) {
                float dirTrack =  (track.getEnd().getX() - track.getStart().getX() + track.getEnd().getY() - track.getStart().getY()) > 0 ? 1 : -1;
                float dirSetted = (direction.getX() - pointFrom.getX() + direction.getY() - pointFrom.getY()) > 0 ? 1 : -1;
                if(dirTrack == dirSetted)
                    return track;
            }
        }
        return null;
    }
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
        if(track.getSameCoord() == point.getX() || track.getSameCoord() == point.getY()) {
            if(track.getFromCoord() < point.getY() && point.getY() < track.getToCoord()) {
                return true;
            } else return track.getFromCoord() < point.getX() && point.getX() < track.getToCoord();
        }
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
    private boolean isRWValidIfSwitchSetted(int switchTrackid, Point end) {
        ArrayList<Track> tempTracks = (ArrayList<Track>) tracks.clone();
        //seting up switch in clone array list
        for(Track track : tempTracks) {
            if(track.getId() == switchTrackid) {
                ((SwitchTrack)track).setDirection(end);
            }
        }
        ArrayList<Track> searchStartDirection;
        ArrayList<Track> searchEndDirection;

        Track track = tempTracks.get(0);
        tempTracks.remove(track);
        Point lastTrackPoint = track.getStart();
        searchStartDirection = getEndedStruct(tempTracks, lastTrackPoint, 0);
        lastTrackPoint = track.getEnd();
        searchEndDirection = getEndedStruct(tempTracks, lastTrackPoint, 0);
        ArrayList<Track> endedStruct = combineEndedStructs(searchEndDirection, searchStartDirection);
        endedStruct.add(track);
        System.out.println(":");
        for (Track track1 : searchEndDirection) {
            System.out.print(track1.getId() + " ");
        }
        for (Track track1 : searchStartDirection) {
            System.out.print(track1.getId() + " ");
        }
        System.out.println();
        for (Track track1 : tracks) {
            System.out.print(track1.getId() + " ");
        }
        System.out.println();
        return areSameRWStructs(tracks, endedStruct);
    }
    private boolean areSameRWStructs(ArrayList<Track> struct1, ArrayList<Track> struct2) {
        if((struct1 == null && struct2 != null) || (struct1 != null && struct2 == null))
            return false;
        if(struct1 == null && struct2 == null)
            return true;
        return struct1.containsAll(struct2) && struct2.containsAll(struct1);
    }
    private ArrayList<Track> combineEndedStructs(ArrayList<Track> struct1, ArrayList<Track> struct2) {
        Set<Track> set = new LinkedHashSet<>(struct1);
        set.addAll(struct2);
        return new ArrayList<>(set);
    }
    //there are no track from one side of the point
    //=> we dont need track as an argument
    private ArrayList<Track> getEndedStruct(ArrayList<Track> field, Point direction, int depth) {
        System.out.println("g" + depth);
        for(Track track : field) {
            System.out.print(track.getId() + " ");
        }
        System.out.println();
        if(field.size()==0)
            return new ArrayList<>();
        for(Track tempTrack : field) {
            if (SwitchTrack.class.isInstance(tempTrack)) {
                if(((SwitchTrack) tempTrack).isSwitchSetted()) {
                    if (((SwitchTrack) tempTrack).getStart().equals(direction)) {
                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = null;
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                direction1 = trackInTemp.getEnd();
                                break;
                            }
                            i++;
                        }
//                        System.out.println("a");
//                        for(Track track : temp) {
//                            System.out.print(track.getId() + " ");
//                        }
//                        System.out.println();
//                        System.out.println(temp.size() + " remove\n");
                        temp.remove(i);
//                        System.out.println(temp.size());
                        return getEndedStruct(temp, direction1, ++depth);
                    } else if (((SwitchTrack) tempTrack).getEnd().equals(direction)) {
                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = null;
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                direction1 = trackInTemp.getStart();
                                break;
                            }
                            i++;
                        }
//                        System.out.println("b");
//                        for(Track track : temp) {
//                            System.out.print(track.getId() + " ");
//                        }
//                        System.out.println();
                        temp.remove(i);
                        return getEndedStruct(temp, direction1, ++depth);
                    } else if(((SwitchTrack) tempTrack).getEnd2().equals(direction))
                        return new ArrayList<>();
                } else {
                    if (((SwitchTrack) tempTrack).getStart().equals(direction)) {
                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = null;
                        Point direction2 = null;
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                direction1 = trackInTemp.getEnd();
                                direction2 = ((SwitchTrack)trackInTemp).getEnd2();
                                break;
                            }
                            i++;
                        }
//                        System.out.println("c");
//                        for(Track track : temp) {
//                            System.out.print(track.getId() + " ");
//                        }
//                        System.out.println();
                        temp.remove(i);
                        return combineEndedStructs(getEndedStruct(temp, direction1, ++depth),
                                getEndedStruct(temp, direction2, ++depth));
                    } else if (((SwitchTrack) tempTrack).getEnd().equals(direction)) {
                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = null;
                        Point direction2 = null;

                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                direction1 = trackInTemp.getStart();
                                direction2 = ((SwitchTrack)trackInTemp).getEnd2();
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
//                        System.out.println("d");
//                        for(Track track : temp) {
//                            System.out.print(track.getId() + " ");
//                        }
//                        System.out.println();
                        return combineEndedStructs(getEndedStruct(temp, direction1, ++depth),
                                getEndedStruct(temp, direction2, ++depth));
                    } else if (((SwitchTrack) tempTrack).getEnd2().equals(direction)) {
                        ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                        int i = 0;
                        Point direction1 = null;
                        Point direction2 = null;
                        for(Track trackInTemp : temp) {
                            if(trackInTemp.getId() == tempTrack.getId()) {
                                direction1 = trackInTemp.getEnd();
                                direction2 = trackInTemp.getStart();
                                break;
                            }
                            i++;
                        }
//                        System.out.println(temp.indexOf(entityOfTempTrack) + " " + entityOfTempTrack);
//                        System.out.println("e");
//                        for(Track track : temp) {
//                            System.out.print(track.getId() + " ");
//                        }
//                        System.out.println();
                        temp.remove(i);
                        return combineEndedStructs(getEndedStruct(temp, direction1, ++depth),
                                getEndedStruct(temp, direction2, ++depth));
                    }
                }
            } else {
                if (tempTrack.getStart().equals(direction)) {
                    ArrayList<Track> temp = (ArrayList<Track>) field.clone();
//                    Track entityOfTempTrack = null;
                    int i = 0;
                    Point direction1 = null;
                    for(Track trackInTemp : temp) {
                        if(trackInTemp.getId() == tempTrack.getId()) {
                            direction1 = trackInTemp.getEnd();
                            break;
                        }
                        i++;
                    }
//                    System.out.println(temp.indexOf(temp.get(i)) + " " + temp.get(i));
//                    System.out.println("f");
//                    for(Track track : temp) {
//                        System.out.print(track.getId() + " ");
//                    }
//                    System.out.println();
                    temp.remove(i);
                    return getEndedStruct(temp, direction1, ++depth);
                } else if (tempTrack.getEnd().equals(direction)) {
                    ArrayList<Track> temp = (ArrayList<Track>) field.clone();
                    int i = 0;
                    Point direction1 = null;
                    for(Track trackInTemp : temp) {
                        if(trackInTemp.getId() == tempTrack.getId()) {
                            direction1 = trackInTemp.getStart();
                            break;
                        }
                        i++;
                    }
//                    System.out.println("b");
//                    for(Track track : temp) {
//                        System.out.print(track.getId() + " ");
//                    }
//                    System.out.println();
                    temp.remove(i);
                    return getEndedStruct(temp, direction1, ++depth);
                }
            }
        }
        //case when no connections where found
        return new ArrayList<>();

    }

    private void removeConnections(Point trackStart, Point trackEnd) {
        Iterator<Track> iter = tracks.iterator();
        for(int i = 0; i < tracks.size(); i++) {
            Track track = iter.next();
            if(track.getEnd().equals(trackStart))
                tracks.get(i).setEndConnected(false);
            if(track.getStart().equals(trackEnd))
                tracks.get(i).setStartConnected(false);
        }
    }
}
