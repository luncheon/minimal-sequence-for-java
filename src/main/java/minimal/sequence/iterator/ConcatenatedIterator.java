package minimal.sequence.iterator;

import java.util.Iterator;

/**
 * 2 つのイテレーターを連結するイテレーターを表します。
 */
public final class ConcatenatedIterator<T> implements Iterator<T> {
    private final Iterator<? extends T> source1;
    private final Iterator<? extends T> source2;

    public ConcatenatedIterator(Iterator<? extends T> source1, Iterator<? extends T> source2) {
        this.source1 = source1;
        this.source2 = source2;
    }

    public boolean hasNext() {
        return source1.hasNext() || source2.hasNext();
    }

    public T next() {
        return source1.hasNext() ? source1.next() : source2.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
