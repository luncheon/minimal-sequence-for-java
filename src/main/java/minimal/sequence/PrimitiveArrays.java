package minimal.sequence;

/**
 * プリミティブ型の配列を操作するためのユーティリティを表します。
 */
final class PrimitiveArrays {
    /**
     * byte の配列を {@link Byte} の配列に変換します。
     * @param array boolean の配列
     * @return      {@link Byte} の配列
     */
    public static Byte[] wrap(byte[] array) {
        if (array == null || array.length == 0) {
            return new Byte[]{};
        }
        Byte[] wrapped = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    /**
     * short の配列を {@link Short} の配列に変換します。
     * @param array short の配列
     * @return      {@link Short} の配列
     */
    public static Short[] wrap(short[] array) {
        if (array == null || array.length == 0) {
            return new Short[]{};
        }
        Short[] wrapped = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    /**
     * int の配列を {@link Integer} の配列に変換します。
     * @param array int の配列
     * @return      {@link Integer} の配列
     */
    public static Integer[] wrap(int[] array) {
        if (array == null || array.length == 0) {
            return new Integer[]{};
        }
        Integer[] wrapped = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    /**
     * long の配列を {@link Long} の配列に変換します。
     * @param array long の配列
     * @return      {@link Long} の配列
     */
    public static Long[] wrap(long[] array) {
        if (array == null || array.length == 0) {
            return new Long[]{};
        }
        Long[] wrapped = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    /**
     * float の配列を {@link Float} の配列に変換します。
     * @param array float の配列
     * @return      {@link Float} の配列
     */
    public static Float[] wrap(float[] array) {
        if (array == null || array.length == 0) {
            return new Float[]{};
        }
        Float[] wrapped = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    /**
     * double の配列を {@link Double} の配列に変換します。
     * @param array double の配列
     * @return      {@link Double} の配列
     */
    public static Double[] wrap(double[] array) {
        if (array == null || array.length == 0) {
            return new Double[]{};
        }
        Double[] wrapped = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    /**
     * boolean の配列を {@link Boolean} の配列に変換します。
     * @param array boolean の配列
     * @return      {@link Boolean} の配列
     */
    public static Boolean[] wrap(boolean[] array) {
        if (array == null || array.length == 0) {
            return new Boolean[]{};
        }
        Boolean[] wrapped = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    /**
     * char の配列を {@link Character} の配列に変換します。
     * @param array char の配列
     * @return      {@link Character} の配列
     */
    public static Character[] wrap(char[] array) {
        if (array == null || array.length == 0) {
            return new Character[]{};
        }
        Character[] wrapped = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }
}
