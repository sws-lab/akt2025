package week6.kalaparser;

import week5.kalalexer.KalaLexer;
import week5.kalalexer.KalaToken;

import java.util.List;

import static week5.kalalexer.KalaToken.Type.*;

public class KalaParser {

    private final List<KalaToken> tokens;
    private int pos;

    private KalaParser(KalaLexer lexer) {
        this.tokens = lexer.readAllTokens();
        this.pos = 0;
    }

    public static KalaNode parse(String input) {
        KalaLexer lexer = new KalaLexer(input);
        // Ja kuidagi võiks nüüd seda lekserit kasutada oma parseris...
        KalaParser parser = new KalaParser(lexer);
        return parser.parse();
    }

    private KalaNode parse() {
        KalaNode node = s();
        done();
        return node;
    }

    private void done() {
        if (peek().getType() != EOF) throw new KalaParseException(peek(), EOF);
    }

    private KalaToken peek() {
        return tokens.get(pos);
    }

    private void match(KalaToken.Type tokenType) {
        if (peek().getType() != tokenType) {
            throw new KalaParseException(peek(), tokenType);
        }
        pos++;
    }


    // S → (L) | ()
    // L → A,L | A
    // A → w | 0 | S
    private KalaNode s() {
        return null;
    }

    public static void main(String[] args) {
        KalaNode ast = parse("(kala, (x,y  , null, (), (kala,()) ))");
        System.out.println(ast);
    }
}
