package RailWay.utils;

import java.util.ArrayList;

public final class SwitchTrack extends Track{
    public SwitchTrack(Point start, Point end, Point end2) {
        super(start, end);
        this.end2 = end2;
    }
    private Point end2;
    private boolean switchSetted = false;
    private boolean firstendConnected;
    private boolean secondendConnected;

    public final boolean isSwitchSetted() {
        return switchSetted;
    }

    public final Point getEnd2() {
        return end2;
    }
    public final void setSecondendConnected(boolean state) {
        secondendConnected = state;
    }
    public final void setEndconnected(boolean flag, Point point) {
        if(getEnd().equals(point)) {
            firstendConnected = flag;
        } else if(end2.equals(point)) {
            secondendConnected = flag;
        }
    }
    //if start not connected then it could be only one valid direction
    public final void setDirection(Point direction) {
        if (getEnd().equals(direction)) {
            switchSetted = true;
        } else if(end2.equals(direction)){
            Point temp = end2;
            end2 = getEnd();
            setEnd(temp);
            switchSetted = true;
            boolean flag = firstendConnected;
            firstendConnected = secondendConnected;
            secondendConnected = flag;
        }
    }

    @Override
    public boolean areIlligalConnected(Track track) {
        Point[] thisTrackPoints = {getStart(), getEnd(), getEnd2()};
        Point[] secondTrackPoints;
        if(track instanceof SwitchTrack) {
            secondTrackPoints = new Point[]{track.getStart(), track.getEnd(), ((SwitchTrack) track).getEnd2()};
        }
        else {
            secondTrackPoints = new Point[]{track.getStart(), track.getEnd()};
        }
        int commonPointsNum = 0;
        for(Point point1 : thisTrackPoints) {
            for (Point point2 : secondTrackPoints) {
                if(point1.equals(point2))
                    commonPointsNum++;
            }
        }
        return commonPointsNum > 1;

    }

    @Override
    public int getLength(){
        return getStart().countLength(getEnd());
    }
    @Override
    public final Point getCommonPoint(Track track) {
        //maby dont use isconnected here because there no reason to use it here
        if(this.isConnectedWith(track)) {
            if(track instanceof SwitchTrack) {
                if(((SwitchTrack) track).getEnd2().equals(getStart()))
                    return getStart();
                else if (((SwitchTrack) track).getEnd2().equals(getEnd()))
                    return getEnd();
                else if (((SwitchTrack) track).getEnd2().equals(end2))
                    return end2;
            }
            if(track.getStart().equals(getStart()) || track.getEnd().equals(getStart()))
                return getStart();
            else if(track.getStart().equals(getEnd()) || track.getEnd().equals(getEnd()))
                return getEnd();
            else if (track.getStart().equals(end2) || track.getEnd().equals(end2))
                return end2;
            else
                return null;
        } else
            return null;
    }
    @Override
    public final boolean connect(Point connectionPoint) {
        if(connectionPoint.equals(getStart())) {
            setStartConnected(true);
            return true;
        } else if(connectionPoint.equals(getEnd()) || connectionPoint.equals(end2)) {
            setEndconnected(true, connectionPoint);
            return true;
        }
        return false;
    }
    @Override
    public final boolean isConnectedWith(Track track) {
        if(track instanceof SwitchTrack) {
            if(track.equals(this))
                return false;
            if(((SwitchTrack)track).getEnd2().equals(getEnd()) || ((SwitchTrack)track).getEnd2().equals(getStart())
                    || ((SwitchTrack)track).getEnd2().equals(end2))
                return true;
        }
            return track.getStart().equals(getEnd()) || track.getEnd().equals(getEnd())
                    || track.getStart().equals(end2) || track.getEnd().equals(end2)
                    || track.getStart().equals(getStart()) || track.getEnd().equals(getStart());
    }
    @Override
    public final boolean equals(Object obj) {
        if(!SwitchTrack.class.isInstance(obj))
            return false;
        if(Track.class.isInstance(obj))
            return false;
        return (this.getStart() == ((Track) obj).getStart() || this.getStart() == ((Track) obj).getEnd())
                && (this.getEnd() == ((Track) obj).getStart() || this.getEnd() == ((Track) obj).getEnd());
    }
    @Override
    public final String toString() {
        String str = "s " + super.getId() + " " + super.getStart() + " -> " + super.getEnd() + "," + end2;
        if(switchSetted) str += " " + getLength();
        return str;
    }
}
