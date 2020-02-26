package railway.utils;

/**
 * class for track of railway
 * @author Nikita
 * @version 1
 */
public class Track {
    private int id;
    private int length;
    private Point start;
    private Point end;
    private boolean endconnected;
    private boolean startconnected;

    /**
     * track constructor
     * @param start start point
     * @param end end point
     */
    public Track(Point start, Point end) {
        this.start = start;
        this.end = end;
        length = start.countLength(end);
    }

    /**
     * setter for track id
     * @param id id of the track
     */
    public void setId(int id) { this.id = id; }

    /**
     * setter for track end
     * @param end
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * setter for connection status
     * @param endconnected status
     */
    public void setEndConnected(boolean endconnected) {
        this.endconnected = endconnected;
    }

    /**
     * setter for connection status
     * @param startconnected status
     */
    public void setStartConnected(boolean startconnected) {
        this.startconnected = startconnected;
    }

    /**
     * set connected for switch
     * @param connectionPoint point of the connection
     * @return true if connected
     */
    public boolean connect(Point connectionPoint) {
        if (connectionPoint.equals(start)) {
            startconnected = true;
            return true;
        } else if (connectionPoint.equals(end)) {
            endconnected = true;
            return true;
        }
        return false;
    }

    /**
     * getter for common points of tracks
     * @param track track to check
     * @return status
     */
    public Point getCommonPoint(Track track) {
        if (this.isConnectedWith(track)) {
            if (track instanceof SwitchTrack) {
                if (((SwitchTrack) track).getEnd2().equals(start))
                    return start;
                else if (((SwitchTrack) track).getEnd2().equals(end))
                    return end;
            }
            if (track.getStart().equals(start) || track.getEnd().equals(start))
                return start;
            else if (track.getStart().equals(end) || track.getEnd().equals(end))
                return end;
            else
                return null;
        } else
            return null;
    }

    /**
     * return x if track vertical else y
     * @return
     */
    public int getSameCoord() {
        if (start.getX() == end.getX())
            return start.getX();
        else
            return start.getY();
    }

    /**
     * getter for start point
     * @return start point
     */
    public Point getStart() {
        return start;
    }

    /**
     * getter for end point
     * @return end point
     */
    public Point getEnd() {
        return end;
    }

    /**
     * getter for track id
     * @return id of track
     */
    public int getId() {
        return id;
    }

    /**
     * getter length
     * @return track length
     */
    public int getLength() {
        return length;
    }

    /**
     * check connection status
     * @param track track for search
     * @return connection status
     */
    public boolean isConnectedWith(Track track) {
        if (track instanceof SwitchTrack) {
            if (((SwitchTrack) track).getEnd2().equals(end) || ((SwitchTrack) track).getEnd2().equals(start))
                return true;
        } else if (track.equals(this))
            return false;
        return track.getStart().equals(end) || track.getEnd().equals(end)
                || track.getStart().equals(start) || track.getEnd().equals(start);
    }

    /**
     * check if the tracks are illigal connected(cross of tracks)
     * @param track track to check
     * @return true if cross hap
     */
    public boolean areIlligalConnected(Track track) {
        if (track instanceof SwitchTrack) {
            if (this.getStart().equals(((SwitchTrack) track).getEnd2())
                    && (this.getEnd().equals(track.getEnd())
                    || this.getEnd().equals(track.getStart())) )
                return true;
            return this.getEnd().equals(((SwitchTrack) track).getEnd2())
                    && (this.getStart().equals(track.getEnd())
                    || this.getStart().equals(track.getStart()));
        } else
            return track.equals(this);

    }

    /**
     * string representation of the switch-track
     * @return str
     */
    @Override
    public String toString() {
        return "t " + id + " " + start + " -> " + end + " " + length;
    }

    /**
     * check if the tracks are same
     * @param obj second track
     * @return status
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SwitchTrack) {
            return false;
        }
        if (!(obj instanceof Track))
            return false;
        return (this.start == ((Track) obj).start || this.start == ((Track) obj).end)
                && (this.end == ((Track) obj).start || this.end == ((Track) obj).end);
    }

    /**
     * custom hashcode
     * @return id of track(is unique)
     */
    @Override
    public int hashCode() {
        return getId();
    }

}
