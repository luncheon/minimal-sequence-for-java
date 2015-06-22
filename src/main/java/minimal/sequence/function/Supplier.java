package minimal.sequence.function;

/**
 * 引数を取らずに結果を生成する関数を表します。
 */
public interface Supplier<T> {
    T get();
}
