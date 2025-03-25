package week6.parsers.wisdom;

import week6.parsers.Node;
import week6.parsers.Parser;

public class LL1 extends Parser {

    public LL1(String input) {
        super(input);
    }

    public static void main(String[] args) {
        Parser parser = new LL1(args[0]);
        parser.testRecognizer();
    }

    // Grammatika reeglid:
    // S -> aSb | ε
    @Override
    protected Node s() {
        switch (peek()) {
            case 'a':
                // S -> aSb
                match('a');
                s();
                match('b');
                break;
            case 'b':
            case '$':
                // S -> ε
                epsilon();
                break;
            default:
                // Viskame ootamatu sisendi kohta erindi (lubatud on 'a', 'b' või EOF).
                expected('a', 'b', '$');
        }
        return null;
    }
}
