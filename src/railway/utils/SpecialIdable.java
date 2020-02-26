package railway.utils;

/**
 * inteface for train parts with name and class as id
 * @author Nikita
 * @version 1
 */
public interface SpecialIdable {
    /**
     * getter class
     * @return class of train part
     */
    String getSpecialClass();

    /**
     * getter name
     * @return train part name
     */
    String getSpecialName();

    /**
     * equals for id of such train parts
     * @param trainpart1 first trainpart
     * @param trainpart2 second trainpart
     * @return true if are equal
     */
    static boolean haveSameSpecialId(SpecialIdable trainpart1, SpecialIdable trainpart2) {
        return trainpart1.getSpecialClass().equals(trainpart2.getSpecialClass())
                && trainpart1.getSpecialName().equals(trainpart2.getSpecialName());
    }
}
