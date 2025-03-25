package week6.parsers.xtra;

import week6.parsers.Node;
import week6.parsers.Parser;

// S →  aSb | aA | c
// A → bAb | d | Ac
public class SAParser extends Parser {

    public SAParser(String input) {
        super(input);
    }

    // S → aR | c
    @Override
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Parser p = new SAParser("abdb");
        p.testParser();
    }
}
