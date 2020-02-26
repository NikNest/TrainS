package railway.utils;

/**
 * class for switch tracks
 * @author Nikita
 * @version 1
 */
public final class SwitchTrack extends Track {
    private Point end2;
    private boolean switchSetted = false;
    private boolean firstendConnected;
    private boolean secondendConnected;

    /**
     * switch track constructor
     * @param start point
     * @param end point
     * @param end2 point
     */
    public SwitchTrack(Point start, Point end, Point end2) {
        super(start, end);
        this.end2 = end2;
    }

    /**
     * getter for switch state
     * @return state
     */
    public boolean isSwitchSetted() {
        return switchSetted;
    }

    /**
     * getter for second end
     * @return second end point
     */
    public Point getEnd2() {
        return end2;
    }

    /**
     * connect second end point
     * @param state state of connection
     */
    public void setSecondendConnected(boolean state) {
        secondendConnected = state;
    }

    /**
     * simplify of setter for connection
     * @param flag state of connection
     * @param point end
     */
    public void setEndconnected(boolean flag, Point point) {
        if (getEnd().equals(point)) {
            firstendConnected = flag;
        } else if (end2.equals(point)) {
            secondendConnected = flag;
        }
    }

    /**
     * if start not connected then it could be only one valid direction
     * @param direction witch point is first end point
     */
    public void setDirection(Point direction) {
        if (getEnd().equals(direction)) {
            switchSetted = true;
        } else if (end2.equals(direction)) {
            Point temp = end2;
            end2 = getEnd();
            setEnd(temp);
            switchSetted = true;
            boolean flag = firstendConnected;
            firstendConnected = secondendConnected;
            secondendConnected = flag;
        }
    }

    /**
     * check if the tracks are illigal connected(cross of tracks)
     * @param track track to check
     * @return true if cross hap
     */
    @Override
    public boolean areIlligalConnected(Track track) {
        Point[] thisTrackPoints = {getStart(), getEnd(), getEnd2()};
        Point[] secondTrackPoints;
        if (track instanceof SwitchTrack) {
            secondTrackPoints = new Point[]{track.getStart(), track.getEnd(), ((SwitchTrack) track).getEnd2()};
        }
        else {
            secondTrackPoints = new Point[]{track.getStart(), track.getEnd()};
        }
        int commonPointsNum = 0;
        for (Point point1 : thisTrackPoints) {
            for (Point point2 : secondTrackPoints) {
                if (point1.equals(point2))
                    commonPointsNum++;
            }
        }
        return commonPointsNum > 1;

    }

    /**
     * getter for track length
     * @return length
     */
    @Override
    public int getLength() {
        return getStart().countLength(getEnd());
    }

    /**
     * getter for common point with another track
     * @param track another track
     * @return common point
     */
    @Override
    public Point getCommonPoint(Track track) {
        //maby dont use isconnected here because there no reason to use it here
        if (this.isConnectedWith(track)) {
            if (track instanceof SwitchTrack) {
                if (((SwitchTrack) track).getEnd2().equals(getStart()))
                    return getStart();
                else if (((SwitchTrack) track).getEnd2().equals(getEnd()))
                    return getEnd();
                else if (((SwitchTrack) track).getEnd2().equals(end2))
                    return end2;
            }
            if (track.getStart().equals(getStart()) || track.getEnd().equals(getStart()))
                return getStart();
            else if (track.getStart().equals(getEnd()) || track.getEnd().equals(getEnd()))
                return getEnd();
            else if (track.getStart().equals(end2) || track.getEnd().equals(end2))
                return end2;
            else
                return null;
        } else
            return null;
    }

    /**
     * set connected for switch
     * @param connectionPoint point of the connection
     * @return true if connected
     */
    @Override
    public boolean connect(Point connectionPoint) {
        if (connectionPoint.equals(getStart())) {
            setStartConnected(true);
            return true;
        } else if (connectionPoint.equals(getEnd()) || connectionPoint.equals(end2)) {
            setEndconnected(true, connectionPoint);
            return true;
        }
        return false;
    }

    /**
     * check connection of two tracks
     * @param track second track
     * @return true if connected
     */
    @Override
    public boolean isConnectedWith(Track track) {
        if (track instanceof SwitchTrack) {
            if (track.equals(this))
                return false;
            if (((SwitchTrack) track).getEnd2().equals(getEnd()) || ((SwitchTrack) track).getEnd2().equals(getStart())
                    || ((SwitchTrack) track).getEnd2().equals(end2))
                return true;
        }
        return track.getStart().equals(getEnd()) || track.getEnd().equals(getEnd())
                    || track.getStart().equals(end2) || track.getEnd().equals(end2)
                    || track.getStart().equals(getStart()) || track.getEnd().equals(getStart());
    }

    /**
     * check if the tracks are same
     * @param obj second track
     * @return status
     */
    @Override
    public boolean equals(Object obj) {
        if (!SwitchTrack.class.isInstance(obj))
            return false;
        if (Track.class.isInstance(obj))
            return false;
        return (this.getStart() == ((Track) obj).getStart() || this.getStart() == ((Track) obj).getEnd())
                && (this.getEnd() == ((Track) obj).getStart() || this.getEnd() == ((Track) obj).getEnd());
    }

    /**
     * string representation of the switch-track
     * @return str
     */
    @Override
    public String toString() {
        String str = "s " + super.getId() + " " + super.getStart() + " -> " + super.getEnd() + "," + end2;
        if (switchSetted) str += " " + getLength();
        return str;
    }
}
