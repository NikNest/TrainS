package railway.utils;

import static java.lang.StrictMath.abs;

/**
 * class for point (x coord, y coord)
 * @author Nikita
 * @version 1
 */
public final class Point {
    private final int x;
    private final int y;

    /**
     * point constructor
     * @param x coord
     * @param y coord
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * counts length to another point
     * @param to point
     * @return length
     */
    public int countLength(Point to) {
        if ((x != to.x) && (y != to.y))
            System.out.println("distance count fails");
        return abs(x - to.x) + abs(y - to.y);
    }

    /**
     * returns x of the point
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * returns y of the point
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * str representation of the point
     * @return (x,y)
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * checks if point are equal
     * @param obj the second point
     * @return true if are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Point o = (Point) obj;
        return (o.getX() == x) && (o.getY() == y);
    }

}
