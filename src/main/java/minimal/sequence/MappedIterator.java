package minimal.sequence;

import minimal.sequence.function.Function;

import java.util.Iterator;

/**
 * 各要素を射影関数で変換するイテレーターを表します。
 */
final class MappedIterator<T, R> implements Iterator<R> {
    private final Iterator<T> source;
    private final Function<? super T, ? extends R> mapper;

    public MappedIterator(Iterator<T> source, Function<? super T, ? extends R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public R next() {
        return mapper.apply(source.next());
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
