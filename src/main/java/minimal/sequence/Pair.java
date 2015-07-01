package minimal.sequence;

import java.util.Objects;

/**
 * 2 つの値を表します。
 */
public final class Pair<F, S> {
    private final F first;
    private final S second;

    /**
     * インスタンスを初期化します。
     * @param first  1 番目の要素
     * @param second 2 番目の要素
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * インスタンスを作成します。
     * @param first  1 番目の要素
     * @param second 2 番目の要素
     * @param <F>    1 番目の要素の型
     * @param <S>    2 番目の要素の型
     * @return ペア
     */
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<F, S>(first, second);
    }

    /**
     * 1 番目の要素を取得します。
     * @return 1 つめの要素
     */
    public F first() {
        return first;
    }

    /**
     * 2 番目の要素を取得します。
     * @return 2 つめの要素
     */
    public S second() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first) * 31 + Objects.hash(second);
    }

    @Override
    public String toString() {
        return "Pair{" + first + "," + second + '}';
    }
}
