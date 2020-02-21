package RailWay.utils;

public class TrainOnRoad {
    Train train;
    Track trackHead;
    //direction of moving forward
    boolean isStartDirection;
    Point positionHead;

    public TrainOnRoad(Train train, Track track, boolean isStartDirection, Point currentPosition) {
        this.train = train;
        this.trackHead = track;
        this.isStartDirection = isStartDirection;
        this.positionHead = currentPosition;
    }

    public Train getTrain() {
        return train;
    }
    public int getLengthHead() {
        return isStartDirection ? Math.abs(positionHead.getX() + positionHead.getY() - trackHead.getEnd().getX() - trackHead.getEnd().getY()) :
                Math.abs(positionHead.getX() + positionHead.getY() - trackHead.getStart().getX() - trackHead.getStart().getY());
    }
    public int getLength() {
        return train.getTrainLength();
    }
    public Track getTrackHead() {
        return trackHead;
    }
    public boolean isStartDirection() {
        return isStartDirection;
    }
    public Point getPositionHead() {
        return positionHead;
    }

    @Override
    public String toString() {
        return "Train " + train.getTrainId() + " at " + positionHead;
    }
}
