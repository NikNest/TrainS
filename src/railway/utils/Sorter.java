package railway.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * class for sorting of lists
 * @author Nikita
 * @version 1
 */
public final class Sorter {
    /**
     * use algorithm for sorting list
     * @param list string representation of list
     * @param algorithm sort
     * @return sorted list's string representation
     */
    public static String sortList(String list, SortAlgorithm algorithm) {
        String temp[] = list.split("\n");
        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(temp));
        Collections.sort(lines, algorithm);
        String str = "";
        for (String s : lines) {
            str += s + "\n";
        }
        return str.trim();
    }
}
