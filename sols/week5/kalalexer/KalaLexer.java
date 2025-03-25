package week5.kalalexer;

import java.util.ArrayList;
import java.util.List;

import static week5.kalalexer.KalaToken.Type.*;

public class KalaLexer {
    private static final char TERMINATOR = '\0';
    private final String input;
    private int pos;

    public KalaLexer(String input) {
        this.input = input + TERMINATOR;
        pos = 0;
    }

    public List<KalaToken> readAllTokens() {
        List<KalaToken> tokens = new ArrayList<>();
        while (peek() != TERMINATOR) {
            switch (peek()) {
                case '(':
                    tokens.add(new KalaToken(LPAREN));
                    pos++;
                    break;
                case ')':
                    tokens.add(new KalaToken(RPAREN));
                    pos++;
                    break;
                case ',':
                    tokens.add(new KalaToken(COMMA));
                    pos++;
                    break;
                default:
                    if (Character.isLetter(peek())) {
                        KalaToken tok = readIdentOrNull();
                        tokens.add(tok);
                    } else pos++;
            }
        }
        tokens.add(new KalaToken(EOF));
        return tokens;
    }

    private KalaToken readIdentOrNull() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(peek())) {
            sb.append(peek());
            pos++;
        }
        if (sb.toString().equals("null")) {
            return new KalaToken(NULL);
        } else
            return new KalaToken(IDENT, sb.toString());
    }

    private char peek() {
        return input.charAt(pos);
    }
}
