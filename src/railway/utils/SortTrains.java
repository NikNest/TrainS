package railway.utils;

/**
 * sort trains algorihm
 * @author Nikita
 * @version 1
 */
public final class SortTrains extends SortAlgorithm {
    /**
     * compare algorithm
     * @param s first str
     * @param t1 second str
     * @return result of comparison
     */
    @Override
    public int compare(String s, String t1) {
        int num1 = Integer.parseInt(s.substring(0, 1));
        int num2 = Integer.parseInt(t1.substring(0, 1));
        return num1 - num2;
    }
}
