package minimal.sequence.iterator;

import minimal.sequence.function.Predicate;

import java.util.Iterator;

/**
 * 先頭から条件を満たす間のみ要素を抽出するイテレーターを表します。
 */
public final class ConditionedTakingIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final Predicate<? super T> predicate;
    private boolean hasNext = true;
    private T next;

    public ConditionedTakingIterator(Iterator<T> source, Predicate<? super T> predicate) {
        this.source = source;
        this.predicate = predicate;
        seek();
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public T next() {
        T current = next;
        seek();
        return current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void seek() {
        if (source.hasNext()) {
            next = source.next();
            if (predicate.test(next)) {
                return;
            }
        }
        next = null;
        hasNext = false;
    }
}
