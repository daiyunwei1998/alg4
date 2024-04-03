/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private double[][] energyArray;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("null picture");
        }
        else {
            // make a copy
            this.pic = new Picture(picture);
        }
        this.energyArray = new double[width()][height()];
        // make energy array
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energyArray[i][j] = energy(i, j);
            }
        }

    }

    // current picture
    public Picture picture() {
        return this.pic;
    }

    // width of current picture
    public int width() {
        return this.pic.width();
    }

    // height of current picture
    public int height() {
        return this.pic.height();
    }

    private void validatePixel(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException("pixel out of range");
        }
    }

    private int square(int num) {
        return num * num;
    }

    private int squaredDiffInGRB(Color color1, Color color2) {
        return square(color1.getRed() - color2.getRed()) +
                square(color1.getBlue() - color2.getBlue()) +
                square(color1.getGreen() - color2.getGreen());
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validatePixel(x, y);

        // border pixels
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }

        // calculate energy
        Color colorXMinusOne = picture().get(x - 1, y);
        Color colorXPlusOne = picture().get(x + 1, y);
        Color colorYMinusOne = picture().get(x, y - 1);
        Color colorYPlusOne = picture().get(x, y + 1);
        return Math.sqrt(
                squaredDiffInGRB(colorXMinusOne, colorXPlusOne) + squaredDiffInGRB(colorYMinusOne,
                                                                                   colorYPlusOne));
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Pixel[][] edgeTo = new Pixel[width()][height()];
        double[][] distTo = new double[width()][height()];
        Queue<Pixel> q = new Queue();

        for (int i = 0; i < height(); i++) {
            edgeTo[0][i] = null;
            distTo[0][i] = 0;
            q.enqueue(new Pixel(0, i));
        }

        while (!q.isEmpty()) {
            Pixel curr = q.dequeue();
            for (Pixel aPixel : curr.adjHorizontal()) {
                if (edgeTo[aPixel.x()][aPixel.y()] != null) {
                    // already marked
                    if (distTo[curr.x()][curr.y()] + this.energyArray[aPixel.x()][aPixel.y()]
                            < distTo[aPixel.x()][aPixel.y()]) {
                        // shorter path, relax
                        distTo[aPixel.x()][aPixel.y()] = distTo[curr.x()][curr.y()]
                                + this.energyArray[aPixel.x()][aPixel.y()];
                        edgeTo[aPixel.x()][aPixel.y()] = curr;
                    }
                }
                else {
                    // untraveled
                    distTo[aPixel.x()][aPixel.y()] = distTo[curr.x()][curr.y()]
                            + this.energyArray[aPixel.x()][aPixel.y()];
                    edgeTo[aPixel.x()][aPixel.y()] = curr;
                    q.enqueue(aPixel);
                }
            }
        }
        int match = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height(); i++) {
            if (distTo[width() - 1][i] < minEnergy) {
                match = i;
                minEnergy = distTo[width() - 1][i];
            }
        }
        // System.out.println(minEnergy);  // debug only
        int[] output = new int[width()];
        output[width() - 1] = match; // matched column
        Pixel prev = edgeTo[width() - 1][match];
        for (int i = width() - 2; i >= 0; i--) {
            output[i] = prev.y();
            prev = edgeTo[i][prev.y()];
        }
        return output;
    }

    private class Pixel {
        private int x;
        private int y;

        private Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
        }

        public Queue<Pixel> adjVertical() {
            Queue<Pixel> adjacentPixels = new Queue<>();
            if (y + 1 >= height()) {
                // reach the vertical end, return empty hashset
                return adjacentPixels;
            }
            if (x - 1 > 0) {
                adjacentPixels.enqueue(new Pixel(x - 1, y + 1));
            }
            if (x + 1 < width()) {
                adjacentPixels.enqueue(new Pixel(x + 1, y + 1));
            }
            adjacentPixels.enqueue(new Pixel(x, y + 1));
            return adjacentPixels;
        }

        public Queue<Pixel> adjHorizontal() {
            Queue<Pixel> adjacentPixels = new Queue<>();
            if (x + 1 >= width()) {
                // reach the vertical end, return empty hashset
                return adjacentPixels;
            }
            if (y - 1 > 0) {
                adjacentPixels.enqueue(new Pixel(x + 1, y - 1));
            }
            if (y + 1 < height()) {
                adjacentPixels.enqueue(new Pixel(x + 1, y + 1));
            }
            adjacentPixels.enqueue(new Pixel(x + 1, y));
            return adjacentPixels;
        }
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        Pixel[][] edgeTo = new Pixel[width()][height()];
        double[][] distTo = new double[width()][height()];
        Queue<Pixel> q = new Queue();

        for (int i = 0; i < width(); i++) {
            edgeTo[i][0] = null;
            distTo[i][0] = 0;
            q.enqueue(new Pixel(i, 0));
        }

        while (!q.isEmpty()) {
            Pixel curr = q.dequeue();
            for (Pixel aPixel : curr.adjVertical()) {
                if (edgeTo[aPixel.x()][aPixel.y()] != null) {
                    // already marked
                    if (distTo[curr.x()][curr.y()] + this.energyArray[aPixel.x()][aPixel.y()]
                            < distTo[aPixel.x()][aPixel.y()]) {
                        // shorter path, relax
                        distTo[aPixel.x()][aPixel.y()] = distTo[curr.x()][curr.y()]
                                + this.energyArray[aPixel.x()][aPixel.y()];
                        edgeTo[aPixel.x()][aPixel.y()] = curr;
                    }
                }
                else {
                    // untraveled
                    distTo[aPixel.x()][aPixel.y()] = distTo[curr.x()][curr.y()]
                            + this.energyArray[aPixel.x()][aPixel.y()];
                    edgeTo[aPixel.x()][aPixel.y()] = curr;
                    q.enqueue(aPixel);
                }
            }
        }
        int match = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < minEnergy) {
                match = i;
                minEnergy = distTo[i][height() - 1];
            }
        }
        // System.out.println(minEnergy);  // debug only
        int[] output = new int[height()];
        output[height() - 1] = match; // matched column
        Pixel prev = edgeTo[match][height() - 1];
        for (int i = height() - 2; i >= 0; i--) {
            output[i] = prev.x();
            prev = edgeTo[prev.x()][i];
        }
        return output;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
