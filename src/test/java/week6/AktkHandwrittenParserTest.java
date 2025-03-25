package week6;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import week5.AktkToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static week5.AktkToken.Type.*;
import static week6.AktkNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkHandwrittenParserTest {

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));


    private final HashMap<String, Integer> env = new HashMap<>();

    @Before
    public void setUp() {
        env.put("kala", 2);
        env.put("koer", -5);
        env.put("x", 10);
    }


    @Test
    public void test01_recognizeBasics() {
        checkAccept("55", true);
        checkAccept("kala", true);
        checkAccept("5 5", false);
    }

    @Test
    public void test02_recognizeOps() {
        checkAccept("kala+5", true);
        checkAccept("7-6", true);
        checkAccept("x 5", false);
    }

    @Test
    public void test03_recognizeParen() {
        checkAccept("5+(kala-4)", true);
        checkAccept("5x", false);
        checkAccept("5+kala-4", true);
        checkAccept("5)", false);
    }

    @Test
    public void test04_recognizeAll() {
        checkAccept("5+(kala-4)*x+8-(5/68)", true);
        checkAccept("x*3/kala", true);
        checkAccept("+", false);
    }

    private void checkAccept(String input, boolean accept) {
        try {
            AktkHandwrittenParser.parse(input);
            assertTrue(input + " ei kuulu tegelikult keelde", accept);
        } catch (AktkParseException e) {
            assertFalse(input + " peaks kuuluma keelde", accept);
        }
    }

    @Test
    public void test05_parseOps() {
        checkEval("kala+5", 7);
        checkEval("7-6", 1);
    }

    @Test
    public void test06_parsePrio() {
        checkEval("5-(kala+4)", -1);
        checkEval("5-kala+4", 7);
        checkEval("1+kala*2", 5);
    }

    @Test
    public void test07_parseAll() {
        checkEval("5+(kala-4)*x+8-(5/68)", -7);
        checkEval("x*3/kala", 15);
    }


    private void checkEval(String input, int expected) {
        AktkNode node = AktkHandwrittenParser.parse(input);
        assertEquals(input, expected, node.eval(env));
    }

    @Test
    public void test08_errorLoc() {
        checkException("5+5-+5", 4);
        checkException("55 5", 3);
        checkException("5+(kala-4))*x+8-(5/68)", 10);
    }

    @Test
    public void test09_expectedSets() {
        List<AktkToken.Type> s1 = Arrays.asList(LPAREN, INTEGER, VARIABLE);
        List<AktkToken.Type> s2 = Arrays.asList(PLUS, MINUS, TIMES, DIV, EOF);
        List<AktkToken.Type> s3 = Arrays.asList(PLUS, MINUS, TIMES, DIV, RPAREN);
        checkException("105+5-+5", 6, s1, s2);
        checkException("15 5", 3, s2, s1);
        checkException("((5 5", 4, s3, s1);
    }

    @Test
    public void test10_pretty() {
        AktkNode node;
        node = minus(num(5), minus(var("kala"), num(5)));
        assertEquals(node.toString(), "5-(kala-5)", node.toPrettyString());
        node = minus(num(5), plus(var("kala"), num(5)));
        assertEquals(node.toString(), "5-(kala+5)", node.toPrettyString());
        node = plus(num(5), minus(var("kala"), num(5)));
        assertEquals(node.toString(), "5+kala-5", node.toPrettyString());

        node = mul(num(5), minus(num(7), num(5)));
        assertEquals(node.toString(), "5*(7-5)", node.toPrettyString());
        node = minus(num(5), mul(num(7), num(5)));
        assertEquals(node.toString(), "5-7*5", node.toPrettyString());
        node = div(num(5), mul(num(7), num(5)));
        assertEquals(node.toString(), "5/(7*5)", node.toPrettyString());

        node = mul(num(3), minus(minus(num(7), num(5)), minus(num(8), num(2))));
        assertEquals(node.toString(), "3*(7-5-(8-2))", node.toPrettyString());
    }

    private void checkException(String input, int location, List<AktkToken.Type> expected, List<AktkToken.Type> unexpected) {
        try {
            AktkHandwrittenParser.parse(input);
            fail("Must throw exception!");
        } catch (AktkParseException e) {
            assertEquals(location, e.getToken().getOffset());
            if (expected != null) {
                String msg = "Your expected set %s should include %s, but not %s."
                        .formatted(e.getExpected(), expected, unexpected);
                assertTrue(msg, e.getExpected().containsAll(expected));
                HashSet<AktkToken.Type> intersection = new HashSet<>(unexpected);
                intersection.retainAll(e.getExpected());
                assertTrue(msg, intersection.isEmpty());
            }
        }
    }

    private void checkException(String input, int location) {
        checkException(input, location, null, null);
    }

}
