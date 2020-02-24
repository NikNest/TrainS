package RailWay.utils;

import static java.lang.StrictMath.abs;

public final class Point {
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    private final int x;
    private final int y;
    public final int countLength(Point to) {
        if((x != to.x) && (y != to.y))
            System.out.println("distance count fails");
        return abs(x - to.x) + abs(y - to.y);
    }
    public final int getX() {
        return x;
    }
    public final int getY() {
        return y;
    }
    @Override
    public final String toString() {
        return "(" + x + "," + y + ")";
    }
    @Override
    public final boolean equals(Object obj) {
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
