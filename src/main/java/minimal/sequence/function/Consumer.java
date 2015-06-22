package minimal.sequence.function;

/**
 * 1 つの引数を受け取って結果を返さない操作を表します。
 */
public interface Consumer<T> {
    void accept(T t);
}
