package minimal.sequence;

import minimal.sequence.function.Consumer;
import minimal.sequence.function.Function;
import minimal.sequence.function.Predicate;
import minimal.sequence.function.Supplier;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * 値が存在しない可能性のあるコンテナを表します。
 */
public final class Maybe<T> implements Iterable<T> {
    private final T object;

    private Maybe(T object) {
        this.object = object;
    }

    /**
     * 値が存在しない唯一の Maybe コンテナを表します。
     */
    public static final Maybe nothing = new Maybe<Object>(null);

    /**
     * 値が存在しない Maybe コンテナを取得します。
     * @param <T> Maybe コンテナが保持するオブジェクトの型
     * @return    値が存在しない Maybe コンテナ
     */
    @SuppressWarnings("unchecked")
    public static <T> Maybe<T> nothing() {
        return (Maybe<T>) nothing;
    }

    /**
     * オブジェクトから Maybe コンテナを取得します。
     * @param object オブジェクト
     * @param <T>    オブジェクトの型
     * @return       オブジェクトが null の場合は nothing, それ以外の場合は当該オブジェクトを持つ Maybe コンテナ
     */
    public static <T> Maybe<T> of(T object) {
        return object == null ? Maybe.<T>nothing() : new Maybe<T>(object);
    }

    /**
     * シーケンスから最初の非 null 要素を持つ Maybe コンテナを取得します。
     * @param objects シーケンス
     * @param <T>     Maybe コンテナが保持するオブジェクトの型
     * @return        Maybe コンテナ
     */
    public static <T> Maybe<T> anyOf(Iterable<? extends T> objects) {
        for (T object : objects) {
            if (object != null) {
                return new Maybe<T>(object);
            }
        }
        return nothing();
    }

    /**
     * 配列から最初の非 null 要素を持つ Maybe コンテナを取得します。
     * @param objects シーケンス
     * @param <T>     Maybe コンテナが保持するオブジェクトの型
     * @return        Maybe コンテナ
     */
    public static <T> Maybe<T> anyOf(T... objects) {
        return Maybe.anyOf(Arrays.asList(objects));
    }

    /**
     * サプライヤーのシーケンスから最初の非 null 生成値を持つ Maybe コンテナを取得します。
     * @param suppliers サプライヤーのシーケンス
     * @param <T>       Maybe コンテナが保持するオブジェクトの型
     * @return          Maybe コンテナ
     */
    public static <T> Maybe<T> anyOfGet(Iterable<Supplier<? extends T>> suppliers) {
        for (Supplier<? extends T> supplier : suppliers) {
            T object = supplier.get();
            if (object != null) {
                return new Maybe<T>(object);
            }
        }
        return nothing();
    }

    /**
     * サプライヤーの配列から最初の非 null 生成値を持つ Maybe コンテナを取得します。
     * @param suppliers サプライヤーの配列
     * @param <T>       Maybe コンテナが保持するオブジェクトの型
     * @return          Maybe コンテナ
     */
    public static <T> Maybe<T> anyOfGet(Supplier<? extends T>... suppliers) {
        return anyOfGet(Arrays.asList(suppliers));
    }

    /**
     * イテレーターを生成します。
     * @return イテレーター
     */
    public Iterator<T> iterator() {
        return this == nothing ? Collections.<T>emptyIterator() : Collections.singletonList(object).iterator();
    }

    /**
     * 値が存在する場合はこのインスタンスを返します。値が存在しない場合は引数の Maybe コンテナを返します。
     * @param ifNothing 値が存在しない場合に返す Maybe コンテナ
     * @return          Maybe コンテナ
     */
    public Maybe<T> or(Maybe<T> ifNothing) {
        return this != nothing ? this : ifNothing;
    }

    /**
     * 値が存在する場合はこのインスタンスを返します。値が存在しない場合はサプライヤーから Maybe コンテナを生成して返します。
     * @param ifNothing 値が存在しない場合に返す Maybe コンテナ
     * @return          Maybe コンテナ
     */
    public Maybe<T> or(Supplier<Maybe<T>> ifNothing) {
        return this != nothing ? this : ifNothing.get();
    }

    /**
     * 値が存在する場合はこのインスタンスを返します。値が存在しない場合は引数値を Maybe コンテナとして返します。
     * @param ifNothing 値が存在しない場合に返す Maybe コンテナ
     * @return          Maybe コンテナ
     */
    public Maybe<T> orMaybe(T ifNothing) {
        return this != nothing ? this : of(ifNothing);
    }

    /**
     * 値が存在する場合はこのインスタンスを返します。値が存在しない場合はサプライヤーから値を生成して Maybe コンテナとして返します。
     * @param ifNothing 値が存在しない場合に返す Maybe コンテナ
     * @return          Maybe コンテナ
     */
    public Maybe<T> orMaybe(Supplier<? extends T> ifNothing) {
        return this != nothing ? this : of(ifNothing.get());
    }

    /**
     * 値が存在する場合のみ操作を実行します。
     * @param action 操作
     */
    public void each(Consumer<? super T> action) {
        if (this != nothing) {
            action.accept(object);
        }
    }

    /**
     * 値が存在する場合は値を引数に取る操作を実行します。値が存在しない場合は引数のない操作を実行します。
     * @param ifExistence 値が存在する場合の操作
     * @param ifNothing   値が存在しない場合の操作
     */
    public void each(Consumer<? super T> ifExistence, Runnable ifNothing) {
        if (this != nothing) {
            ifExistence.accept(object);
        } else {
            ifNothing.run();
        }
    }

    /**
     * 値が存在する場合は射影関数を適用した結果を Maybe コンテナとして返します。値が存在しない場合は nothing を返します。
     * @param mapper 射影関数
     * @param <R>    射影結果の型
     * @return       射影結果を持つ可能性のある Maybe コンテナ
     */
    public <R> Maybe<R> map(Function<? super T, ? extends R> mapper) {
        return this == nothing ? Maybe.<R>nothing() : of(mapper.apply(object));
    }

    /**
     * 値が存在して指定の条件を満たす場合はこのインスタンスをそのまま返します。それ以外の場合は nothing を返します。
     * @param predicate 条件
     * @return          条件を満たす場合は
     */
    public Maybe<T> filter(Predicate<? super T> predicate) {
        return this != nothing && predicate.test(object) ? this : Maybe.<T>nothing();
    }

    /**
     * 他のオブジェクトがこのオブジェクトと等しいかどうか調べます。
     * @param o 他のオブジェクト
     * @return 他のオブジェクトがこのオブジェクトと等しい場合は true, そうでない場合は false
     */
    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Maybe && Objects.equals(((Maybe) o).object, object));
    }

    /**
     * ハッシュコードを取得します。
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(object);
    }

    /**
     * 文字列に変換します。
     * @return 文字列
     */
    @Override
    public String toString() {
        return this == nothing ? "Maybe{Nothing}" : ("Maybe{Just " + object + '}');
    }
}
