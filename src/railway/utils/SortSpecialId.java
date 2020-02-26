package railway.utils;

/**
 * sort trainparts with class and name
 * @author Nikita
 * @version 1
 */
public final class SortSpecialId extends SortAlgorithm {

    /**
     * compare algorithm
     * @param s first str
     * @param t1 second str
     * @return result of comparison
     */
    @Override
    public int compare(String s, String t1) {
        String[] temps = s.split(" ");
        String idtoCompareS = temps[2] + temps[3];
        String[] tempt = t1.split(" ");
        String idtoCompareT = tempt[2] + tempt[3];
        return idtoCompareS.compareTo(idtoCompareT);
    }
}
