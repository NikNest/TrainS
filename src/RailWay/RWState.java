package RailWay;

import RailWay.utils.*;

import java.util.*;

//убрать паблики потом
public class RWState {
    public RWState() {
        tracks = new ArrayList<>();
        trains = new Hashtable<>();
    }
    ArrayList<Track> tracks;
    Hashtable<Train, Point> trains;
    public void addTrack(Point start, Point end){

        if((start.getY() == end.getY()) || (start.getX() == end.getX())) {
            Track track = new Track(start, end);
            if(tracks.size() == 0) {
                tracks.add(track);
                System.out.println(track.getId());
                return;
            }
            Iterator<Track> iter = tracks.iterator();
            for (int i = 0; i < tracks.size(); i++){
                Track temptrack = iter.next();
                //track *---* temptrack *-----
                //                        \---
                if(temptrack.getStart().equals(start) || temptrack.getEnd().equals(end)) {
                    System.out.println("track with the same start or the same end already exists");
                    return;
                }

                if (Track.areEndStartConnected(track, temptrack)) {
                    tracks.get(i).setStartConnected(true);
                    track.setEndConnected(true);
                }
                //temptrack is Track
                if (!(SwitchTrack.class.isInstance(temptrack))) {
                    if (Track.areEndStartConnected(temptrack, track)) {
                        tracks.get(i).setEndConnected(true);
                        track.setStartConnected(true);
                    }
                } else {
                    if (SwitchTrack.areEndStartConnected( (SwitchTrack)temptrack, track)) {
                        ((SwitchTrack)tracks.get(i)).setEndconnected(true, track.getStart());
                        track.setStartConnected(true);
                    }
                }
            }
            if (!track.isStartConnected()) {
                if (!track.isEndConnected()) {
                    System.out.println("invalid track: no connection");
                    return;
                }
            }
            track.initId();
            tracks.add(track);
            System.out.println(track.getId());
        } else
            System.out.println("invalid track: not straight");
    }
    public void addSwitch(Point start, Point end, Point end2){
        if((start.getY() == end2.getY()) || (start.getX() == end2.getX())
                || (start.getY() == end.getY()) || (start.getX() == end.getX())) {
            SwitchTrack track = new SwitchTrack(start, end, end2);
            Iterator<Track> iter = tracks.iterator();
            for (int i = 0; i < tracks.size(); i++){
                Track temptrack = iter.next();
                if(temptrack.getStart().equals(start) ||
                        temptrack.getEnd().equals(end) || temptrack.getEnd().equals(end2)) {
                    System.out.println("track with the same start or the same end already exists");
                    return;
                }
                if (!(SwitchTrack.class.isInstance(temptrack))) {
                    if (Track.areEndStartConnected(temptrack, track)) {
                        tracks.get(i).setEndConnected(true);
                        track.setStartConnected(true);
                    }
                } else {
                    if (SwitchTrack.areEndStartConnected((SwitchTrack)temptrack, track)) {
                        ((SwitchTrack)tracks.get(i)).setEndconnected(true, track.getStart());
                        track.setStartConnected(true);
                    }
                }

                if (SwitchTrack.areEndStartConnected(track, temptrack)) {
                        tracks.get(i).setStartConnected(true);
                        track.setEndconnected(true, tracks.get(i).getStart());
                }
            }
            track.initId();
            tracks.add(track);
            System.out.println(track.getId());
        } else
            System.out.println("invalid switch-track: not straight");
    }
    public void setSwitch(int id, Point end) {
        Iterator<Track> iter = tracks.iterator();
        for (int i = 0; i < tracks.size(); i++) {
            Track track = iter.next();

            if(SwitchTrack.class.isInstance(track)) {
                if((((SwitchTrack)track).getId() == id) && (((SwitchTrack)track).getEnd().equals(end))) {
                    ((SwitchTrack) tracks.get(i)).setSwitch(true);
                    System.out.println("OK");
                    return;
                }
                if((((SwitchTrack)track).getId() == id) && (((SwitchTrack)track).getEnd2().equals(end))) {
                    ((SwitchTrack) tracks.get(i)).setSwitch(false);
                    System.out.println("OK");
                    return;
                }
            }

        }
        System.out.println("Switch with this id has no such an endpoin");


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
