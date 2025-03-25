package week6.parsers.mini;

import org.junit.Test;
import week6.parsers.TestParser;

public class AbbaParserTest {

    private final TestParser<AbbaParser> abbaParser = new TestParser<>(AbbaParser.class);
    
    //S -> ABAR
    //A -> a
    //B -> bb
    //R -> +S | epsilon

    @Test
    public void testParse01() {
        abbaParser.accepts("abba");
        abbaParser.rejects("baba");
        abbaParser.rejects("");
    }

    @Test
    public void testParse02() {
        abbaParser.accepts("abba+abba");
        abbaParser.rejects("abba+");
        abbaParser.rejects("+abba");
        abbaParser.rejects("+");
        abbaParser.rejects("");
    }

    //S -> ABAR
    //A -> a | aAb
    //B -> bb
    //R -> +S | epsilon

    @Test
    public void testParse03() {
        abbaParser.accepts("abba");
        abbaParser.accepts("aabbba");
        abbaParser.accepts("aabbbaab");
        abbaParser.accepts("aaabbbbaaaaabbbb");
        abbaParser.rejects("aaabbbbaaaaaabbbb");
        abbaParser.rejects("aaabbbbbaaaaabbbb");
        abbaParser.rejects("baba");
        abbaParser.rejects("");
    }

    @Test
    public void testParse04() {
        abbaParser.accepts("abba+abba");
        abbaParser.accepts("aabbba+aaabbbbaab");
        abbaParser.accepts("aaabbbbaaaaabbbb+aabbba+aaabbbbaab");
        abbaParser.rejects("aababba+aaabbbbaab");
        abbaParser.rejects("aabbba+aaabbbbaabb");
        abbaParser.rejects("+");
        abbaParser.rejects("");
    }

    @Test
    public void testParse05() {
        abbaParser.accepts("abba", "S(A(a),B(b,b),A(a),R(ε))");
        abbaParser.accepts("abba+abba", "S(A(a),B(b,b),A(a),R(+,S(A(a),B(b,b),A(a),R(ε))))");
        abbaParser.accepts("aabbba", "S(A(a,A(a),b),B(b,b),A(a),R(ε))");
        abbaParser.accepts("aaabbbbaaaaabbbb", "S(A(a,A(a,A(a),b),b),B(b,b),A(a,A(a,A(a,A(a,A(a),b),b),b),b),R(ε))");
    }


    //S -> ABAR
    //A -> a | aAb
    //B -> bb | BbSb
    //R -> +S | epsilon

    @Test
    public void testParse06() {
        abbaParser.accepts("abba+abba");
        abbaParser.accepts("abbbabba+abbaba+abba");
        abbaParser.accepts("aabbbbabbbabba+abbaba+abbaba+aaabbbbbaaabbbbaaaaabbbbbaab");
        abbaParser.rejects("abbb+abba+abbaba+abba");
        abbaParser.rejects("abbbabbab+abbaba+abba");
        abbaParser.rejects("aabbbbabbb+abba+abbaba+abbaba+aaabbbbbaaabbbbaaaaabbbbbaab");
        abbaParser.rejects("aabbbbabbbabba+abbaba+abbaba+aaabbbbbaaabbbbaaaaaabbbbbaab");
        abbaParser.rejects("");
    }

    @Test
    public void testParse07() {
        abbaParser.accepts("aabbbbabbaba", "S(A(a,A(a),b),B(B(b,b),b,S(A(a),B(b,b),A(a),R(ε)),b),A(a),R(ε))");
    }

    @Test
    public void testParse08() {
        abbaParser.rejects("c", 0, "a");
        abbaParser.rejects("abbca", 3, "ab");
        abbaParser.rejects("abbacbb", 4, "ab$+");
        abbaParser.rejects("abbbabbab+abba", 9, "ab");
        abbaParser.rejects("abbbabbaba+baba", 11, "a");
    }
    

}
