package RailWay.utils;

public class TrainOnRoad {
    Train train;
    Track trackHead;
    boolean isStartDirection;
    Point positionHead;

    public TrainOnRoad(Train train, Track track, boolean isStartDirection, Point currentPosition) {
        this.train = train;
        this.trackHead = track;
        this.isStartDirection = isStartDirection;
        this.positionHead = currentPosition;
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
}
