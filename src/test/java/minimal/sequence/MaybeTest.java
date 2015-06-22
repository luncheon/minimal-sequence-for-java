package minimal.sequence;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * {@link Maybe} コンテナをテストします。
 */
public class MaybeTest {
    @Test
    public void testAnyOf() throws Exception {
        assertEquals(Maybe.of(1), Maybe.anyOf(1, 2, 3));
        assertEquals(Maybe.of(1), Maybe.anyOf(null, null, 1, 2));
        assertEquals(Maybe.nothing, Maybe.anyOf());
        assertEquals(Maybe.nothing, Maybe.anyOf(null, null));
    }

    @Test
    public void testAnyOfGet() throws Exception {
        assertEquals(Maybe.of(1), Maybe.anyOfGet(() -> 1, () -> 2, () -> 3));
        assertEquals(Maybe.of(1), Maybe.anyOfGet(() -> null, () -> null, () -> 1, () -> 2));
        assertEquals(Maybe.nothing, Maybe.anyOfGet());
        assertEquals(Maybe.nothing, Maybe.anyOfGet(() -> null, () -> null));
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

        Maybe.of(1).each(x -> builder.append(String.valueOf(x)), () -> builder.append("nothing"));
        assertEquals("11", builder.toString());

        Maybe.<Integer>nothing().each(x -> builder.append(String.valueOf(x)), () -> builder.append("nothing"));
        assertEquals("11nothing", builder.toString());
    }

    @Test
    public void testMap() throws Exception {
        assertEquals(Maybe.of("1"), Maybe.of(1).map(String::valueOf));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().map(String::valueOf));
        assertEquals(Maybe.nothing, Maybe.of(1).map(x -> null));
    }

    @Test
    public void testFilter() throws Exception {
        assertEquals(Maybe.of(1), Maybe.of(1).filter(x -> x > 0));
        assertEquals(Maybe.nothing, Maybe.of(1).filter(x -> x > 1));
        assertEquals(Maybe.nothing, Maybe.<Integer>nothing().filter(x -> x > 0));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Maybe{Nothing}", Maybe.nothing.toString());
        assertEquals("Maybe{Just 1}", Maybe.of(1).toString());
    }
}
