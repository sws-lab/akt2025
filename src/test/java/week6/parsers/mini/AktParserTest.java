package week6.parsers.mini;

import org.junit.Test;
import week6.parsers.TestParser;

public class AktParserTest {

    private final TestParser<AktParser> aktParser = new TestParser<>(AktParser.class);

    /*
        S -> AkT
        A -> aA | epsilon
        T -> t
     */

    @Test
    public void testParse01() {
        aktParser.accepts("akt");
        aktParser.rejects("dme");
        aktParser.rejects("");
    }

    @Test
    public void testParse02() {
        aktParser.accepts("kt");
        aktParser.accepts("akt");
        aktParser.accepts("aaaakt");
        aktParser.rejects("akkt");
        aktParser.rejects("at");
        aktParser.rejects("k");
        aktParser.rejects("t");
        aktParser.rejects("");
    }


    /*
        S -> AkT
        A -> aA | epsilon
        T -> t | tSp
    */

    @Test
    public void testParse03() {
        aktParser.accepts("aktaktp");
        aktParser.rejects("");
    }

    @Test
    public void testParse04() {
        aktParser.accepts("aaaktaktaktpp");
        aktParser.accepts("ktaaaktaktaktppp");
        aktParser.rejects("aaaktaktaktppp");
        aktParser.rejects("ktaaaktaktaktpp");
        aktParser.rejects("");
    }

    @Test
    public void testParse05() {
        aktParser.accepts("kt", "S(A(ε),k,T(t))");
        aktParser.accepts("aaakt", "S(A(a,A(a,A(a,A(ε)))),k,T(t))");
        aktParser.accepts("aktaktp", "S(A(a,A(ε)),k,T(t,S(A(a,A(ε)),k,T(t)),p))");
    }


    /*
        S -> AkT
        A -> aA | epsilon
        T -> t | tSp | Tt
    */

    @Test
    public void testParse06() {
        aktParser.accepts("kttt");
        aktParser.accepts("aktaktpttt");
        aktParser.accepts("aktaakttpttt");
        aktParser.rejects("ktakt");
        aktParser.rejects("aktaktaktpttt");
        aktParser.rejects("");
    }

    @Test
    public void testParse07() {
        aktParser.accepts("kttt", "S(A(ε),k,T(T(T(t),t),t))");
        aktParser.accepts("aktaakttpt", "S(A(a,A(ε)),k,T(T(t,S(A(a,A(a,A(ε))),k,T(T(t),t)),p),t))");
    }

    @Test
    public void testParse08() {
        aktParser.rejects("", 0, "ak");
        aktParser.rejects("akq", 2, "t");
        aktParser.rejects("ktaktq", 5, "akpt$");
        aktParser.rejects("ktttzakt", 4, "tp$");
    }

}
