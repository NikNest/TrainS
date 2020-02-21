package RailWay.utils;

import java.util.ArrayList;

public class MovingTrain {
    int trainId;
    boolean crashed = false;
    public MovingTrain(ArrayList<Track> usedTracks, int trainId) {
        this.trainId = trainId;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }
}
