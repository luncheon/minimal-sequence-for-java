package minimal.sequence;

import minimal.sequence.function.BiFunction;
import minimal.sequence.function.Consumer;
import minimal.sequence.function.Function;
import minimal.sequence.function.Predicate;
import minimal.sequence.function.Supplier;

import java.util.*;

/**
 * シーケンスをラップして操作するためのユーティリティを表します。
 */
public final class Sequence<T> implements Iterable<T> {
    public static final Sequence empty = new Sequence<Object>(Collections.emptyList(), 0);
    private final Iterable<T> items;
    private Integer size;   // 要素数のキャッシュ

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
        return items == null ? empty : new Sequence<T>(items, items.size());
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @param <T>   要素の型
     * @return      シーケンス
     */
    public static <T> Sequence<T> of(Iterable<T> items) {
        return items == null ? empty : new Sequence<T>(items, null);
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @param <T>   要素の型
     * @return      シーケンス
     */
    @SafeVarargs
    public static <T> Sequence<T> of(T... items) {
        return items == null || items.length == 0 ? empty : of(Arrays.asList(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Byte> of(byte[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Short> of(short[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Integer> of(int[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Long> of(long[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Float> of(float[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Double> of(double[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Boolean> of(boolean[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。
     * @param items 要素
     * @return      シーケンス
     */
    public static Sequence<Character> of(char[] items) {
        return of(PrimitiveArrays.wrap(items));
    }

    /**
     * シーケンスを作成します。現状の実装では {@link Collections#list} を利用しているため先行評価となります。
     * @param items 要素
     * @param <T>   要素の型
     * @return      シーケンス
     */
    public static <T> Sequence<T> of(Enumeration<T> items) {
        return items == null ? empty : of(Collections.list(items));
    }

    /**
     * 2 つのシーケンスの要素が順序も含めて一致するかどうか調べます。
     * @param x シーケンス 1
     * @param y シーケンス 2
     * @return  2 つのシーケンスの要素が順序も含めて一致する場合は true, 一致しない場合は false
     */
    public static boolean equals(Iterable x, Iterable y) {
        if (x == y) {
            return true;
        }
        Iterator xi = x.iterator(), yi = y.iterator();
        while (xi.hasNext() && yi.hasNext()) {
            if (!Objects.equals(xi.next(), yi.next())) {
                return false;
            }
        }
        return !xi.hasNext() && !yi.hasNext();
    }

    /**
     * イテレーターを生成します。
     * @return イテレーター
     */
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    /**
     * 他のオブジェクトがこのオブジェクトと等しいかどうか調べます。
     * @param o 他のオブジェクト
     * @return 他のオブジェクトがこのオブジェクトと等しい場合は true, そうでない場合は false
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Sequence && equals(this, (Sequence) o);
    }

    /**
     * 全要素のハッシュコードを元にハッシュコードを算出します。
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        int hash = 1;
        for (T item : items) {
            hash = hash * 31 + Objects.hashCode(item);
        }
        return hash;
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
     * シーケンスが空の場合はサプライヤーから値を生成します。シーケンスが空でない場合は最初の要素と残りの要素を射影関数に適用します。
     * @param ifEmpty シーケンスが空の場合の結果を生成するサプライヤー
     * @param ifAny   シーケンスが空でない場合に最初の要素と残りの要素から結果を算出する射影関数
     * @param <R>     射影結果の型
     * @return        射影結果
     */
    public <R> R match(Supplier<? extends R> ifEmpty, BiFunction<? super T, ? super Sequence<T>, ? extends R> ifAny) {
        Iterator<T> iterator = items.iterator();
        return iterator.hasNext() ? ifAny.apply(iterator.next(), rest()) : ifEmpty.get();
    }

    /**
     * 各要素に要素からシーケンスへの射影関数を適用して、結果を連結したシーケンスを返します。
     * @param mapper 要素からシーケンスへの射影関数
     * @param <R>    射影結果となるシーケンスの要素の型
     * @return       射影結果の要素を連結したシーケンス
     */
    public <R> Sequence<R> flatMap(final Function<? super T, ? extends Iterable<R>> mapper) {
        return of(new Iterable<R>() {
            @Override
            public Iterator<R> iterator() {
                return new FlatMappedIterator<T, R>(items.iterator(), mapper);
            }
        });
    }

    /**
     * 条件を満たす要素のみ抽出します。
     * @param predicate 条件
     * @return          条件を満たす要素のシーケンス
     */
    public Sequence<T> filter(final Predicate<? super T> predicate) {
        return of(new Iterable<T>() {
            public Iterator<T> iterator() {
                return new FilteredIterator<T>(items.iterator(), predicate);
            }
        });
    }

    /**
     * 指定された型のインスタンスのみ抽出して、指定された型のシーケンスとして返します。
     * @param cls 型
     * @param <U> 型
     * @return    指定された型の要素を持つシーケンス
     */
    public <U> Sequence<U> ofClass(final Class<U> cls) {
        return filter(new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return cls.isInstance(t);
            }
        }).map(new Function<T, U>() {
            @Override
            public U apply(T t) {
                return cls.cast(t);
            }
        });
    }

    /**
     * 他のシーケンスとマージしたペアシーケンスを返します。
     * @param sequence マージ対象シーケンス
     * @param <U>      マージ対象シーケンスの要素の型
     * @return         ペアシーケンス
     */
    public <U> Sequence<Pair<T, U>> zip(final Iterable<? extends U> sequence) {
        return sequence == null ? empty : of(new Iterable<Pair<T, U>>() {
            @Override
            public Iterator<Pair<T, U>> iterator() {
                return new ZippedIterator<T, U>(items.iterator(), sequence.iterator());
            }
        });
    }

    /**
     * 他のシーケンスとマージしたペアシーケンスを返します。
     * @param sequence マージ対象シーケンス
     * @param <U>      マージ対象シーケンスの要素の型
     * @return         ペアシーケンス
     */
    @SafeVarargs
    public final <U> Sequence<Pair<T, U>> zip(final U... sequence) {
        return zip(sequence == null ? null : Arrays.asList(sequence));
    }

    /**
     * 先頭から条件を満たす限り要素を抽出します。条件を満たさない最初の要素以降の要素を除外します。
     * @param predicate 条件
     * @return          先頭から条件を満たしている間の要素のシーケンス
     */
    public Sequence<T> takeWhile(final Predicate<? super T> predicate) {
        return of(new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new ConditionedTakingIterator<T>(items.iterator(), predicate);
            }
        });
    }

    /**
     * 先頭から条件を満たす限り要素を除外します。条件を満たさない最初の要素以降の要素を抽出します。
     * @param predicate 条件
     * @return          先頭から条件を満たしている間の要素を除いたシーケンス
     */
    public Sequence<T> skipWhile(final Predicate<? super T> predicate) {
        return of(new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new ConditionedSkippingIterator<T>(items.iterator(), predicate);
            }
        });
    }

    /**
     * 末尾へ要素を追加します。
     * @param after 追加要素
     * @return      シーケンス
     */
    public Sequence<T> append(final Iterable<? extends T> after) {
        return after == null ? this : of(new Iterable<T>() {
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
        return append(after == null ? null : Arrays.asList(after));
    }

    /**
     * 先頭へ要素を追加します。
     * @param before 追加要素
     * @return       シーケンス
     */
    public Sequence<T> prepend(final Iterable<? extends T> before) {
        return before == null ? this : of(new Iterable<T>() {
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
        return prepend(before == null ? null : Arrays.asList(before));
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
     * 指定されたオブジェクトに一致する最初の要素のインデックスを取得します。条件を満たす要素が含まれていない場合は -1 を返します。
     * @param object オブジェクト
     * @return       オブジェクトに一致する最初の要素のインデックス
     */
    public int indexOf(T object) {
        int i = 0;
        for (T item : items) {
            if (Objects.equals(item, object)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    /**
     * 条件を満たす最初の要素のインデックスを取得します。条件を満たす要素が含まれていない場合は -1 を返します。
     * @param predicate 条件
     * @return          条件を満たす最初の要素のインデックス
     */
    public int indexOf(Predicate<? super T> predicate) {
        int i = 0;
        for (T item : items) {
            if (predicate.test(item)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    // YAGNI
    // int indexesOfAny(T... objects);
    // int indexesOfAny(Predicate<? super T>... predicate);
    // Sequence<Integer> indexesOf(Predicate<? super T> predicate);

    /**
     * 指定されたオブジェクトが含まれているかどうかを調べます。
     * @param object オブジェクト
     * @return       指定されたオブジェクトが含まれている場合は true, そうでない場合は false
     */
    public boolean contains(T object) {
        return indexOf(object) != -1;
    }

    // YAGNI
    // boolean containsAny(T... objects);
    // boolean containsAll(T... objects);

    /**
     * 条件を満たす要素が含まれているかどうかを調べます。
     * @param predicate 条件
     * @return          条件を満たす要素が含まれている場合は true, そうでない場合は false
     */
    public boolean any(Predicate<? super T> predicate) {
        return indexOf(predicate) != -1;
    }

    /**
     * すべての要素が条件を満たしているかどうかを調べます。
     * @param predicate 条件
     * @return          すべての要素が条件を満たしている場合は true, そうでない場合は false
     */
    public boolean all(Predicate<? super T> predicate) {
        for (T item : items) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比較値への射影関数を適用して、比較値の昇順に要素を並べ替えます。
     * @param comparableSelector 比較値への射影関数
     * @param <C>                比較値の型
     * @return                   比較値の昇順に並べ替えたシーケンス
     */
    public <C extends Comparable<C>> Sequence<T> sortBy(Function<? super T, ? extends C> comparableSelector) {
        // 射影関数の適用回数を各要素に 1 回だけとするため、以下のように実装する
        //   1. 射影関数を各要素に適用してペア (要素, 比較値) のリストを構築する
        //   2. 比較値でソートする
        //   3. ペアの 1 番目の要素 (元々の要素) を抽出するシーケンスを返す
        return Sequence.of(sortBySecondOfPair(zip(map(comparableSelector)).toArrayList())).map(Pair.<T, C>firstSelector());
    }

    /**
     * ペアのシーケンスを 2 番目の要素を比較値として昇順に並べ替えます。
     * @param pairs ペアシーケンス
     * @param <T>   1 番目の要素の型
     * @param <C>   2 番目の要素の型
     * @return      2 番目の要素の昇順に並べ替えたペアシーケンス
     */
    private static <T, C extends Comparable<C>> Iterable<Pair<T, C>> sortBySecondOfPair(Iterable<Pair<T, C>> pairs) {
        Iterator<Pair<T, C>> iterator = pairs.iterator();
        if (!iterator.hasNext()) {
            return pairs;
        }
        ArrayList<Pair<T, C>> lesser = new ArrayList<Pair<T, C>>();
        ArrayList<Pair<T, C>> greater = new ArrayList<Pair<T, C>>();
        Pair<T, C> pivot = iterator.next();
        while (iterator.hasNext()) {
            Pair<T, C> current = iterator.next();
            (current.second().compareTo(pivot.second()) < 0 ? lesser : greater).add(current);
        }
        return Sequence.of(pivot).prepend(sortBySecondOfPair(lesser)).append(sortBySecondOfPair(greater));
    }

    /**
     * 比較値への射影関数を適用して、比較値が最小の要素を返します。
     * @param comparableSelector 比較値への射影関数
     * @param <C>                比較値の型
     * @return                   最小要素
     */
    public <C extends Comparable<C>> Maybe<T> minBy(Function<? super T, ? extends C> comparableSelector) {
        return minOrMaxBy(comparableSelector, true);
    }

    /**
     * 比較値への射影関数を適用して、比較値が最大の要素を返します。
     * @param comparableSelector 比較値への射影関数
     * @param <C>                比較値の型
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
    public <K> LinkedHashMap<K, Sequence<T>> groupBy(Function<? super T, ? extends K> keySelector) {
        LinkedHashMap<K, Sequence<T>> result = new LinkedHashMap<K, Sequence<T>>();
        HashMap<K, List<T>> lists = new HashMap<K, List<T>>();
        for (T item : items) {
            K key = keySelector.apply(item);
            List<T> list = lists.get(key);
            if (list == null) {
                list = new ArrayList<T>();
                lists.put(key, list);
                result.put(key, Sequence.of(list));
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
