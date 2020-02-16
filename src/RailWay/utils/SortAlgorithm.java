package RailWay.utils;

import java.util.Comparator;

public abstract class SortAlgorithm implements Comparator<String> {
    @Override
    public abstract int compare(String s, String t1);
}
