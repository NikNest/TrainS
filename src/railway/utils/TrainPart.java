package railway.utils;

/**
 * class for train part
 */
public class TrainPart {
    private int trainId = 0;
    private int length;
    private boolean forwConnection;
    private boolean backConnection;

    /**
     * setter for train part's train id
     * @param trainId train's id
     */
    public final void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    /**
     * setter for trainpart's length
     * @param length the length
     */
    public final void setLength(int length) {
        this.length = length;
    }

    /**
     * setter for connection status
     * @param forwConnection status
     */
    public final void setForwConnection(boolean forwConnection) {
        this.forwConnection = forwConnection;
    }

    /**
     * setter for connection status
     * @param backConnection status
     */
    public final void setBackConnection(boolean backConnection) {
        this.backConnection = backConnection;
    }

    /**
     * getter for connection status
     * @return status
     */
    public final boolean isForwConnection() {
        return forwConnection;
    }

    /**
     * getter for connection status
     * @return status
     */
    public final boolean isBackConnection() {
        return backConnection;
    }

    /**
     * length getter
     * @return length
     */
    public final int getLength() {
        return length;
    }

    /**
     * getter for train id
     * @return train id
     */
    public final int getTrainId() {
        return trainId;
    }
}
