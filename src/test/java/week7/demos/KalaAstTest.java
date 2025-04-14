package week7.demos;

import org.junit.Test;
import week6.kalaparser.KalaNode;

import static org.junit.Assert.*;

public class KalaAstTest {

    @Test
    public void test_recognize() {
        legal("(kala)");
        legal("()");
        legal("(null)");
        legal("(kala, koer, x, x)");
        legal("(kala, koer, x, null, null)");
        legal("(kala, koer, (x, null), null)");
        legal("(((((())))))");
        legal("(kala, (x,y , null, (), (kala,()) ))");

        illegal("() k");
        illegal("kala, (null)");
        illegal("(((((()))))");
        illegal("(kala, (x,y , null, (), (kala,()) )");
    }

    @Test
    public void test_ast() {
        legal("(kala)", "(kala)");
        legal("()", "()");
        legal("(null)", "(NULL)");
        legal("(kala, koer, x, x)", "(kala, koer, x, x)");
        legal("(kala, koer, x, null, null)", "(kala, koer, x, NULL, NULL)");
        legal("(kala, koer, (x, null), null)", "(kala, koer, (x, NULL), NULL)");
        legal("(((((())))))", "(((((())))))");
        legal("(kala, (x,y , null, (), (kala,()) ))", "(kala, (x, y, NULL, (), (kala, ())))");
    }

    private void legal(String input) {
        KalaAst.makeKalaAst(input);
    }

    private void legal(String input, String expectedAstString) {
        KalaNode actualAst = KalaAst.makeKalaAst(input);
        assertEquals(expectedAstString, actualAst.toString());
    }

    private void illegal(String input) {
        try {
            KalaAst.makeKalaAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
