package RailWay.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class Sorter {
    public static final String sortList(String list, SortAlgorithm algorithm) {
        String temp[] = list.split("\n");
        ArrayList<String> lines = new ArrayList(Arrays.asList(temp));
        Collections.sort(lines, algorithm);
        String str = "";
        for(String s : lines) {
            str += s + "\n";
        }
        return str.trim();
    }
}
