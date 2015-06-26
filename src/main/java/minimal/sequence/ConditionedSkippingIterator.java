package minimal.sequence;

import minimal.sequence.function.Predicate;

import java.util.Iterator;

/**
 * 先頭から条件を満たす間の要素を除外して、残りの要素を抽出するイテレーターを表します。
 */
final class ConditionedSkippingIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private boolean isFirst = false;
    private T first = null;

    public ConditionedSkippingIterator(Iterator<T> source, Predicate<? super T> predicate) {
        this.source = source;
        while (source.hasNext()) {
            T first = source.next();
            if (!predicate.test(first)) {
                this.first = first;
                this.isFirst = true;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return isFirst || source.hasNext();
    }

    @Override
    public T next() {
        if (isFirst) {
            isFirst = false;
            return first;
        } else {
            return source.next();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
