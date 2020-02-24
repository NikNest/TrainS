package RailWay.utils;

public class Track {
    public Track(Point start, Point end) {
        this.start = start;
        this.end = end;
        length = start.countLength(end);
    }

    private int id;
    private int length;
    private Point start;
    private Point end;
    private boolean endconnected;
    private boolean startconnected;

    public final void setId(int id) { this.id = id;}
    public final void setEnd(Point end) {
        this.end = end;
    }
    public final void setEndConnected(boolean endconnected) {
        this.endconnected = endconnected;
    }
    public final void setStartConnected(boolean startconnected) {
        this.startconnected = startconnected;
    }

    public boolean connect(Point connectionPoint) {
        if(connectionPoint.equals(start)) {
            startconnected = true;
            return true;
        } else if (connectionPoint.equals(end)) {
            endconnected = true;
            return true;
        }
        return false;
    }
    public Point getCommonPoint(Track track) {
        if(this.isConnectedWith(track)) {
            if(SwitchTrack.class.isInstance(track)) {
                if(((SwitchTrack) track).getEnd2().equals(start))
                    return start;
                else if (((SwitchTrack) track).getEnd2().equals(end))
                    return end;
            }
            if(track.getStart().equals(start) || track.getEnd().equals(start))
                return start;
            else if(track.getStart().equals(end) || track.getEnd().equals(end))
                return end;
            else
                return null;
        } else
            return null;
    }
    public final int getSameCoord() {
        if(start.getX() == end.getX())
            return start.getX();
        else
            return start.getY();
    }
    public final Point getStart() {
        return start;
    }
    public final Point getEnd() {
        return end;
    }
    public final int getId() {
        return id;
    }
    public int getLength() {
        return length;
    }
    public boolean isConnectedWith(Track track) {
        if(SwitchTrack.class.isInstance(track)) {
            if(((SwitchTrack)track).getEnd2().equals(end) || ((SwitchTrack)track).getEnd2().equals(start))
                return true;
        } else if(track.equals(this))
            return false;
        if(track.getStart().equals(end) || track.getEnd().equals(end)
                || track.getStart().equals(start) || track.getEnd().equals(start)) {
            return true;
        } else
            return false;
    }

    @Override
    public String toString() {
        return "t " + id + " " + start + " -> " + end + " " + length;
    }
    @Override
    public boolean equals(Object obj) {
        if(SwitchTrack.class.isInstance(obj))
            return false;
        if(!Track.class.isInstance(obj))
            return false;
        return (this.start == ((Track)obj).start || this.start == ((Track)obj).end)
                && (this.end == ((Track)obj).start || this.end == ((Track)obj).end);
    }
    @Override
    public int hashCode() {
        return getId();
    }

}
