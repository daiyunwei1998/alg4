import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class BruteCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();

    public BruteCollinearPoints(Point[] points)  {
        if (points == null) {
            throw new IllegalArgumentException("The input points array is null.");
        }
        if (Arrays.asList(points).contains(null)) {
            throw new IllegalArgumentException("contain null in array");
        }

        // finds all line segments containing 4 points
        Arrays.sort(points);
        Point pLast = null;
        for (Point p:points) {
            if (pLast == null) {
                pLast = p;
                continue;
            }
            if (p.compareTo(pLast) == 0) {
                throw new IllegalArgumentException("duplicated point exists");
            }
            pLast = p;
        }

        for (Point p1: points) {
            Point[] recordedPoints = new Point[4];
            recordedPoints[0] = p1;
            for (Point p2:points) {
                if (p2.compareTo(p1) == 0) {
                    continue;
                }
                double slope = p1.slopeTo(p2);
                recordedPoints[1] = p2;
                int count = 2;

                for (Point pt:points) {
                    if (pt.compareTo(p1) == 0 || pt.compareTo(p2) == 0 ) {
                        continue;
                    }
                    if (recordedPoints[2] != null && pt.compareTo(recordedPoints[2])== 0) {
                        continue;
                    }
                    if (p1.slopeTo(pt) == slope) {
                        recordedPoints[count++] = pt;
                    }
                    if (count == 4) {
                        break;
                    }
                }

            }
            if (recordedPoints[3] != null) {
                LineSegment line = new LineSegment(recordedPoints[0], recordedPoints[3]);
                lines.add(line);
            }
        }
    }


    public int numberOfSegments() {
        // the number of line segments
        return lines.size();
    }
    public LineSegment[] segments() {
        // the line segments
        return lines.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
    }
}
