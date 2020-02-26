package railway.utils;

import java.util.*;

/**
 * class with static methods for tracks and moving on the tracks logic
 * @author Nikita
 * @version 228
 */
public class RWstateUtils {
    /**
     * positiv direction if the forward point of the track is higher or to the right
     * @param track the track
     * @param startDirection track status
     * @return important info for position counting
     */
    public static boolean isDirectionPositiv(Track track, boolean startDirection) {
        if (startDirection) {
            return (track.getStart().getX() + track.getStart().getY()
                    - track.getEnd().getX() - track.getEnd().getY()) > 0;
        } else {
            return (track.getStart().getX() + track.getStart().getY()
                    - track.getEnd().getX() - track.getEnd().getY()) < 0;
        }
    }

    /**
     * for step and put commands
     * @param point point to check
     * @param track track to check
     * @return true if the point belongs to the track
     */
    public static boolean pointBelongsTrack(Point point, Track track) {
        if (track.getSameCoord() == point.getX()) {
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

    /**
     * works as a part of the getEndedStruct(...)
     * @param field field of search
     * @param tempTrack the last track of the actual ended struct
     * @return ended struct
     */
    private static ArrayList<Track> startEndStructExtract(ArrayList<Track> field, Track tempTrack) {
        ArrayList<Track> temp = new ArrayList<Track>(field);
        int i = 0;
        Point direction1 = tempTrack.getEnd();
        for (Track trackInTemp : temp) {
            if (trackInTemp.getId() == tempTrack.getId()) {
                break;
            }
            i++;
        }
        temp.remove(i);
        ArrayList<Track> endedStruct = getEndedStruct(new ArrayList<Track>(temp), direction1);
        endedStruct.add(tempTrack);
        return endedStruct;
    }

    /**
     * works as a part of the getEndedStruct(...)
     * @param field field of search
     * @param tempTrack the last track of the actual ended struct
     * @return ended struct
     */
    private static ArrayList<Track> endEndStructExtract(ArrayList<Track> field, Track tempTrack) {
        ArrayList<Track> temp = new ArrayList<Track>(field);
        int i = 0;
        Point direction1 = tempTrack.getStart();
        for (Track trackInTemp : temp) {
            if (trackInTemp.getId() == tempTrack.getId()) {
                break;
            }
            i++;
        }
        temp.remove(i);
        ArrayList<Track> endedStruct = getEndedStruct(new ArrayList<Track>(temp), direction1);
        endedStruct.add(tempTrack);
        return endedStruct;
    }

    /**
     * ended struct = ArrayList of Tracks
     * the tracks that are connected
     * works recursive
     * @param field field of search for the ended struct
     * @param lastPoint point of starting the search(is
     *                  the last point of ended struct from one side)
     * @return ended struct
     */
    public static ArrayList<Track> getEndedStruct(ArrayList<Track> field, Point lastPoint) {
        if (field.size() == 0)
            return new ArrayList<Track>();
        ArrayList<Track> temp = new ArrayList<Track>(field);
        for (Track tempTrack : field) {
            if (SwitchTrack.class.isInstance(tempTrack)) {
                if (!((SwitchTrack) tempTrack).isSwitchSetted()) {
                    if ((tempTrack.getStart().equals(lastPoint))) {
                        int i = 0;
                        Point direction1 = tempTrack.getEnd();
                        Point direction2 = ((SwitchTrack) tempTrack).getEnd2();
                        for (Track trackInTemp : temp) {
                            if (trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct(temp, direction1),
                                getEndedStruct(new ArrayList<>(temp), direction2));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (tempTrack.getEnd().equals(lastPoint)) {
                        int i = 0;
                        Point direction1 = tempTrack.getStart();
                        Point direction2 = ((SwitchTrack) tempTrack).getEnd2();
                        for (Track trackInTemp : temp) {
                            if (trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct(new
                                ArrayList<Track>(temp), direction1), getEndedStruct(new ArrayList<>(temp), direction2));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    } else if (((SwitchTrack) tempTrack).getEnd2().equals(lastPoint)) {
                        int i = 0;
                        Point direction1 = tempTrack.getStart();
                        Point direction2 = tempTrack.getEnd();
                        for (Track trackInTemp : temp) {
                            if (trackInTemp.getId() == tempTrack.getId()) {
                                break;
                            }
                            i++;
                        }
                        temp.remove(i);
                        ArrayList<Track> endedStruct = combineEndedStructs(getEndedStruct(new
                                        ArrayList<Track>(temp), direction1), getEndedStruct(temp, direction2));
                        endedStruct.add(tempTrack);
                        return endedStruct;
                    }
                }
            }
            if (tempTrack.getStart().equals(lastPoint)) {
                return startEndStructExtract(temp, tempTrack);
            } else if (tempTrack.getEnd().equals(lastPoint)) {
                return endEndStructExtract(temp, tempTrack);
            }
        }
        //case when no connections where found
        return new ArrayList<>();
    }

    /**
     * combines two ended structs in one
     * @param struct1 first
     * @param struct2 second
     * @return result
     */
    public static ArrayList<Track> combineEndedStructs(ArrayList<Track> struct1, ArrayList<Track> struct2) {
        if (struct1 == null)
            return struct2;
        else if (struct2 == null)
            return struct1;
        Set<Track> set = new LinkedHashSet<>(struct1);
        set.addAll(struct2);
        return new ArrayList<Track>(set);
    }

    /**
     * uses getEndedStruct() to get ArrayList of the tracks of the TrainOnRoad
     * @param train train on railway
     * @param mainField field of search
     * @return arraylist of tracks
     */
    public static ArrayList<Track> getTracksFromTrainOnRoad(TrainOnRoad train, ArrayList<Track> mainField) {
        ArrayList<Track> field = new ArrayList<Track>(mainField);
        Point pointStartSearch = null;
        int i = 0;
        for (Track track : field) {
            boolean notTrackHead = track.getId() != train.getTrackHead().getId();
            boolean areStartConnected = track.getStart().equals(train.getTrackHead().getStart())
                    || track.getEnd().equals(train.getTrackHead().getStart());
            boolean areEndConnected = track.getStart().equals(train.getTrackHead().getEnd())
                    || track.getEnd().equals(train.getTrackHead().getEnd());
            boolean isForwardTrack = train.isStartDirection() ? areStartConnected : areEndConnected;
            if (isForwardTrack && notTrackHead) {
                pointStartSearch = areStartConnected ? train.getTrackHead().getStart() : train.getTrackHead().getEnd();
                break;
            }
            i++;
        }
        ArrayList<Track> tracksOfTrain = new ArrayList<Track>();
        if (i != field.size()) {
            field.remove(i);
            ArrayList<Track> forwardRoad;
            forwardRoad = RWstateUtils.getEndedStruct(field, pointStartSearch);
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

    /**
     * checks if train crosses the track
     * @param trainOnRoad1 train
     * @param track track for check
     * @param field field of search
     * @return true if crosses
     */
    public static final boolean haveCommonTrack(TrainOnRoad trainOnRoad1, Track track, ArrayList<Track> field) {
        if (trainOnRoad1.getPositionHead().equals(track.getStart())
                || trainOnRoad1.getPositionHead().equals(track.getEnd()))
            return true;
        ArrayList<Track> tracksOfFirst = RWstateUtils.getTracksFromTrainOnRoad(trainOnRoad1, field);
        for (Track track1 : tracksOfFirst) {
            if (track1.getId() == track.getId())
                return true;
        }
        return false;
    }

    /**
     * checks if trains crosse each other
     * @param trainOnRoad1 first
     * @param trainOnRoad2 second
     * @param field field of search
     * @return true if crosse
     */
    public static boolean haveCommonTracks(TrainOnRoad trainOnRoad1, TrainOnRoad trainOnRoad2, ArrayList<Track> field) {
        if (trainOnRoad1.getPositionHead().equals(trainOnRoad2.getPositionHead()))
            return true;
        ArrayList<Track> tracksOfFirst = RWstateUtils.getTracksFromTrainOnRoad(trainOnRoad1, field);
        ArrayList<Track> tracksOfSecond = RWstateUtils.getTracksFromTrainOnRoad(trainOnRoad2, field);
        for (Track track1 : tracksOfFirst) {
            for (Track track2 : tracksOfSecond) {
                if (track1.getId() == track2.getId())
                    return true;
            }
        }
        return false;
    }

    /**
     * find crosses of trains
     * @param movingTrains list of trains
     * @param field field of search
     * @return ids of crashes
     */
    public static ArrayList<ArrayList<Integer>> findCrashes(ArrayList<TrainOnRoad> movingTrains,
                                                            ArrayList<Track> field) {
        ArrayList<ArrayList<Integer>> crashesId = new ArrayList<ArrayList<Integer>>();
        for (TrainOnRoad train1 : movingTrains) {
            ArrayList<Integer> crashIds = new ArrayList<Integer>();
            for (TrainOnRoad train2 : movingTrains) {
                if ((train2.getTrain().getTrainId() != train1.getTrain().getTrainId())
                        && RWstateUtils.haveCommonTracks(train1, train2, field)) {
                    train1.setCrashed(true);
                    train2.setCrashed(true);
                    crashIds.add(train1.getTrain().getTrainId());
                    crashIds.add(train2.getTrain().getTrainId());
                }
            }
            if (train1.isCrashed()) {
                crashIds.add(train1.getTrain().getTrainId());
            }

            if (crashIds.size() != 0) {
                Collections.sort(crashIds);
                HashSet<Integer> temp = new HashSet<>(crashIds);
                crashIds.clear();
                crashIds.addAll(temp);
                crashesId.add(crashIds);
            }
        }
        if (crashesId.size() != 0) {
            HashSet<ArrayList<Integer>> temp = new HashSet<ArrayList<Integer>>(crashesId);
            crashesId.clear();
            crashesId.addAll(temp);
            crashesId.sort(Comparator.comparingInt(l -> l.get(0)));
            return crashesId;
        } else
            return new ArrayList<ArrayList<Integer>>();
    }

    /**
     * check if train goes out of the railway
     * @param train train
     * @param mainField field of search
     * @return true fo crash
     */
    public static boolean crashesWithTrack(TrainOnRoad train, ArrayList<Track> mainField) {
        ArrayList<Track> field = new ArrayList<Track>(mainField);
        int i = 0;
        for (Track track : field) {
            if (track.getId() != train.getTrackHead().getId())
                if (train.isStartDirection()) {
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
        if (i != field.size())
            field.remove(i);
        ArrayList<Track> endedStruct = new ArrayList<Track>();
        int trainLength = train.getLength();
        if (train.isStartDirection()) {
            endedStruct = RWstateUtils.getEndedStruct(field, train.getTrackHead().getStart());
            trainLength += Math.abs(train.getPositionHead().getX() + train.getPositionHead().getY()
                    - train.getTrackHead().getStart().getX() - train.getTrackHead().getStart().getY());
        } else {
            endedStruct = RWstateUtils.getEndedStruct(field, train.getTrackHead().getEnd());
            trainLength += Math.abs(train.getPositionHead().getX() + train.getPositionHead().getY()
                    - train.getTrackHead().getEnd().getX() - train.getTrackHead().getEnd().getY());
        }
        Collections.reverse(endedStruct);
        for (Track track : endedStruct) {
            trainLength -= track.getLength();
        }
        if (trainLength > 0)
            return true;
        else
            return false;
    }

    /**
     * creates string for crashes in move
     * @param movingTrains trains after step
     * @param idsCrashes crashes found
     * @return representation of crashes
     */
    public static String createStepString(ArrayList<TrainOnRoad> movingTrains,
                                          ArrayList<ArrayList<Integer>> idsCrashes) {
        String str = "";
        movingTrains.sort(Comparator.comparingInt(t -> t.getTrain().getTrainId()));
        if (idsCrashes.size() == 0) {
            for (TrainOnRoad train : movingTrains) {
                str += train + "\n";
            }
            return str.trim();
        }
        for (TrainOnRoad train : movingTrains) {
            if (train.isCrashed()) {
                ArrayList<Integer> crashIds = new ArrayList<Integer>();
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
                        for (int q = 1; q < crashIds.size(); q++) {
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

    /**
     * find Track from point and direction(for put)
     * @param pointFrom main point
     * @param direction point of direction
     * @param field field of search
     * @return track
     * @throws IncorrectInputException track not found
     */
    public static Track findTrack(Point pointFrom, Point direction, ArrayList<Track> field)
            throws IncorrectInputException {
        for (Track track : field) {
            if (RWstateUtils.pointBelongsTrack(pointFrom, track)) {
                boolean trackHor = track.getEnd().getY() == track.getStart().getY();
                boolean dirHor = pointFrom.getY() == direction.getY();
                if (trackHor == dirHor) {
                    //cause of two horizontal or vertical tracks check
                    for (Track track2 : field) {
                        if (track2.getId() != track.getId()) {
                            boolean track2Hor = track2.getEnd().getY() == track2.getStart().getY();
                            //two tracks are connected and have the same direction
                            if (track2.getId() != track.getId() && track2.getStart().equals(pointFrom)
                                    && (track2Hor == trackHor)) {
                                boolean positivDirectionTrack2 = track.getEnd().getX() + track.getEnd().getY()
                                        - track.getStart().getY() - track.getStart().getX() > 0;
                                boolean positivDirectionDir = direction.getY() + direction.getX()
                                        - pointFrom.getX() - pointFrom.getY() > 0;
                                if (positivDirectionDir != positivDirectionTrack2) {
                                    return track2;
                                }
                            } else if (track2.getId() != track.getId() && track2.getEnd().equals(pointFrom)
                                    && (track2Hor == trackHor)) {
                                boolean positivDirectionTrack2 = track.getStart().getX() + track.getStart().getY()
                                        - track.getEnd().getY() - track.getEnd().getX() > 0;
                                boolean positivDirectionDir = direction.getY() + direction.getX()
                                        - pointFrom.getX() - pointFrom.getY() > 0;
                                if (positivDirectionDir != positivDirectionTrack2) {
                                    return track2;
                                    //corner cause
                                } else if ((track2.getEnd().equals(pointFrom)
                                        || track2.getStart().equals(pointFrom)) && track2Hor != trackHor) {
                                    return track2;
                                }
                            }
                            return track;
                        }
                    }
                }
            }
        }
        throw new IncorrectInputException("track not found");
    }

    /**
     *c counts the length of ended struct
     * @param endedStruct connected tracks
     * @return length
     */
    public static int countLengthEndedStruct(ArrayList<Track> endedStruct) {
        if (endedStruct.size() == 0)
            return 0;
        else {
            int length = 0;
            for (Track track : endedStruct) {
                length += track.getLength();
            }
            return length;
        }
    }

    /**
     * removes crashed trains after step
     * @param movingTrains trains with crashed
     * @return not crashed trains
     */
    public static ArrayList<TrainOnRoad> removeCrashedTrains(ArrayList<TrainOnRoad> movingTrains) {
        ArrayList<TrainOnRoad> temp = new ArrayList<TrainOnRoad>();
        for (TrainOnRoad train : movingTrains) {
            if (!train.isCrashed())
                temp.add(train);
        }
        return temp;
    }

    /**
     * simplify of point search
     * @param pointFrom point main
     * @param direction point of direction
     * @return direction in main point system
     */
    public static Point getPointFromDirection(Point pointFrom, Point direction) {
        return new Point(pointFrom.getX() + direction.getX(), pointFrom.getY() + direction.getY());
    }
}
