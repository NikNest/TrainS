package RailWay.utils;

import java.util.Comparator;

public class SortSpecialId implements Comparator<String> {
    @Override
    public int compare(String s, String t1) {
        String[] temps = s.split(" ");
        String idtoCompareS = temps[2] + temps[3];
        String[] tempt = t1.split(" ");
        String idtoCompareT = tempt[2] + tempt[3];
//        System.out.println("in compare method: " + idtoCompareS + " " + idtoCompareT + " " + idtoCompareS.compareTo(idtoCompareT));
        return /*(-1) **/ idtoCompareS.compareTo(idtoCompareT);
    }
}
