package minimal.sequence;

import minimal.sequence.function.Consumer;
import minimal.sequence.function.Function;
import minimal.sequence.function.Predicate;
import minimal.sequence.iterator.ConcatenatedIterator;
import minimal.sequence.iterator.FilteredIterator;
import minimal.sequence.iterator.MappedIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * シーケンスをラップして操作するためのユーティリティを表します。
 */
public final class Sequence<T> implements Iterable<T> {
    private final Iterable<T> items;
    private Integer size;   // 要素数のキャッシュ

    private Sequence(Iterable<T> items) {
        this(items, null);
    }

    private Sequence(Iterable<T> items, Integer size) {
        this.items = items;
        this.size = size;
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @param <T>   要素の型
     * @return      シーケンス
     */
    public static <T> Sequence<T> of(Collection<T> items) {
        return new Sequence<T>(items, items.size());
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @param <T>   要素の型
     * @return      シーケンス
     */
    public static <T> Sequence<T> of(Iterable<T> items) {
        return new Sequence<T>(items);
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @param <T>   要素の型
     * @return      シーケンス
     */
    @SafeVarargs
    public static <T> Sequence<T> of(T... items) {
        return of(Arrays.asList(items));
    }

    /**
     * イテレーターを生成します。
     * @return イテレーター
     */
    public Iterator<T> iterator() {
        return items.iterator();
    }

    /**
     * シーケンスが空かどうか調べます。
     * @return シーケンスが空の場合は true, そうでない場合は false
     */
    public boolean isEmpty() {
        return !items.iterator().hasNext();
    }

    /**
     * 要素数を取得します。
     * @return 要素数
     */
    public int size() {
        if (size == null) {
            int i = 0;
            for (T ignored : items) {
                i++;
            }
            size = i;
        }
        return size;
    }

    /**
     * 各要素に対してアクションを実行します。
     * @param action アクション
     * @return       このインスタンス
     */
    public Sequence<T> each(Consumer<? super T> action) {
        for (T item : items) {
            action.accept(item);
        }
        return this;
    }

    /**
     * 各要素に射影関数を適用します。
     * @param mapper 射影関数
     * @param <R>    射影結果の型
     * @return       射影結果のシーケンス
     */
    public <R> Sequence<R> map(final Function<? super T, ? extends R> mapper) {
        return new Sequence<R>(new Iterable<R>() {
            public Iterator<R> iterator() {
                return new MappedIterator<T, R>(items.iterator(), mapper);
            }
        }, size);
    }

    /**
     * 指定された条件を満たす要素のみ抽出します。
     * @param predicate 条件
     * @return          条件を満たす要素のシーケンス
     */
    public Sequence<T> filter(final Predicate<? super T> predicate) {
        return new Sequence<T>(new Iterable<T>() {
            public Iterator<T> iterator() {
                return new FilteredIterator<T>(items.iterator(), predicate);
            }
        });
    }

    /**
     * 末尾へ要素を追加します。
     * @param after 追加要素
     * @return      シーケンス
     */
    public Sequence<T> append(final Iterable<? extends T> after) {
        return new Sequence<T>(new Iterable<T>() {
            public Iterator<T> iterator() {
                return new ConcatenatedIterator<T>(items.iterator(), after.iterator());
            }
        });
    }

    /**
     * 末尾へ要素を追加します。
     * @param after 追加要素
     * @return      シーケンス
     */
    @SafeVarargs
    public final Sequence<T> append(T... after) {
        return append(Arrays.asList(after));
    }

    /**
     * 先頭へ要素を追加します。
     * @param before 追加要素
     * @return       シーケンス
     */
    public Sequence<T> prepend(final Iterable<? extends T> before) {
        return new Sequence<T>(new Iterable<T>() {
            public Iterator<T> iterator() {
                return new ConcatenatedIterator<T>(before.iterator(), items.iterator());
            }
        });
    }

    /**
     * 先頭へ要素を追加します。
     * @param before 追加要素
     * @return       シーケンス
     */
    @SafeVarargs
    public final Sequence<T> prepend(T... before) {
        return prepend(Arrays.asList(before));
    }

    /**
     * 最初の要素を返します。要素がない場合は nothing を返します。
     * @return 最初の要素
     */
    public Maybe<T> first() {
        Iterator<T> iterator = items.iterator();
        return Maybe.of(iterator.hasNext() ? iterator.next() : null);
    }

    /**
     * 最初の要素を除いた残りを返します。
     * @return 最初の要素を除いた残りの要素のシーケンス
     */
    public Sequence<T> rest() {
        return new Sequence<T>(new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                Iterator<T> iterator = items.iterator();
                if (iterator.hasNext()) {
                    iterator.next();
                }
                return iterator;
            }
        }, size == null ? null : size - 1);
    }

    /**
     * 単一の要素を持つ場合はその要素を返します。そうでない場合は nothing を返します。
     * @return 単一の要素
     */
    public Maybe<T> single() {
        return Maybe.of(size() == 1 ? items.iterator().next() : null);
    }

    /**
     * 比較可能な値への射影関数を適用して、結果が最小の要素を返します。
     * @param comparableSelector 比較可能な値への射影関数
     * @param <C>                比較可能な値の型
     * @return                   最小要素
     */
    public <C extends Comparable<C>> Maybe<T> minBy(Function<? super T, ? extends C> comparableSelector) {
        return minOrMaxBy(comparableSelector, true);
    }

    /**
     * 比較可能な値への射影関数を適用して、結果が最大の要素を返します。
     * @param comparableSelector 比較可能な値への射影関数
     * @param <C>                比較可能な値の型
     * @return                   最大要素
     */
    public <C extends Comparable<C>> Maybe<T> maxBy(Function<? super T, ? extends C> comparableSelector) {
        return minOrMaxBy(comparableSelector, false);
    }

    /**
     * 各要素を区切り文字で区切って連結した文字列を生成します。
     * @param delimiter 区切り文字
     * @return          連結文字列
     */
    public String joinToString(String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (T item : first()) {
            builder.append(item);
        }
        for (T item : rest()) {
            builder.append(delimiter).append(item);
        }
        return builder.toString();
    }

    /**
     * {@link ArrayList} を生成します。
     * @return {@link ArrayList}
     */
    public ArrayList<T> toArrayList() {
        ArrayList<T> result = new ArrayList<T>();
        for (T item : items) {
            result.add(item);
        }
        return result;
    }

    /**
     * 配列を生成します。
     * @return 配列
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[]) toArrayList().toArray();
    }

    /**
     * {@link HashMap} を生成します。
     * @param keySelector   要素からキーを生成する射影関数
     * @param valueSelector 要素から値を生成する射影関数
     * @param <K>           キーの型
     * @param <V>           値の型
     * @return              {@link HashMap}
     */
    public <K, V> HashMap<K, V> toHashMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        HashMap<K, V> result = new HashMap<K, V>();
        for (T item : items) {
            result.put(keySelector.apply(item), valueSelector.apply(item));
        }
        return result;
    }

    /**
     * 同一のキーを持つ要素ごとにグルーピングします。
     * @param keySelector 要素からグルーピングのキーを生成する射影関数
     * @param <K>         グルーピングのキーの型
     * @return            キーとグループの連想配列
     */
    public <K> LinkedHashMap<K, List<T>> groupBy(Function<? super T, ? extends K> keySelector) {
        LinkedHashMap<K, List<T>> result = new LinkedHashMap<K, List<T>>();
        for (T item : items) {
            K key = keySelector.apply(item);
            List<T> list = result.get(key);
            if (list == null) {
                list = new ArrayList<T>();
                result.put(key, list);
            }
            list.add(item);
        }
        return result;
    }

    /**
     * 比較可能な値への射影関数を適用して、結果が最小または最大となる要素を返します。
     * @param comparableSelector 比較可能な値への射影関数
     * @param min                最小要素を取得する場合は true, 最大要素を取得する場合は false
     * @param <C>                比較可能な値の型
     * @return                   最小または最大の要素
     */
    private <C extends Comparable<C>> Maybe<T> minOrMaxBy(Function<? super T, ? extends C> comparableSelector, boolean min) {
        Iterator<T> iterator = items.iterator();
        if (!iterator.hasNext()) {
            return Maybe.nothing();
        }
        T currentObject = iterator.next();
        C currentValue = comparableSelector.apply(currentObject);
        while (iterator.hasNext()) {
            T object = iterator.next();
            C value = comparableSelector.apply(object);
            int compared = value.compareTo(currentValue);
            if (min ? compared < 0 : compared > 0) {
                currentObject = object;
                currentValue = value;
            }
        }
        return Maybe.of(currentObject);
    }
}
