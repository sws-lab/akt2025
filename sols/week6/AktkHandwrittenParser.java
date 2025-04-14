package week6;

import week5.AktkHandwrittenLexer;
import week5.AktkToken;

import java.io.StringReader;

import static week5.AktkToken.Type.*;

public class AktkHandwrittenParser {

    private final AktkHandwrittenLexer lexer;
    private AktkToken current;

    public AktkHandwrittenParser(AktkHandwrittenLexer lexer) {
        this.lexer = lexer;
    }

    private void consume() {
        current = lexer.readNextToken();
    }

    private AktkToken.Type peek() {
        return current.getType();
    }

    private void match(AktkToken.Type t) {
        if (peek() != t) throw new AktkParseException(current, t);
        consume();
    }

    private AktkNode expression() {
        AktkNode left = term();
        AktkToken.Type next = peek();
        while (next == PLUS || next == MINUS) {
            consume();
            left = new AktkNode.BinOp(next, left, term());
            next = peek();
        }
        if (next == EOF || next == RPAREN) {
            return left;
        }
        throw new AktkParseException(current, PLUS, MINUS, TIMES, DIV, EOF, RPAREN);
    }

    private AktkNode term() {
        AktkNode left = factor();
        AktkToken.Type next = peek();
        while (next == TIMES || next == DIV) {
            consume();
            left = new AktkNode.BinOp(next, left, factor());
            next = peek();
        }
        return left;
    }

    private AktkNode factor() {
        AktkToken.Type t = peek();
        AktkNode result;
        switch (t) {
            case VARIABLE:
                result = new AktkNode.Variable((String) current.getData());
                consume();
                break;
            case INTEGER:
                result = new AktkNode.IntLiteral((int) current.getData());
                consume();
                break;
            case LPAREN:
                match(LPAREN);
                result = expression();
                match(RPAREN);
                break;
            default:
                throw new AktkParseException(current, VARIABLE, INTEGER, LPAREN);
        }
        return result;
    }

    public AktkNode parse() {
        consume();
        AktkNode result = expression();
        if (peek() != EOF) {
            throw new AktkParseException(current, EOF);
        }
        return result;
    }

    public static AktkNode parse(String input) {
        AktkHandwrittenLexer lexer = new AktkHandwrittenLexer(new StringReader(input));
        AktkHandwrittenParser parser = new AktkHandwrittenParser(lexer);
        return parser.parse();
    }

}
