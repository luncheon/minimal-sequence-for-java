package minimal.sequence;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

/**
 * シーケンス {@link Sequence} をテストします。
 */
public class SequenceTest {
    @Test
    public void testIsEmpty() throws Exception {
        assertTrue(Sequence.of().isEmpty());
        assertFalse(Sequence.of(1).isEmpty());
        assertFalse(Sequence.of(1, 2).isEmpty());
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(0, Sequence.of().size());
        assertEquals(1, Sequence.of(1).size());
        assertEquals(2, Sequence.of((Iterable<Integer>) Arrays.asList(1, 2)).size());
    }

    @Test
    public void testEach() throws Exception {
        int[] sum = new int[]{0};
        Sequence.of(1, 2, 3, 4).each(x -> sum[0] += x);
        assertEquals(10, sum[0]);
    }

    @Test
    public void testMap() throws Exception {
        assertEquals(Sequence.of(1, 4, 9), Sequence.of(1, 2, 3).map(x -> x * x));
        assertEquals(Sequence.of(), Sequence.<Integer>of().map(x -> x * x));
    }

    private static Sequence<Integer> sort(Sequence<Integer> sequence) {
        return sequence.match(
                () -> sequence,
                (first, rest) -> Sequence.of(first)
                        .prepend(sort(rest.filter(x -> x <= first)))
                        .append(sort(rest.filter(x -> x > first)))
        );
    }

    @Test
    public void testMatch() throws Exception {
        assertEquals(Sequence.of(2, 3, 5, 7, 7, 11), sort(Sequence.of(7, 11, 5, 3, 7, 2)));
    }

    @Test
    public void testFlatMap() throws Exception {
        assertEquals(Sequence.of("2", "3", "4", "6", "6", "9"), Sequence.of(1, 2, 3).flatMap(x -> Arrays.asList(String.valueOf(x * 2), String.valueOf(x * 3))));
        assertEquals(Sequence.of(), Sequence.<Integer>of().flatMap(x -> Arrays.asList(String.valueOf(x * 2), String.valueOf(x * 3))));
        assertEquals(Sequence.of(), Sequence.<Integer>of(1, 2, 3).flatMap(x -> Collections.emptyList()));
    }

    @Test
    public void testFilter() throws Exception {
        assertEquals(Sequence.of(2, 4, 2), Sequence.of(1, 2, 3, 4, 3, 2, 1).filter(x -> x % 2 == 0));
        assertEquals(Sequence.of(), Sequence.<Integer>of().filter(x -> true));
    }

    @Test
    public void testOfClass() throws Exception {
        assertEquals(Sequence.of(1, 2), Sequence.of(1, 1.5, "1.75", 2).ofClass(Integer.class));
        assertEquals(Sequence.<Number>of(1, 1.5, 2), Sequence.of(1, 1.5, "1.75", 2).ofClass(Number.class));
    }

    @Test
    public void testZip() throws Exception {
        assertEquals(Sequence.of(Pair.of(1, "abc"), Pair.of(2, "def")), Sequence.of(1, 2).zip(Sequence.of("abc", "def")));
        assertEquals(Sequence.of(Pair.of(1, "abc"), Pair.of(2, "def")), Sequence.of(1, 2, 3).zip(Sequence.of("abc", "def")));
        assertEquals(Sequence.of(Pair.of(1, "abc"), Pair.of(2, "def")), Sequence.of(1, 2).zip(Sequence.of("abc", "def", "ghi")));
        assertEquals(Sequence.<Pair<Integer, String>>of(), Sequence.of(1, 2).zip(Sequence.of((Iterable<String>) null)));

        assertEquals(Sequence.of(Pair.of(1, "abc"), Pair.of(2, "def")), Sequence.of(1, 2).zip("abc", "def"));
        assertEquals(Sequence.of(Pair.of(1, "abc"), Pair.of(2, "def")), Sequence.of(1, 2, 3).zip("abc", "def"));
        assertEquals(Sequence.of(Pair.of(1, "abc"), Pair.of(2, "def")), Sequence.of(1, 2).zip("abc", "def", "ghi"));
        assertEquals(Sequence.<Pair<Integer, String>>of(), Sequence.of(1, 2).zip(Sequence.of((String[]) null)));
    }

    @Test
    public void testTakeWhile() throws Exception {
        assertEquals(Sequence.<Integer>of(1, 2), Sequence.of(1, 2, 3, 2, 1).takeWhile(x -> x < 3));
        assertEquals(Sequence.<Integer>of(), Sequence.of(1, 2, 3, 2, 1).takeWhile(x -> x > 1));
        assertEquals(Sequence.<Integer>of(), Sequence.<Integer>of().takeWhile(x -> true));
    }

    @Test
    public void testSkipWhile() throws Exception {
        assertEquals(Sequence.<Integer>of(3, 2, 1), Sequence.of(1, 2, 3, 2, 1).skipWhile(x -> x < 3));
        assertEquals(Sequence.<Integer>of(1, 2, 3, 2, 1), Sequence.of(1, 2, 3, 2, 1).skipWhile(x -> x > 1));
        assertEquals(Sequence.<Integer>of(), Sequence.<Integer>of().skipWhile(x -> true));
    }

    @Test
    public void testTake() throws Exception {
        assertEquals(Sequence.of(1, 2, 3), Sequence.of(1, 2, 3, 4, 5).take(3));
        assertEquals(Sequence.of(1, 2, 3), Sequence.of(1, 2, 3).take(5));
        assertEquals(Sequence.of(), Sequence.of().take(2));
    }

    @Test
    public void tesSkip() throws Exception {
        assertEquals(Sequence.of(4, 5), Sequence.of(1, 2, 3, 4, 5).skip(3));
        assertEquals(Sequence.of(), Sequence.of(1, 2, 3).skip(5));
        assertEquals(Sequence.of(), Sequence.of().skip(2));
    }

    @Test
    public void testAppend() throws Exception {
        assertEquals(Sequence.<String>of("ab", "cd", "ef", "gh"), Sequence.of("ab", "cd").append("ef", "gh"));
        assertEquals(Sequence.<String>of("ef", "gh"), Sequence.of().append("ef", "gh"));
        assertEquals(Sequence.<String>of("ab", "cd"), Sequence.of("ab", "cd").append((String[]) null));
    }

    @Test
    public void testPrepend() throws Exception {
        assertEquals(Sequence.<String>of("ab", "cd", "ef", "gh"), Sequence.of("ef", "gh").prepend("ab", "cd"));
        assertEquals(Sequence.<String>of("ef", "gh"), Sequence.of().prepend("ef", "gh"));
        assertEquals(Sequence.<String>of("ab", "cd"), Sequence.of("ab", "cd").prepend((String[]) null));
    }

    @Test
    public void testFirst() throws Exception {
        assertEquals(Maybe.of(1), Sequence.of(1, 2, 3).first());
        assertEquals(Maybe.nothing, Sequence.of().first());
    }

    @Test
    public void testRest() throws Exception {
        assertEquals(Sequence.<Integer>of(2, 3), Sequence.of(1, 2, 3).rest());
        assertEquals(Sequence.<Integer>of(), Sequence.of(1).rest());
        assertEquals(Sequence.<Integer>of(), Sequence.of().rest());
    }

    @Test
    public void testSingle() throws Exception {
        assertEquals(Maybe.of(1), Sequence.of(1).single());
        assertEquals(Maybe.nothing, Sequence.of().single());
        assertEquals(Maybe.nothing, Sequence.of(1, 2).single());
    }

    @Test
    public void testIndexOf() throws Exception {
        assertEquals(-1, Sequence.of("aa", "bb", "cc", "dd").indexOf("ab"));
        assertEquals(0, Sequence.of("aa", "bb", "cc", "dd").indexOf("aa"));
        assertEquals(3, Sequence.of("aa", "bb", "cc", "dd").indexOf("dd"));

        assertEquals(-1, Sequence.of("aa", "bb", "cc", "dd").indexOf(s -> s.startsWith("e")));
        assertEquals(0, Sequence.of("aa", "bb", "cc", "dd").indexOf(s -> s.startsWith("a")));
        assertEquals(3, Sequence.of("aa", "bb", "cc", "dd").indexOf(s -> s.startsWith("d")));
    }

    @Test
    public void testContains() throws Exception {
        assertFalse(Sequence.of().contains(1));
        assertTrue(Sequence.of("abc", "def").contains("def"));
        assertFalse(Sequence.of("abc", "def").contains("de"));
    }

    @Test
    public void testAny() throws Exception {
        assertFalse(Sequence.of().any(x -> true));
        assertTrue(Sequence.of(1, 2, 3).any(x -> x.equals(2)));
        assertFalse(Sequence.of(1, 3, 5).any(x -> x.equals(2)));
    }

    @Test
    public void testAll() throws Exception {
        assertTrue(Sequence.of().all(x -> false));
        assertFalse(Sequence.of(1, 2, 3).all(x -> x < 3));
        assertTrue(Sequence.of(1, 2, 3).all(x -> x < 4));
    }

    @Test
    public void testMinBy() throws Exception {
        assertEquals(Maybe.of(1), Sequence.of(2, 4, 6, 1, 3, 5).minBy(x -> x));
        assertEquals(Maybe.of(6), Sequence.of(2, 4, 6, 1, 3, 5).minBy(x -> -x));
        assertEquals(Maybe.nothing, Sequence.<Integer>of().minBy(x -> x));
    }

    @Test
    public void testMaxBy() throws Exception {
        assertEquals(Maybe.of(6), Sequence.of(2, 4, 6, 1, 3, 5).maxBy(x -> x));
        assertEquals(Maybe.of(1), Sequence.of(2, 4, 6, 1, 3, 5).maxBy(x -> -x));
        assertEquals(Maybe.nothing, Sequence.<Integer>of().maxBy(x -> x));
    }

    @Test
    public void testSortBy() throws Exception {
        Sequence<Pair<Integer, String>> source = Sequence.of(Pair.of(7, "b"), Pair.of(2, "b"), Pair.of(4, "c"), Pair.of(2, "a"));
        assertEquals(Sequence.of(Pair.of(2, "b"), Pair.of(2, "a"), Pair.of(4, "c"), Pair.of(7, "b")), source.sortBy(Pair::first));
        assertEquals(Sequence.of(Pair.of(2, "a"), Pair.of(7, "b"), Pair.of(2, "b"), Pair.of(4, "c")), source.sortBy(Pair::second));
    }

    @Test
    public void testJoinToString() throws Exception {
        assertEquals("1, 2, 3", Sequence.of(1, 2, 3).joinToString(", "));
        assertEquals("1", Sequence.of(1).joinToString(", "));
        assertEquals("", Sequence.of().joinToString(", "));
    }

    @Test
    public void testAddTo() throws Exception {
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3, 4)), Sequence.of(3, 4).addTo(new ArrayList<>(Arrays.asList(1, 2))));
    }

    @Test
    public void testToArrayList() throws Exception {
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3, 4)), Sequence.of(1, 2, 3, 4).toArrayList());
    }

    @Test
    public void testToHashMap() throws Exception {
        HashMap<String, Integer> map = Sequence.of(1, 2, 3).toHashMap(String::valueOf, x -> x * x);
        assertEquals(3, map.size());
        assertEquals(1, (int) map.get("1"));
        assertEquals(4, (int) map.get("2"));
        assertEquals(9, (int) map.get("3"));
    }

    @Test
    public void testGroupBy() throws Exception {
        LinkedHashMap<Integer, Sequence<Integer>> groups = Sequence.of(0, 1, 2, 3, 4, 5, 6, 7).groupBy(x -> x % 3);
        assertEquals(3, groups.size());
        assertEquals(Sequence.<Integer>of(0, 3, 6), groups.get(0));
        assertEquals(Sequence.<Integer>of(1, 4, 7), groups.get(1));
        assertEquals(Sequence.<Integer>of(2, 5), groups.get(2));
    }

    @Test
    public void testSample() throws Exception {
        assertEquals("exists 556",
                Sequence.of("123", "456", "789")        // <= Array or Iterable
                        .map(Integer::valueOf)
                        .prepend(22, 33, 44)
                        .append(55, 66, 77)
                        .filter(x -> x % 2 == 0)
                        .each(System.out::println)      // 22 44 456 66
                        .sortBy(x -> -x)
                        .each(System.out::println)      // 456 66 44 22
                        .first()                        // => Maybe<Integer>(456)
                        .each(System.out::println)      // 456
                        .map(x -> x + 100)
                        .match(
                                () -> "empty",
                                x -> "exists " + x
                        )                               // => String("exists 556")
        );
    }
}
