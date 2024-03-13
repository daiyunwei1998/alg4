import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class BruteCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();

    public BruteCollinearPoints(Point[] points)  {
        if (points == null) {
            throw new IllegalArgumentException("The input points array is null.");
        }
        Point[] copy = points.clone();

        if (Arrays.asList(copy).contains(null)) {
            throw new IllegalArgumentException("contain null in array");
        }

        // finds all line segments containing 4 points
        Arrays.sort(copy);
        Point pLast = null;
        for (Point p:copy) {
            if (pLast == null) {
                pLast = p;
                continue;
            }
            if (p.compareTo(pLast) == 0) {
                throw new IllegalArgumentException("duplicated point exists");
            }
            pLast = p;
        }

        for (Point p1: copy) {


            for (Point p2:copy) {
                ArrayList<Point> recordedPoints = new ArrayList<>();

                if (p2.compareTo(p1) <= 0) {
                    continue;
                }
                double slope = p1.slopeTo(p2);


                for (Point pt:copy) {
                    if (recordedPoints.contains(pt)) {
                        continue;
                    }
                    if (p1.compareTo(pt) == 0 || p2.compareTo(pt) == 0) {
                        continue;
                    }

                    if (p1.slopeTo(pt) == slope) {
                        recordedPoints.add(pt);
                    }
                }
                if (recordedPoints.size() >= 2) {
                    Collections.sort(recordedPoints);



                    if (p1.compareTo(recordedPoints.get(0)) < 0 && p2.compareTo(recordedPoints.get(0)) <0) {
                        LineSegment line = new LineSegment(p1, recordedPoints.get(recordedPoints.size() - 1));
                        lines.add(line);

                    }

            }


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

        // read the n points from a file
        In in = new In("input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(null);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
