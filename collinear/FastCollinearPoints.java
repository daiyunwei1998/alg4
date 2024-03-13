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
        for (Point point:points) {
            if (point == null) {
                throw new IllegalArgumentException("contain null in array");
            }
        }

        Point[] copy = points.clone();
        Arrays.sort(copy);
        // finds all line segments containing 4 or more points

        //for (Point p:copy) {
        //    System.out.println(p);
        //}
        //System.out.println("=====");

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

        for (Point p: copy) {
          //System.out.println("_____");
            //System.out.println(p);
            checkSinglePoint(copy, p);
        }
    }

    private void checkSinglePoint(Point[] points, Point p) {
        points = points.clone();
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


        In in = new In("vertical5.txt");
        int n = in.readInt();
        Point[] points = new Point[1];
        points[0] = new Point(1,2);



        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);

    }
}