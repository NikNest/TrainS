package RailWay.utils;

import static java.lang.StrictMath.abs;

public class Point {
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    int x;
    int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    int countLength(Point to) {
        if((x != to.x) && (y != to.y))
            System.out.println("distance count fails");
        return abs(x - to.x) + abs(y - to.y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

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
