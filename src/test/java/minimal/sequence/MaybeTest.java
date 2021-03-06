package minimal.sequence;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * {@link Maybe} コンテナをテストします。
 */
public class MaybeTest {
    @Test
    public void testAsNullable() throws Exception {
        assertEquals(Integer.valueOf(1), Maybe.of(1).asNullable());
        assertEquals(null, Maybe.<Integer>nothing().asNullable());
        assertEquals(null, Maybe.of((Integer) null).asNullable());
    }

    @Test
    public void testOrElse() throws Exception {
        assertEquals(Integer.valueOf(1), Maybe.of(1).orElse(2));
        assertEquals(Integer.valueOf(2), Maybe.<Integer>nothing().orElse(2));
        assertEquals(null, Maybe.<Integer>nothing().orElse((Integer) null));

        assertEquals(Integer.valueOf(1), Maybe.of(1).orElse(() -> 2));
        assertEquals(Integer.valueOf(2), Maybe.<Integer>nothing().orElse(() -> 2));
        assertEquals(null, Maybe.<Integer>nothing().orElse(() -> null));
    }

    @Test
    public void testOr() throws Exception {
        assertEquals(Maybe.of(1), Maybe.of(1).or(Maybe.of(2)));
        assertEquals(Maybe.of(2), Maybe.<Integer>nothing().or(Maybe.of(2)));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().or(Maybe.<Integer>nothing()));

        assertEquals(Maybe.of(1), Maybe.of(1).or(() -> Maybe.of(2)));
        assertEquals(Maybe.of(2), Maybe.<Integer>nothing().or(() -> Maybe.of(2)));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().or(() -> Maybe.<Integer>nothing()));
    }

    @Test
    public void testOrMaybe() throws Exception {
        assertEquals(Maybe.of(1), Maybe.of(1).orMaybe(2));
        assertEquals(Maybe.of(2), Maybe.<Integer>nothing().orMaybe(2));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().orMaybe((Integer) null));

        assertEquals(Maybe.of(1), Maybe.of(1).orMaybe(() -> 2));
        assertEquals(Maybe.of(2), Maybe.<Integer>nothing().orMaybe(() -> 2));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().orMaybe(() -> null));
    }

    @Test
    public void testEach() throws Exception {
        StringBuilder builder = new StringBuilder();
        Maybe.of(1).each(x -> builder.append(String.valueOf(x)));
        assertEquals("1", builder.toString());

        Maybe.<Integer>nothing().each(x -> builder.append(String.valueOf(x)));
        assertEquals("1", builder.toString());

        Maybe.of(1).each(() -> builder.append("nothing"), x -> builder.append(String.valueOf(x)));
        assertEquals("11", builder.toString());

        Maybe.<Integer>nothing().each(() -> builder.append("nothing"), x -> builder.append(String.valueOf(x)));
        assertEquals("11nothing", builder.toString());
    }

    @Test
    public void testMap() throws Exception {
        assertEquals(Maybe.of("1"), Maybe.of(1).map(String::valueOf));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().map(String::valueOf));
        assertEquals(Maybe.nothing, Maybe.of(1).map(x -> null));
    }

    @Test
    public void testMatch() throws Exception {
        assertEquals("1", Maybe.of(1).match(() -> "", String::valueOf));
        assertEquals("", Maybe.<Integer>nothing().match(() -> "", String::valueOf));
    }

    @Test
    public void testFlatMap() throws Exception {
        assertEquals(Maybe.of("1"), Maybe.of(1).flatMap(n -> Maybe.of(String.valueOf(n))));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().flatMap(n -> Maybe.of(String.valueOf(n))));
        assertEquals(Maybe.nothing, Maybe.of(1).flatMap(x -> Maybe.nothing));
    }

    @Test
    public void testSequenceMap() throws Exception {
        assertEquals(Sequence.<String>of("1", "2"), Maybe.of(1).sequenceMap(n -> Sequence.of(String.valueOf(n), String.valueOf(n + 1))));
        assertEquals(Sequence.<String>of(), Maybe.<Integer>nothing().sequenceMap(n -> Sequence.of(String.valueOf(n), String.valueOf(n + 1))));
        assertEquals(Sequence.<String>of(), Maybe.of(1).sequenceMap(n -> Sequence.<String>of()));
    }

    @Test
    public void testFilter() throws Exception {
        assertEquals(Maybe.of(1), Maybe.of(1).filter(x -> x > 0));
        assertEquals(Maybe.nothing, Maybe.of(1).filter(x -> x > 1));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().filter(x -> x > 0));
    }

    @Test
    public void testOfClass() throws Exception {
        assertEquals(Maybe.of(1), Maybe.of(1).ofClass(Integer.class));
        assertEquals(Maybe.of(1), Maybe.of(1).ofClass(Number.class));
        assertEquals(Maybe.nothing, Maybe.of(1).ofClass(Double.class));
        assertEquals(Maybe.nothing, Maybe.nothing.ofClass(Object.class));
    }

    @Test
    public void testZip() throws Exception {
        assertEquals(Maybe.of(Pair.of(1, "abc")), Maybe.of(1).zip(Maybe.of("abc")));
        assertEquals(Maybe.nothing, Maybe.nothing.zip(Maybe.of("abc")));
        assertEquals(Maybe.nothing, Maybe.of(1).zip(Maybe.nothing));
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(Maybe.of("abc").contains("abc"));
        assertFalse(Maybe.of("abc").contains("ab"));
        assertFalse(Maybe.<String>nothing().contains("ab"));
        assertFalse(Maybe.<String>nothing().contains(null));
    }

    @Test
    public void testAny() throws Exception {
        assertTrue(Maybe.of("ab").any("abc"::startsWith));
        assertFalse(Maybe.of("ac").any("abc"::startsWith));
        assertFalse(Maybe.<String>nothing().any("abc"::startsWith));
        assertFalse(Maybe.<String>nothing().any(s -> s == null));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Maybe{Nothing}", Maybe.nothing.toString());
        assertEquals("Maybe{Just 1}", Maybe.of(1).toString());
    }
}
