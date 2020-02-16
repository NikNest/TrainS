package RailWay.utils;

public class SwitchTrack extends Track{
//    static int lastidswitch = 0;
    Point end;
    Point end2;
    boolean switchSetted = false;
    boolean firstEndPoint;
    boolean firstendConnected;
    boolean secondendConnected;

    public SwitchTrack(Point start, Point end, Point end2) {
        super(start);
        this.start = start;
        this.end = end;
        this.end2 = end2;
//          id  = ++lastidswitch;
//        normalTrack = false;
    }
//    @Override
//    public void initId() {
//        id  = ++lastidswitch;
//    }
//    public boolean isSwitchSetted() {
//       return switchSetted;
//    }
    public int getLength(){
        if(firstEndPoint)
            return start.countLength(end);
        return start.countLength(end2);
    }
    public void setEndconnected(boolean flag, Point start) {
        if(this.end.equals(start))
            firstendConnected = flag;
        if(this.end2.equals(start))
            secondendConnected = flag;
    }
    public static boolean areEndStartConnected(SwitchTrack track1, Track track2) {
        if (!track1.isEndConnected() && !track2.isStartConnected() &&
                ((track2.getStart().equals(track1.getEnd())) || (track2.getStart().equals(track1.getEnd2()))))
            return true;
        else
            return false;

    }
    public void setSwitch(boolean isFirst) {
        firstEndPoint = isFirst;
        switchSetted = true;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public Point getEnd() {
        return end;
    }

    public Point getEnd2() {
        return end2;
    }

    //s 2 (5,1) -> (8,1),(5,3)
    @Override
    public String toString() {
        String str = "s " + id + " " + start + " -> " + end + "," + end2;
        if(switchSetted) str += " " + getLength();
        return str;
    }
}
