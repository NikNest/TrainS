package RailWay.utils;

import java.util.Comparator;

public class SortNumCoach implements Comparator<String> {
    @Override
    public int compare(String s, String t1) {
        String[] temps = s.split(" ");
        int id1 = Integer.parseInt(temps[0]);
        String[] tempt = t1.split(" ");
        int id2 = Integer.parseInt(tempt[0]);
//        System.out.println("in compare method: " + idtoCompareS + " " + idtoCompareT + " " + idtoCompareS.compareTo(idtoCompareT));
        return  id1 - id2;
    }
}
