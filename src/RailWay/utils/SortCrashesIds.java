package RailWay.utils;

import java.util.Comparator;
import java.util.List;

public class SortCrashesIds implements Comparator<List<Integer>> {
    @Override
    public int compare(List<Integer> list1, List<Integer> list2) {
        return list1.get(0) - list2.get(0);
    }
}
