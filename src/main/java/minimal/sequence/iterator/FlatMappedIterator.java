package minimal.sequence.iterator;

import minimal.sequence.function.Function;

import java.util.Collections;
import java.util.Iterator;

/**
 * 各要素を射影関数でシーケンスに変換して連結するイテレーターを表します。
 */
public final class FlatMappedIterator<T, R> implements Iterator<R> {
    private final Iterator<? extends T> source;
    private final Function<? super T, ? extends Iterable<R>> mapper;
    private Iterator<? extends R> currentIterator = Collections.emptyIterator();

    public FlatMappedIterator(Iterator<? extends T> source, Function<? super T, ? extends Iterable<R>> mapper) {
        this.source = source;
        this.mapper = mapper;
        seek();
    }

    @Override
    public boolean hasNext() {
        return currentIterator.hasNext();
    }

    @Override
    public R next() {
        R current = currentIterator.next();
        seek();
        return current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void seek() {
        while (!currentIterator.hasNext() && source.hasNext()) {
            currentIterator = mapper.apply(source.next()).iterator();
        }
    }
}
