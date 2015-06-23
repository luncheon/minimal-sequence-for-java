package minimal.sequence;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
        assertArrayEquals(new Integer[]{1, 4, 9}, Sequence.of(1, 2, 3).map(x -> x * x).toArray());
        assertArrayEquals(new Integer[]{}, Sequence.<Integer>of().map(x -> x * x).toArray());
    }

    @Test
    public void testFilter() throws Exception {
        assertArrayEquals(new Integer[]{2, 4, 2}, Sequence.of(1, 2, 3, 4, 3, 2, 1).filter(x -> x % 2 == 0).toArray());
        assertArrayEquals(new Integer[]{}, Sequence.<Integer>of().filter(x -> true).toArray());
    }

    @Test
    public void testTakeWhile() throws Exception {
        assertArrayEquals(new Integer[]{1, 2}, Sequence.of(1, 2, 3, 2, 1).takeWhile(x -> x < 3).toArray());
        assertArrayEquals(new Integer[]{}, Sequence.of(1, 2, 3, 2, 1).takeWhile(x -> x > 1).toArray());
        assertArrayEquals(new Integer[]{}, Sequence.<Integer>of().takeWhile(x -> true).toArray());
    }

    @Test
    public void testSkipWhile() throws Exception {
        assertArrayEquals(new Integer[]{3, 2, 1}, Sequence.of(1, 2, 3, 2, 1).skipWhile(x -> x < 3).toArray());
        assertArrayEquals(new Integer[]{1, 2, 3, 2, 1}, Sequence.of(1, 2, 3, 2, 1).skipWhile(x -> x > 1).toArray());
        assertArrayEquals(new Integer[]{}, Sequence.<Integer>of().skipWhile(x -> true).toArray());
    }

    @Test
    public void testAppend() throws Exception {
        assertArrayEquals(new String[]{"ab", "cd", "ef", "gh"}, Sequence.of("ab", "cd").append("ef", "gh").toArray());
        assertArrayEquals(new String[]{"ef", "gh"}, Sequence.of().append("ef", "gh").toArray());
    }

    @Test
    public void testPrepend() throws Exception {
        assertArrayEquals(new String[]{"ab", "cd", "ef", "gh"}, Sequence.of("ef", "gh").prepend("ab", "cd").toArray());
        assertArrayEquals(new String[]{"ef", "gh"}, Sequence.of().prepend("ef", "gh").toArray());
    }

    @Test
    public void testFirst() throws Exception {
        assertEquals(Maybe.of(1), Sequence.of(1, 2, 3).first());
        assertEquals(Maybe.nothing, Sequence.of().first());
    }

    @Test
    public void testRest() throws Exception {
        assertArrayEquals(new Integer[]{2, 3}, Sequence.of(1, 2, 3).rest().toArray());
        assertArrayEquals(new Integer[]{}, Sequence.of(1).rest().toArray());
        assertArrayEquals(new Integer[]{}, Sequence.of().rest().toArray());
    }

    @Test
    public void testSingle() throws Exception {
        assertEquals(Maybe.of(1), Sequence.of(1).single());
        assertEquals(Maybe.nothing, Sequence.of().single());
        assertEquals(Maybe.nothing, Sequence.of(1, 2).single());
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
    public void testJoinToString() throws Exception {
        assertEquals("1, 2, 3", Sequence.of(1, 2, 3).joinToString(", "));
        assertEquals("1", Sequence.of(1).joinToString(", "));
        assertEquals("", Sequence.of().joinToString(", "));
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
        LinkedHashMap<Integer, List<Integer>> groups = Sequence.of(0, 1, 2, 3, 4, 5, 6, 7).groupBy(x -> x % 3);
        assertEquals(3, groups.size());
        assertArrayEquals(new Integer[]{0, 3, 6}, groups.get(0).toArray());
        assertArrayEquals(new Integer[]{1, 4, 7}, groups.get(1).toArray());
        assertArrayEquals(new Integer[]{2, 5}, groups.get(2).toArray());
    }
}
