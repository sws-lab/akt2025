package week5.kalalexer;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class KalaLexerTest {

    @Test
    public void testReadAllTokens1() {
        check("", List.of("<EOF>"));
        check("()", List.of("<LPAREN>", "<RPAREN>", "<EOF>"));
    }

    @Test
    public void testReadAllTokens2() {
        check("(x)", List.of("<LPAREN>", "<IDENT:x>", "<RPAREN>", "<EOF>"));
        check("(foo)", List.of("<LPAREN>", "<IDENT:foo>", "<RPAREN>", "<EOF>"));
        check("(x, foo)", List.of("<LPAREN>", "<IDENT:x>", "<COMMA>", "<IDENT:foo>", "<RPAREN>", "<EOF>"));
        check("(x, null)", List.of("<LPAREN>", "<IDENT:x>", "<COMMA>", "<NULL>", "<RPAREN>", "<EOF>"));
    }

    @Test
    public void testReadAllTokens3() {
        check("(kala, x, null)", List.of("<LPAREN>", "<IDENT:kala>", "<COMMA>", "<IDENT:x>", "<COMMA>", "<NULL>", "<RPAREN>", "<EOF>"));
        check("(kala, x, nulll)", List.of("<LPAREN>", "<IDENT:kala>", "<COMMA>", "<IDENT:x>", "<COMMA>", "<IDENT:nulll>", "<RPAREN>", "<EOF>"));
    }

    private void check(String input, List<String> expected) {
        KalaLexer lexer = new KalaLexer(input);
        List<KalaToken> tokens = lexer.readAllTokens();
        List<String> tokenStrings = tokens.stream().map(KalaToken::toString).collect(Collectors.toList());
        assertEquals(expected, tokenStrings);
    }
}
