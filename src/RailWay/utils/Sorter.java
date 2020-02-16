package RailWay.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class Sorter {
    public static String sortList(String list, SortAlgorithm algorithm) {
        String temp[] = list.split("\n");
        ArrayList<String> lines = new ArrayList(Arrays.asList(temp));
        Collections.sort(lines, algorithm);
        String str = "";
        Iterator<String> iter = lines.iterator();
        while(iter.hasNext()) {
            str += iter.next();
            if(iter.hasNext())
                str += "\n";
        }
        return str;
    }
}
