package railway.utils;

import java.util.Comparator;

/**
 * class for sorting classes
 * @author Nikita
 * @version 1
 */
public abstract class SortAlgorithm implements Comparator<String> {
    /**
     * compare strings
     * @param s first str
     * @param t1 second str
     * @return 1 - larger, 0 - equals, -1 - less
     */
    @Override
    public abstract int compare(String s, String t1);
}
