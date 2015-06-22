package minimal.sequence.function;

/**
 * 1 つの引数を受け取って結果を生成する関数を表します。
 */
public interface Function<T, R> {
    R apply(T t);
}
