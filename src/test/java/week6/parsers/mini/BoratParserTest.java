package week6.parsers.mini;

import org.junit.Test;
import week6.parsers.TestParser;

public class BoratParserTest {

    private final TestParser<BoratParser> boratParser = new TestParser<>(BoratParser.class);
    
    /*
        S -> AB
        A -> boA | epsilon
        B -> rat
    */

    @Test
    public void testParse01() {
        boratParser.accepts("borat");
        boratParser.rejects("baruto");
        boratParser.rejects("");
    }

    @Test
    public void testParse02() {
        boratParser.accepts("rat");
        boratParser.accepts("boborat");
        boratParser.rejects("bullrat");
        boratParser.rejects("bobo");
        boratParser.rejects("");
    }

    /*
        S -> AB
        A -> boA | baA | epsilon
        B -> rat
    */

    @Test
    public void testParse03() {
        boratParser.accepts("boborat");
        boratParser.rejects("bobocat");
        boratParser.rejects("");
    }

    @Test
    public void testParse04() {
        boratParser.accepts("boborat");
        boratParser.accepts("bobababorat");
        boratParser.accepts("babarat");
        boratParser.rejects("");
        boratParser.rejects("baba");
    }

    @Test
    public void testParse05() {
        boratParser.accepts("borat", "S(A(b,o,A(ε)),B(r,a,t))");
        boratParser.accepts("boboborat", "S(A(b,o,A(b,o,A(b,o,A(ε)))),B(r,a,t))");
        boratParser.accepts("bobababorat", "S(A(b,o,A(b,a,A(b,a,A(b,o,A(ε))))),B(r,a,t))");
    }


    /*
        S -> AB
        A -> boA | baA | epsilon
        B -> Blo | Bbi | rat
    */

    @Test
    public void testParse06() {
        boratParser.accepts("boratbilo");
        boratParser.accepts("ratlolo");
        boratParser.rejects("bilo");
        boratParser.rejects("");
    }

    @Test
    public void testParse07() {
        boratParser.accepts("boratbilo", "S(A(b,o,A(ε)),B(B(B(r,a,t),b,i),l,o))");
        boratParser.accepts("ratlolo", "S(A(ε),B(B(B(r,a,t),l,o),l,o))");
    }

    @Test
    public void testParse08() {
        boratParser.rejects("", 0, "br");
        boratParser.rejects("bq", 1, "oa");
        boratParser.rejects("boratz", 5, "bl$");
        boratParser.rejects("borit", 3, "a");
    }

}
