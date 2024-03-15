import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node sentinel;
    private int size;

    private class Node {
        private Item item;
        private Node previous;
        private Node next;

        private Node(Node previous, Item item, Node next) {
            this.previous = previous;
            this.item = item;
            this.next = next;
        }
    }

    // construct an empty deque
    public Deque() {
        this.sentinel = new Node(null, null, null);
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;

    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }

        if (this.size == 0) {
            Node newNode = new Node(this.sentinel, item, this.sentinel);
            this.sentinel.previous = newNode;
            this.sentinel.next = newNode;
        }
        else {
            Node newNode = new Node(this.sentinel, item, this.sentinel.next);
            this.sentinel.next.previous = newNode;
            this.sentinel.next = newNode;
        }

        this.size += 1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (size == 0) {
            this.sentinel.next = new Node(this.sentinel, item, sentinel);
            this.sentinel.previous = this.sentinel.next;
        }
        else {
            Node newNode = new Node(this.sentinel.previous, item, sentinel);
            this.sentinel.previous.next = newNode;
            this.sentinel.previous = newNode;
        }
        this.size += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("empty deque");
        }
        Item item = this.sentinel.next.item;
        this.sentinel.next.item = null;
        sentinel.next.next.previous = sentinel;
        this.sentinel.next = this.sentinel.next.next;
        this.size -= 1;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("empty deque");
        }
        Item item = this.sentinel.previous.item;
        this.sentinel.previous.previous.next = this.sentinel;
        this.sentinel.previous.item = null;
        this.sentinel.previous = this.sentinel.previous.previous;
        this.size -= 1;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = sentinel.next;

        public boolean hasNext() {
            return this.current != null && this.current.item != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;

        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(3);
        System.out.println(deque.removeFirst());
        deque.addLast(1);
        deque.addLast(2);
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
        System.out.println(deque.size());
        System.out.println(deque.isEmpty());
        deque.addFirst(1);
        deque.addLast(9);
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
        for (int i : deque) {
            System.out.println(i);
        }

        System.out.println("==");
        deque = new Deque<>();
        for (int i = 0; i < 10000; i++) {
            deque.addLast(i);
        }
        for (int i = 0; i < 9999; i++) {
            deque.removeFirst();
        }
        System.out.println("done");
    }

}