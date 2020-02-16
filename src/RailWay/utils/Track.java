package RailWay.utils;

public class Track {
    public Track(Point start) {
        this.start = start;
    }
    public Track(Point start, Point end) {
        this.start = start;
        this.end = end;
//        id = ++lastidnormal;
        length = start.countLength(end);
//        normalTrack = true;
    }

    static int lastidnormal = 0;
    int id;
    int length;
    Point start;
    Point end;
    //boolean normalTrack;

    boolean endconnected;
    boolean startconnected;

    public void initId() {
        id  = ++lastidnormal;
    }
    public boolean areStartConnected(Track track) {
        if (!track.isEndConnected() && (track.getEnd().equals(start)))
            return true;
        else
            return false;

    }
    public static boolean areEndStartConnected(Track track1, Track track2) {
        if (!track2.isStartConnected() && !track1.isEndConnected() && (track2.getStart().equals(track1.getEnd())))
            return true;
        else
            return false;

    }
    public void setEndConnected(boolean endconnected) {
        this.endconnected = endconnected;
    }

    public void setStartConnected(boolean startconnected) {
        this.startconnected = startconnected;
    }

    public boolean isEndConnected() {
        return endconnected;
    }

    public boolean isStartConnected() {
        return startconnected;
    }

    public Point getStart() {
        return start;
    }
    public Point getEnd() {
        return end;
    }
//    public boolean isNormalTrack() {
//        return normalTrack;
//    }
    public int getId() {
        return id;
    }
    public int getLength() {
        return length;
    }
    //t 1 (1,1) -> (5,1) 5
    @Override
    public String toString() {
        return "t " + id + " " + start + " -> " + end + " " + length;
    }
}
