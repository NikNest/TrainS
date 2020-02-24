package RailWay.utils;

public final class SortTrains extends SortAlgorithm {
    @Override
    public final int compare(String s, String t1) {
        int num1 = Integer.parseInt(s.substring(0, 1));
        int num2 = Integer.parseInt(t1.substring(0, 1));
        return num1 - num2;
    }
}
