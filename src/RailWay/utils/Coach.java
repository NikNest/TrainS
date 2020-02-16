package RailWay.utils;

public class Coach extends TrainPart {
    public Coach(String coachType, int length, boolean forwConnected, boolean backConnected) {
        //this.trainPartType = "coach";
        this.type = coachType;
        this.length = length;
        this.forwConnection = forwConnected;
        this.backConnection = backConnected;
    }
    String type;
    int id;
    static int lastCoachId = 0;
    public void initId() {
        this.id = ++lastCoachId;
    }
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        String str = "";
        str += id + " ";
        if(isTrainPart) str += trainId + " ";
        else str += "none ";
        str += type + " " + length + " " + forwConnection + " " + backConnection;
        return str;
    }
}
