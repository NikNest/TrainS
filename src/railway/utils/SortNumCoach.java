package railway.utils;

/**
 * sort coaches
 * @author Nikita
 * @version 1
 */
public final class SortNumCoach extends SortAlgorithm {
    /**
     * compare algorithm
     * @param s first str
     * @param t1 second str
     * @return result of comparison
     */
    @Override
    public int compare(String s, String t1) {
        String[] temps = s.split(" ");
        int id1 = Integer.parseInt(temps[0]);
        String[] tempt = t1.split(" ");
        int id2 = Integer.parseInt(tempt[0]);
        return  id1 - id2;
    }
}
