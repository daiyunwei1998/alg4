/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private Node left;
        private Node right;
        private int level; // start with 0
        private double x;
        private double y;
        private double key;
        private RectHV rect;

        public Node(int level, double x, double y) {
            this.level = level;
            this.x = x;
            this.y = y;
            this.left = null;
            this.right = null;

            if (level % 2 == 0) {
                this.key = x;
            }
            else {
                this.key = y;
            }
        }
    }

    public KdTree() {
        // Construct an empty set of points
        this.size = 0;
        this.root = null;
    }

    public boolean isEmpty() {
        // Is the set empty?
        return this.size == 0;
    }

    public int size() {
        // Number of points in the set
        return this.size;
    }

    public void insert(Point2D p) {
        // Add the point to the set (if it is not already in the set)
        if (this.contains(p)) {
            return;
        }
        if (this.root == null) {
            this.root = new Node(0, p.x(), p.y());
            this.root.rect = new RectHV(0, 0, 1, 1);
        }
        else {
            insert(p, this.root);
        }


    }

    private void insert(Point2D p, Node n) {
        if (n.level % 2 == 0) {
            // check x
            if (p.x() <= n.key) {
                if (n.left == null) {
                    n.left = new Node(n.level + 1, p.x(), p.y());
                    n.left.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.x, n.rect.ymax());
                }
                else {
                    insert(p, n.left);
                }
            }
            else {
                if (n.right == null) {
                    n.right = new Node(n.level + 1, p.x(), p.y());
                    n.right.rect = new RectHV(n.x, n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
                }
                else {
                    insert(p, n.right);
                }
            }
        }
        else {
            // check y
            if (p.y() <= n.key) {
                if (n.left == null) {
                    n.left = new Node(n.level + 1, p.x(), p.y());
                    n.left.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.y);
                }
                else {
                    insert(p, n.left);
                }
            }
            else {
                if (n.right == null) {
                    n.right = new Node(n.level + 1, p.x(), p.y());
                    n.right.rect = new RectHV(n.rect.xmin(), n.y, n.rect.xmax(), n.rect.ymax());
                }
                else {
                    insert(p, n.right);
                }
            }
        }
    }


    public boolean contains(Point2D p) {
        // Does the set contain point p?
        Node curr = this.root;
        while (curr != null) {
            if (p.x() == curr.x && p.y() == curr.y) {
                return true;
            }
            if (curr.level % 2 == 0) {
                // check x
                if (p.x() <= curr.key) {
                    curr = curr.left;
                }
                else {
                    curr = curr.right;
                }
            }
            else {
                // check y
                if (p.y() <= curr.key) {
                    curr = curr.left;
                }
                else {
                    curr = curr.right;
                }
            }
        }
        return false;

    }

    public void draw() {
        // Draw all points to standard draw
    }

    public Iterable<Point2D> range(RectHV rect) {
        // All points that are inside the rectangle (or on the boundary)
        ArrayList<Point2D> list = new ArrayList<>();
        return list;
        // todo implement
    }

    public Point2D nearest(Point2D p) {
        // A nearest neighbor in the set to point p; null if the set is empty
        return p;
        // todo implement
    }

    public static void main(String[] args) {

    }
}
