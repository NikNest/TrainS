package RailWay.utils;

import java.util.Comparator;

public class SortNumTrack implements Comparator<String> {
    @Override
    public int compare(String s, String t1) {
        String[] temps = s.split(" ");
        int id1 = Integer.parseInt(temps[1]);
        String[] tempt = t1.split(" ");
        int id2 = Integer.parseInt(tempt[1]);
        return  id1 - id2;
    }
}