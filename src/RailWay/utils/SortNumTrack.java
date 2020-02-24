package RailWay.utils;

public final class SortNumTrack extends SortAlgorithm {
    @Override
    public int compare(String s, String t1) {
        String[] temps = s.split(" ");
        int id1 = Integer.parseInt(temps[1]);
        String[] tempt = t1.split(" ");
        int id2 = Integer.parseInt(tempt[1]);
        return  id1 - id2;
    }
}
