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
        private Point2D p;
        private double key;
        private RectHV rect;

        public Node(int level, Point2D p) {
            this.level = level;
            this.p = p;
            this.left = null;
            this.right = null;

            if (level % 2 == 0) {
                this.key = p.x();
            }
            else {
                this.key = p.y();
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
        if (p == null) {
            throw new IllegalArgumentException("point is null!");
        }
        // Add the point to the set (if it is not already in the set)
        if (this.contains(p)) {
            return;
        }
        if (this.root == null) {
            this.root = new Node(0, p);
            this.root.rect = new RectHV(0, 0, 1, 1);
            this.size += 1;
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
                    n.left = new Node(n.level + 1, p);
                    n.left.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
                    this.size += 1;
                }
                else {
                    insert(p, n.left);
                }
            }
            else {
                if (n.right == null) {
                    n.right = new Node(n.level + 1, p);
                    n.right.rect = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
                    this.size += 1;
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
                    n.left = new Node(n.level + 1, p);
                    n.left.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
                    this.size += 1;
                }
                else {
                    insert(p, n.left);
                }
            }
            else {
                if (n.right == null) {
                    n.right = new Node(n.level + 1, p);
                    n.right.rect = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
                    this.size += 1;
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
            if (p.x() == curr.p.x() && p.y() == curr.p.y()) {
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
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }
        // All points that are inside the rectangle (or on the boundary)
        ArrayList<Point2D> list = new ArrayList<>();
        rangeSearch(root, rect, list);
        return list;
    }

    private void rangeSearch(Node n, RectHV rect, ArrayList<Point2D> list) {
        if (n == null) {
            return;
        }
        if (rect.contains(n.p)) {
            list.add(n.p);
        }
        if (n.left != null && n.left.rect.intersects(rect)) {
            rangeSearch(n.left, rect, list);
        }
        if (n.right != null && n.right.rect.intersects(rect)) {
            rangeSearch(n.right, rect, list);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("null argument");
        }
        // A nearest neighbor in the set to point p; null if the set is empty
        if (this.isEmpty()) {
            return null;
        }
        return findNear(root, p, root.p.distanceSquaredTo(p), root.p);

    }

    private Point2D findNear(Node n, Point2D p, double min, Point2D NearestPoint) {
        boolean isXLevel = n.level % 2 == 0;

        Node firstSubtree = (isXLevel && p.x() <= n.p.x()) || (!isXLevel && p.y() <= n.p.y())
                            ? n.left
                            : n.right;

        Node secondSubtree = (isXLevel && p.x() > n.p.x()) || (!isXLevel && p.y() > n.p.y())
                             ? n.left
                             : n.right;

        double rectDistance = isXLevel
                              ? Math.pow(n.p.x() - p.x(), 2) : Math.pow(n.p.y() - p.y(), 2);

        // update min
        if (p.distanceSquaredTo(n.p) < min) {
            NearestPoint = n.p;
            min = p.distanceSquaredTo(n.p);
        }
        // todo delete
        System.out.println(
                "checking point " + n.p.x() + " " + n.p.y() + " distance:" + p.distanceSquaredTo(
                        n.p));
        if (firstSubtree != null) {
            NearestPoint = findNear(firstSubtree, p, min, NearestPoint);
        }
        if (secondSubtree != null && rectDistance < min) {
            NearestPoint = findNear(secondSubtree, p, min, NearestPoint);
        }
        return NearestPoint;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.372, 0.497));
        tree.insert(new Point2D(0.564, 0.413));
        tree.insert(new Point2D(0.226, 0.577));
        tree.insert(new Point2D(0.144, 0.179));
        tree.insert(new Point2D(0.083, 0.51));
        tree.insert(new Point2D(0.32, 0.708));
        tree.insert(new Point2D(0.417, 0.362));
        tree.insert(new Point2D(0.862, 0.825));
        tree.insert(new Point2D(0.785, 0.725));
        tree.insert(new Point2D(0.499, 0.208));

        // for (Point2D p : tree.range(new RectHV(0, 0, 1, 1))) {
        //   System.out.println(p.x() + " " + p.y());
        //}
        Point2D p = tree.nearest(new Point2D(0.031, 0.619));
    }
}
