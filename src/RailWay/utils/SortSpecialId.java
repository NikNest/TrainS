package RailWay.utils;

public final class SortSpecialId extends SortAlgorithm {
    @Override
    public final int compare(String s, String t1) {
        String[] temps = s.split(" ");
        String idtoCompareS = temps[2] + temps[3];
        String[] tempt = t1.split(" ");
        String idtoCompareT = tempt[2] + tempt[3];
        return idtoCompareS.compareTo(idtoCompareT);
    }
}
