package minimal.sequence.function;

/**
 * 1 つの引数を受け取って真偽値を返す関数を表します。
 */
public interface Predicate<T> {
    boolean test(T t);
}
