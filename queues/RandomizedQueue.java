import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    private void resize(double factor) {
        int newSize = (int) (items.length * factor);
        Item[] newItems = (Item[]) new Object[newSize];
        System.arraycopy(items, 0, newItems, 0, size);
        this.items = newItems;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.items = (Item[]) new Object[8];
        this.size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (size() >= 0.5 * this.items.length) {
            resize(2);
        }
        this.items[size()] = item;
        this.size += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("empty deque");
        }
        if (size > 0 && size <= 0.25 * items.length) {
            resize(0.5);
        }
        int index = StdRandom.uniformInt(size);
        Item item = items[index];
        items[index] = items[size - 1];
        items[size - 1] = null;
        size--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException("empty deque");
        }
        return this.items[StdRandom.uniformInt(size())];

    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedDequeIterator();
    }

    private class RandomizedDequeIterator implements Iterator<Item> {
        int current;
        private final Item[] shuffledItems;

        public RandomizedDequeIterator() {
            shuffledItems = (Item[]) new Object[size];
            System.arraycopy(items, 0, shuffledItems, 0, size);
            StdRandom.shuffle(shuffledItems);
            current = 0;
        }

        public boolean hasNext() {
            return this.current < size();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return shuffledItems[current++];

        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> r = new RandomizedQueue<>();
        r.enqueue(1);
        r.enqueue(2);
        r.enqueue(3);
        for (int i : r) {
            System.out.println(i);
        }
        System.out.println(r.size());
        System.out.println(r.dequeue());
        System.out.println(r.dequeue());
        System.out.println(r.dequeue());
        System.out.println(r.isEmpty());
    }

}