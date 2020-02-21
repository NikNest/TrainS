package RailWay.utils;

public class Track {
    public Track(Point start) {
        this.start = start;
    }
    public Track(Point start, Point end) {
        this.start = start;
        this.end = end;
        length = start.countLength(end);
    }

    int id;
    int length;
    Point start;
    Point end;

    boolean endconnected;
    boolean startconnected;

    public void setEndConnected(boolean endconnected) {
        this.endconnected = endconnected;
    }

    public void setStartConnected(boolean startconnected) {
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
    //TODO implement this method everywhere
    //
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
    public boolean isEndConnected() {
        return endconnected;
    }

    public boolean isStartConnected() {
        return startconnected;
    }
    public int getSameCoord() {
        if(start.getX() == end.getX())
            return start.getX();
        else
            return start.getY();
    }
    public int getFromCoord() {
        return (start.getX() == getSameCoord()) ? Math.min(start.getY(), end.getY()) : Math.min(start.getX(), end.getX());
    }
    public int getToCoord() {
        return (start.getX() == getSameCoord()) ? Math.max(start.getY(), end.getY()) : Math.max(start.getX(), end.getX());
    }
    public Point getStart() {
        return start;
    }
    public Point getEnd() {
        return end;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id;}
    public int getLength() {
        return length;
    }
    //t 1 (1,1) -> (5,1) 5
    @Override
    public String toString() {
        return "t " + id + " " + start + " -> " + end + " " + length;
    }

    //implement usage of this method in add track, create(?) override version in switch, then add this method's version to addswitch
    //TODO check instace of
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

    //TODO
    public Point getNextPoint(Point lastPoint, boolean directionToStart) {

        if(directionToStart) {
            if(lastPoint.getX() == start.getX())
                return start.getY() > end.getY() ? new Point(lastPoint.getX(), lastPoint.getY() + 1) : new Point(lastPoint.getX(), lastPoint.getY() - 1);
            else
                return start.getX() > end.getX() ? new Point(lastPoint.getX() + 1, lastPoint.getY()) : new Point(lastPoint.getX() - 1, lastPoint.getY());
        } else {
            if(lastPoint.getX() == start.getX())
                return start.getY() < end.getY() ? new Point(lastPoint.getX(), lastPoint.getY() + 1) : new Point(lastPoint.getX(), lastPoint.getY() - 1);
            else
                return start.getX() < end.getX() ? new Point(lastPoint.getX() + 1, lastPoint.getY()) : new Point(lastPoint.getX() - 1, lastPoint.getY());
        }
    }
    //возможно заменить методом getCommonPoint
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
}
