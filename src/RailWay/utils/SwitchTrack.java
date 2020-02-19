package RailWay.utils;

public class SwitchTrack extends Track{
//    Point end;
    Point end2;
    boolean switchSetted = false;
//    boolean firstEndPoint;
    boolean firstendConnected;
    boolean secondendConnected;

    public SwitchTrack(Point start, Point end, Point end2) {
        //rewrite super
        super(start);
        this.start = start;
        this.end = end;
        this.end2 = end2;
    }

    public boolean isFirstendConnected() {
        return firstendConnected;
    }

    public boolean isSecondendConnected() {
        return secondendConnected;
    }

    public boolean isSwitchSetted() {
        return switchSetted;
    }
    public int getLength(){
            return start.countLength(end);
        }
    public void setEndconnected(boolean flag, Point point) {
        if(end.equals(point)) {
            firstendConnected = flag;
        } else if(end2.equals(point)) {
            secondendConnected = flag;
        }
    }
    //if start not connected then it could be only one valid direction
    public void setDirection(Point direction) {
        if (end.equals(direction)) {
            switchSetted = true;
        } else if(end2.equals(direction)){
            Point temp = end2;
            end2 = end;
            end = temp;
            switchSetted = true;
            boolean flag = firstendConnected;
            firstendConnected = secondendConnected;
            secondendConnected = flag;
        }
    }

    @Override
    public Point getCommonPoint(Track track) {
        //maby dont use isconnected here because there no reason to use it here
        if(this.isConnectedWith(track)) {
            if(SwitchTrack.class.isInstance(track)) {
                if(((SwitchTrack) track).getEnd2().equals(start))
                    return start;
                else if (((SwitchTrack) track).getEnd2().equals(end))
                    return end;
                else if (((SwitchTrack) track).getEnd2().equals(end2))
                    return end2;
            }
            if(track.getStart().equals(start) || track.getEnd().equals(start))
                return start;
            else if(track.getStart().equals(end) || track.getEnd().equals(end))
                return end;
            else if (track.getStart().equals(end2) || track.getEnd().equals(end2))
                return end2;
            else
                return null;
        } else
            return null;
    }

    @Override
    public boolean connect(Point connectionPoint) {
        if(connectionPoint.equals(start)) {
            setStartConnected(true);
            return true;
        } else if(connectionPoint.equals(end) || connectionPoint.equals(end2)) {
            setEndconnected(true, connectionPoint);
            return true;
        }
        return false;
    }

    public Point getEnd2() {
        return end2;
    }

    @Override
    public boolean isConnectedWith(Track track) {
        if(SwitchTrack.class.isInstance(track)) {
            if(((SwitchTrack)track).equals(this))
                return false;
            if(((SwitchTrack)track).getEnd2().equals(end) || ((SwitchTrack)track).getEnd2().equals(start) || ((SwitchTrack)track).getEnd2().equals(end2))
                return true;
        }
        if (track.getStart().equals(end) || track.getEnd().equals(end)
                || track.getStart().equals(end2) || track.getEnd().equals(end2)
                || track.getStart().equals(start) || track.getEnd().equals(start)) {
            return true;
        } else
            return false;
    }
    //TODO check instace of
    @Override
    public boolean equals(Object obj) {
        if(!SwitchTrack.class.isInstance(obj))
            return false;
        if(Track.class.isInstance(obj))
            return false;
        return (this.start == ((Track)obj).start || this.start == ((Track)obj).end)
                && (this.end == ((Track)obj).start || this.end == ((Track)obj).end);
    }

    //s 2 (5,1) -> (8,1),(5,3)
    @Override
    public String toString() {
        String str = "s " + id + " " + start + " -> " + end + "," + end2;
        if(switchSetted) str += " " + getLength();
        return str;
    }
}
