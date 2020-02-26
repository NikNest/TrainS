package railway.utils;

/**
 * class for moving trains
 */
public final class TrainOnRoad {
    private Train train;
    private Track trackHead;
    //direction of moving forward
    private boolean isStartDirection;
    private Point positionHead;
    private boolean crashed = false;
    private boolean movingBack = false;

    /**
     * moving train constructor
     * @param train moving train
     * @param track track of train head
     * @param isStartDirection direction of train move
     * @param currentPosition point position of head
     */
    public TrainOnRoad(Train train, Track track, boolean isStartDirection, Point currentPosition) {
        this.train = train;
        this.trackHead = track;
        this.isStartDirection = isStartDirection;
        this.positionHead = currentPosition;

    }

    /**
     * set moving status
     * @param movingBack status
     */
    public void setMovingBack(boolean movingBack) {
        this.movingBack = movingBack;
    }

    /**
     * set direction status
     * @param isStartDirection status
     */
    public void setStartDirection(boolean isStartDirection) {
        this.isStartDirection = isStartDirection;
    }

    /**
     * setter crash status
     * @param crashed status
     */
    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    /**
     * getter moving status
     * @return status
     */
    public boolean isMovingBack() {
        return movingBack;
    }

    /**
     * getter for crash status
     * @return status
     */
    public boolean isCrashed() {
        return crashed;
    }

    /**
     * getter for train of moving train
     * @return train
     */
    public Train getTrain() {
        return train;
    }

    /**
     * getter for train length
     * @return length
     */
    public int getLength() {
        return train.getTrainLength();
    }

    /**
     * getter for track head
     * @return track
     */
    public Track getTrackHead() {
        return trackHead;
    }

    /**
     * getter for start direction
     * @return direction
     */
    public boolean isStartDirection() {
        return isStartDirection;
    }

    /**
     * getter for position head
     * @return position of head
     */
    public Point getPositionHead() {
        return positionHead;
    }

    /**
     * count head point from direction step and track. for move(step)
     * @param startDirection start position
     * @param positivDirection direction for dist counting
     * @param stepLength length of step
     * @param newHeadTrack head track
     * @return point
     */
    public static Point countHeadPoint(boolean startDirection, boolean positivDirection, int stepLength, Track newHeadTrack) {
        Point newHeadPoint;
        if (stepLength == 0) {
            if (startDirection)
                return newHeadTrack.getStart();
            else
                return newHeadTrack.getEnd();
        }
        int absLength = Math.abs(stepLength);
        if (newHeadTrack.getStart().getX() == newHeadTrack.getEnd().getX()) {
            if (startDirection) {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX(),
                        newHeadTrack.getStart().getY()  - absLength) : new Point(newHeadTrack.getStart().getX(),
                        newHeadTrack.getStart().getY() + absLength);
            } else {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX(),
                        newHeadTrack.getEnd().getY() - absLength)
                        : new Point(newHeadTrack.getStart().getX(), newHeadTrack.getEnd().getY() + absLength);
            }

        } else {
            if (startDirection) {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getStart().getX() - absLength,
                        newHeadTrack.getStart().getY()) : new Point(newHeadTrack.getStart().getX()
                        + absLength , newHeadTrack.getStart().getY());
            } else {
                newHeadPoint = positivDirection ? new Point(newHeadTrack.getEnd().getX() - absLength,
                        newHeadTrack.getStart().getY()) : new Point(newHeadTrack.getEnd().getX()
                        + absLength, newHeadTrack.getStart().getY());
            }
        }
        return newHeadPoint;
    }

    /**
     * string representation of moving train
     * @return info
     */
    @Override
    public String toString() {
        return "Train " + train.getTrainId() + " at " + positionHead;
    }
}
