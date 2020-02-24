package RailWay.utils;

public final class TrainOnRoad {
    public TrainOnRoad(Train train, Track track, boolean isStartDirection, Point currentPosition) {
        this.train = train;
        this.trackHead = track;
        this.isStartDirection = isStartDirection;
        this.positionHead = currentPosition;

    }
    private Train train;
    private Track trackHead;
    //direction of moving forward
    private boolean isStartDirection;
    private Point positionHead;
    private boolean crashed = false;
    private boolean movingBack = false;
    public final void setMovingBack(boolean movingBack) {
        this.movingBack = movingBack;
    }
    public final void setStartDirection(boolean isStartDirection) {
        this.isStartDirection = isStartDirection;
    }
    public final void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }
    public final boolean isMovingBack() {
        return movingBack;
    }
    public final boolean isCrashed() {
        return crashed;
    }
    public final Train getTrain() {
        return train;
    }
    public final int getLength() {
        return train.getTrainLength();
    }
    public final Track getTrackHead() {
        return trackHead;
    }
    public final boolean isStartDirection() {
        return isStartDirection;
    }
    public final Point getPositionHead() {
        return positionHead;
    }
    @Override
    public final String toString() {
        return "Train " + train.getTrainId() + " at " + positionHead;
    }
}
