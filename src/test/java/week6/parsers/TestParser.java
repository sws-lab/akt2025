package week6.parsers;

import java.lang.reflect.Constructor;
import java.util.*;

import static org.junit.Assert.*;

public class TestParser<T extends Parser> {

    final Class<T> parserClass;

    public TestParser(Class<T> parserClass) {
        this.parserClass = parserClass;
    }

    private Node parse(String input) {
        Parser parser;
        try {
            Constructor<T> constructor = parserClass.getConstructor(String.class);
            parser = constructor.newInstance(input);
        } catch (Exception e) {
            throw new RuntimeException(parserClass.getSimpleName() + " is not a Parser subclass.");
        }
        return parser.parse();
    }

    public void accepts(String input, String parsetree) {
        assertEquals(input, parsetree, parse(input).toString());
    }

    public void accepts(String input) {
        parse(input);
    }

    public void rejects(String input, Integer loc, String expected, String unexpected) {
        try {
            parse(input);
            fail("Must throw exception!");
        } catch (ParseException e) {
            if (loc != null) {
                assertEquals("Wrong error location for \"" + input + "\".", loc.intValue(), e.getOffset());
                String pretty = "\nFailing input:\n" + input + '\n' + " ".repeat(loc) + "^\n";
                Set<Character> realExpectedSet = new HashSet<>(asList(expected));
                if (unexpected == null) {
                    String msg = "%sYour expected set was %s. It should be %s."
                            .formatted(pretty, e.getExpected(), realExpectedSet);
                    assertEquals(msg, realExpectedSet, e.getExpected());
                } else {
                    String msg = "%sYour epected set was %s. It should include %s, but exclude %s."
                            .formatted(pretty, e.getExpected(), realExpectedSet, asList(unexpected));
                    assertTrue(msg, e.getExpected().containsAll(realExpectedSet));
                    HashSet<Character> intersection = new HashSet<>(e.getExpected());
                    intersection.retainAll(asList(unexpected));
                    assertTrue(msg, intersection.isEmpty());
                }
            }
        }
    }


    public void rejects(String input, int loc, String expected) {
        rejects(input, loc, expected, null);
    }

    public void rejects(String input) {
        rejects(input, null, null, null);
    }

    // http://stackoverflow.com/questions/6319775/java-collections-convert-a-string-to-a-list-of-characters
    private static List<Character> asList(final String string) {
        return new AbstractList<>() {
            @Override
            public int size() {
                return string.length();
            }

            @Override
            public Character get(int index) {
                return string.charAt(index);
            }
        };
    }


    public Set<Character> getExpected(String prefix, String alphabet) {
        Set<Character> result = new TreeSet<>();
        if (checkWord(prefix, false)) result.add('$');
        for (char c : alphabet.toCharArray()) {
            if (checkWord(prefix + c, true)) result.add(c);
        }
        return result;
    }

    private boolean checkWord(String input, boolean prefix) {
        try {
            parse(input);
            return true;
        } catch (ParseException e) {
            return e.getOffset() == input.length() && prefix;
        }
    }

}
