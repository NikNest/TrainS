package railway.utils;

/**
 * class for train sets(type of tain part)
 * @author Nikita
 * @version 1
 */
public final class TrainSet extends TrainPart implements SpecialIdable {
    private final String trainsetClass;
    private final String trainsetName;

    /**
     * trainset constructor
     * @param trainsetClass class of trainset
     * @param trainsetName name of trainset
     * @param length length of train part
     * @param forwConnected connection status
     * @param backConnected connection status
     */
    public TrainSet(String trainsetClass, String trainsetName,
                    int length, boolean forwConnected, boolean backConnected) {
        this.trainsetClass = trainsetClass;
        this.trainsetName = trainsetName;
        this.setLength(length);
        this.setForwConnection(forwConnected);
        this.setBackConnection(backConnected);
    }

    /**
     * getter for train part class
     * @return
     */
    @Override
    public String getSpecialClass() {
        return trainsetClass;
    }

    /**
     * getter for train part name
     * @return
     */
    @Override
    public String getSpecialName() {
        return trainsetName;
    }

    /**
     * string representation of the trainset
     * @return trainset info
     */
    @Override
    public String toString() {
        String str = "";
        if (getTrainId() != 0) str += getTrainId() + " ";
        else str += "none ";
        str += trainsetClass + " " + trainsetName + " " + getLength() + " "
            + isForwConnection() + " " + isBackConnection();
        return str;
    }
}
