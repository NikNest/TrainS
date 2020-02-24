package RailWay.utils;

public final class SortNumCoach extends SortAlgorithm {
    @Override
    public final int compare(String s, String t1) {
        String[] temps = s.split(" ");
        int id1 = Integer.parseInt(temps[0]);
        String[] tempt = t1.split(" ");
        int id2 = Integer.parseInt(tempt[0]);
        return  id1 - id2;
    }
}
