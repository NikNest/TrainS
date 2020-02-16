package RailWay.utils;

public interface SpecialIdable {
    public String getSpecialClass();
    public String getSpecialName();
    public static boolean haveSameSpecialId(SpecialIdable trainpart1, SpecialIdable trainpart2) {
        return trainpart1.getSpecialClass().equals(trainpart2.getSpecialClass())
                && trainpart1.getSpecialName().equals(trainpart2.getSpecialName());
    }
}
