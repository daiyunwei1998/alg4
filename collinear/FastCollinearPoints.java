import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class FastCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("The input points array is null.");
        }
        // finds all line segments containing 4 or more points

        //for (Point p:order) {
            //System.out.println(p);
        //}
        //System.out.println("=====");

        Point pLast = null;
        for (Point p:points) {
            if (pLast == null) {
                pLast = p;
                continue;
            }
            if (p == null) {
                throw new IllegalArgumentException("null point exists");
            }
            if (p.compareTo(pLast) == 0) {
                throw new IllegalArgumentException("duplicated point exists");
            }
        }
        Point[] copy = points.clone();
        for (Point p: points) {
          //System.out.println("_____");
            //System.out.println(p);
            checkSinglePoint(copy, p);
        }
    }

    private void checkSinglePoint(Point[] points, Point p) {

        Arrays.sort(points, p.slopeOrder());
        int count = 0;
        double slope = Double.NEGATIVE_INFINITY;
        ArrayList<Point> recordedPoints = new ArrayList<>();

       // for (Point pt: points){
         //   System.out.print(pt);
        //}
        //System.out.println();

        for (Point pt: points) {
            if (recordedPoints.size() == 0) {
                slope = p.slopeTo(pt);
                recordedPoints.add(pt);
            } else {
                if (p.slopeTo(pt) == slope && p.compareTo(pt) != 0) {
                    recordedPoints.add(pt);
                } else if (p.slopeTo(pt) != slope){
                    if (recordedPoints.size() >= 3) {
                        Collections.sort(recordedPoints);
                        if (p.compareTo(recordedPoints.get(0)) < 0) {
                            LineSegment line = new LineSegment(p, recordedPoints.get(recordedPoints.size() - 1));
                            lines.add(line);
                        }

                    }
                    slope = p.slopeTo(pt);
                    recordedPoints = new ArrayList<>();
                    recordedPoints.add(pt);
                }
            }

        }

        if (recordedPoints.size() >= 3) {
            Collections.sort(recordedPoints);
            if (p.compareTo(recordedPoints.get(0)) < 0) {
                LineSegment line = new LineSegment(p, recordedPoints.get(recordedPoints.size() - 1));
                lines.add(line);
            }
        }

    }
    public int numberOfSegments() {
        // the number of line segments
        return lines.size();
    }
    public LineSegment[] segments(){
        // the line segments
        return lines.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("input300.txt");
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        // test
        Point a = new Point(5757, 3426);
        Point b = new Point(5757, 16647);
        Point c = new Point(5757, 20856);


    }
}