package minimal.sequence.function;

/**
 * 2 つの引数を受け取って結果を生成する関数を表します。
 */
public interface BiFunction<T, U, R> {
    R apply(T t, U u);
}
