/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> set;

    public PointSET() {
        // construct an empty set of points
        this.set = new TreeSet<>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return this.set.size() == 0;
    }

    public int size() {
        // number of points in the set
        return this.set.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        // add the point to the set (if it is not already in the set)
        this.set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null!");
        }
        // does the set contain point p?
        return this.set.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : this.set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        // all points that are inside the rectangle (or on the boundary)
        TreeSet<Point2D> result = new TreeSet<>();
        for (Point2D p : this.set) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        // a nearest neighbor in the set to point p; null if the set is empty
        double minDistance = -1;
        Point2D minPoint = null;
        for (Point2D point : this.set) {
            double distance = point.distanceSquaredTo(p);
            if (minDistance == -1) {
                minDistance = distance;
                minPoint = point;
            }
            else if (distance < minDistance) {
                minDistance = distance;
                minPoint = point;
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {

    }
}
