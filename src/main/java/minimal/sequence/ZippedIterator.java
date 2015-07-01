package minimal.sequence;

import java.util.Iterator;

/**
 * 2 つのイテレーターをマージします。
 */
final class ZippedIterator<F, S> implements Iterator<Pair<F, S>> {
    private final Iterator<? extends F> first;
    private final Iterator<? extends S> second;

    ZippedIterator(Iterator<? extends F> first, Iterator<? extends S> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean hasNext() {
        return first.hasNext() && second.hasNext();
    }

    @Override
    public Pair<F, S> next() {
        return Pair.of(first.next(), second.next());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
