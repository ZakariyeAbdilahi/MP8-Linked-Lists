import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Circularly doubly-linked list with a dummy node to simplify handling edge cases and improve iterator safety.
 * Implements the SimpleList interface.
 * @author Samuel A. Rebelsky
 * @author Zakariye Abdilahi
 */
public class SimpleCDLL<T> implements SimpleList<T> {
    // +--------+------------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * Counter for all changes that affect the structure of the list,
     * used to ensure fail-fast behavior of iterators.
     */
    private int iterChanges = 0;

    /**
     * Dummy node that acts as the start and end of the list.
     * This node does not hold any user-accessible data but simplifies list operations.
     */
    private Node2<T> dummy;

    /**
     * Number of elements in the list, excluding the dummy node.
     */
    private int size = 0;

    // +--------------+------------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Constructs an empty SimpleCDLL.
     * Initializes the dummy node and links it to itself to create a circular structure.
     */
    public SimpleCDLL() {
        this.dummy = new Node2<>(null);
        this.dummy.next = this.dummy;
        this.dummy.prev = this.dummy;
    }

    // +-----------+---------------------------------------------------------
    // | Iterators |
    // +-----------+

    /**
     * Returns an iterator over elements of type T.
     * @return an Iterator.
     */
    public Iterator<T> iterator() {
        return listIterator();
    }

    /**
     * Returns a ListIterator over the elements in this list (in proper sequence).
     * @return a ListIterator over the elements in this list.
     */
    public ListIterator<T> listIterator() {
        return new ListIterator<T>() {
            // Current position of the iterator (0-based).
            int pos = 0;

            // Node immediately preceding the next element that would be returned by next().
            Node2<T> prev = SimpleCDLL.this.dummy;

            // Node immediately following the last element that would be returned by previous().
            Node2<T> next = SimpleCDLL.this.dummy.next;

            // The last node returned by next() or previous(). Needed for remove() and set().
            Node2<T> update = null;

            // Modification count for this iterator, used to detect concurrent modification.
            int myChanges = SimpleCDLL.this.iterChanges;

            // +----------------+------------------------------------------------
            // | Iterator Methods |
            // +----------------+

            /**
             * Returns true if the iteration has more elements.
             * @return true if the iteration has more elements.
             */
            public boolean hasNext() {
                failFast();
                return (this.pos < SimpleCDLL.this.size);
            }

            /**
             * Returns the next element in the list and advances the cursor position.
             * @return the next element in the list.
             * @throws NoSuchElementException if the iteration has no more elements.
             */
            public T next() {
                failFast();
                if (!hasNext()) throw new NoSuchElementException();
                this.update = this.next;
                this.prev = this.next;
                this.next = this.next.next;
                this.pos++;
                return this.update.value;
            }

            /**
             * Returns true if this list iterator has more elements when traversing the list in the reverse direction.
             * @return true if this list iterator has more elements when traversing the list in the reverse direction.
             */
            public boolean hasPrevious() {
                failFast();
                return (this.pos > 0);
            }

            /**
             * Returns the previous element in the list and moves the cursor position backwards.
             * @return the previous element in the list.
             * @throws NoSuchElementException if the iteration has no previous elements.
             */
            public T previous() {
                failFast();
                if (!hasPrevious()) throw new NoSuchElementException();
                this.update = this.prev;
                this.next = this.prev;
                this.prev = this.prev.prev;
                this.pos--;
                return this.next.value;
            }

            /**
             * Returns the index of the element that would be returned by a subsequent call to next().
             * @return the index of the element that would be returned by a subsequent call to next.
             */
            public int nextIndex() {
                failFast();
                return this.pos;
            }

            /**
             * Returns the index of the element that would be returned by a subsequent call to previous().
             * @return the index of the element that would be returned by a subsequent call to previous.
             */
            public int previousIndex() {
                failFast();
                return this.pos - 1;
            }

            /**
             * Removes from the list the last element that was returned by next() or previous().
             * @throws IllegalStateException if neither next nor previous have been called, or remove or add have been called after the last call to next or previous.
             */
            public void remove() {
                failFast();
                if (this.update == null) throw new IllegalStateException();
                if (this.next == this.update) this.next = this.update.next;
                if (this.prev == this.update) {
                    this.prev = this.update.prev;
                    this.pos--;
                }
                this.update.remove();
                SimpleCDLL.this.size--;
                SimpleCDLL.this.iterChanges++;
                this.myChanges++;
                this.update = null;
            }

            /**
             * Replaces the last element returned by next() or previous() with the specified element.
             * @param val the element with which to replace the last element returned by next or previous.
             * @throws IllegalStateException if neither next nor previous have been called, or remove or add have been called after the last call to next or previous.
             */
            public void set(T val) {
                failFast();
                if (update == null) throw new IllegalStateException();
                update.value = val;
                update = null;
            }

            /**
             * Inserts the specified element into the list.
             * @param val the element to insert.
             */
            public void add(T val) {
                failFast();
                this.prev = this.prev.insertAfter(val);
                this.update = null;
                SimpleCDLL.this.size++;
                SimpleCDLL.this.iterChanges++;
                this.myChanges++;
                this.pos++;
            }

            /**
             * Checks if the list has been structurally modified since the iterator was created. Throws ConcurrentModificationException if it has.
             * @throws ConcurrentModificationException if the list has been structurally modified.
             */
            private void failFast() {
                if (this.myChanges != SimpleCDLL.this.iterChanges) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }
}
