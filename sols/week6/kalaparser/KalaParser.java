package week6.kalaparser;

import week5.kalalexer.KalaLexer;
import week5.kalalexer.KalaToken;

import java.util.ArrayList;
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
        match(LPAREN);
        if (peek().getType() == RPAREN) {
            match(RPAREN);
            return KalaNode.mkList();
        } else {
            List<KalaNode> list = l();
            match(RPAREN);
            return KalaNode.mkList(list);
        }
    }

    private List<KalaNode> l() {
        List<KalaNode> list = new ArrayList<>();
        list.add(a());
        if (peek().getType() == COMMA) {
            match(COMMA);
            list.addAll(l());
            return list;
        }
        return list;
    }

    // Alternatiivse variandina võib seda iteratiivselt teha.
    // L → A,L | A esitame EBNF kujul L → A(,A)*
    private List<KalaNode> l_ebnf() {
        List<KalaNode> list = new ArrayList<>();
        list.add(a());
        while (peek().getType() == COMMA) {
            match(COMMA);
            list.add(a());
        }
        return list;
    }

    private KalaNode a() {
        KalaToken node = peek();
        switch (node.getType()) {
            case IDENT:
                match(IDENT);
                return KalaNode.mkIdent((String) node.getData());
            case NULL:
                match(NULL);
                return KalaNode.mkNull();
            case LPAREN:
                return s();
            default:
                throw new KalaParseException(peek(), IDENT, NULL, LPAREN);
        }
    }

    public static void main(String[] args) {
        KalaNode ast = parse("(kala, (x,y  , null, (), (kala,()) ))");
        System.out.println(ast);
    }
}
