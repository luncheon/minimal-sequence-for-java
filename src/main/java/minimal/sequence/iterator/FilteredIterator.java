package minimal.sequence.iterator;

import minimal.sequence.function.Predicate;

import java.util.Iterator;

/**
 * 条件を満たす要素のみ通すイテレーターを表します。
 */
public final class FilteredIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final Predicate<? super T> predicate;
    private boolean hasNext = true;
    private T next;

    public FilteredIterator(Iterator<T> source, Predicate<? super T> predicate) {
        this.source = source;
        this.predicate = predicate;
        seek();
    }

    public boolean hasNext() {
        return hasNext;
    }

    public T next() {
        T current = next;
        seek();
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void seek() {
        while (source.hasNext()) {
            next = source.next();
            if (predicate.test(next)) {
                return;
            }
        }
        next = null;
        hasNext = false;
    }
}
