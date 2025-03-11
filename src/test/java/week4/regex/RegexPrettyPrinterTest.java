package week4.regex;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week4.regex.ast.RegexNode;

import static org.junit.Assert.assertEquals;
import static week4.regex.RegexPrettyPrinter.pretty;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegexPrettyPrinterTest {

    @Test
    public void test01_trivial()  {
        checkPretty("a");
    }

    @Test
    public void test02_simple() {
        checkPretty("abc");
        checkPretty("a|ε");
        checkPretty("(ab)*");
    }

    @Test
    public void test03_difficult()  {
        checkPretty("(ab)*");
        checkPretty("(ab*)*");
        checkPretty("(b*)*");
        checkPretty("ab*cd*");
        checkPretty("(a|b)cd");
        checkPretty("a(b|c)d");
        checkPretty("ab(c|d)");
        checkPretty("a(b|c)d");
        checkPretty("a|b|c");
    }

    private void checkPretty(String input) {
        RegexNode node = RegexParser.parse(input);
        assertEquals(input, pretty(node));
    }
}
