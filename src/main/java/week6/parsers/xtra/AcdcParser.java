package week6.parsers.xtra;

import week6.parsers.Node;
import week6.parsers.Parser;

// S → aSdS | ε | CS
// C → c | C/
public class AcdcParser extends Parser {

    public AcdcParser(String input) {
        super(input);
    }

    // S → aSdS | ε | CS
    @Override
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Parser p = new AcdcParser("ac/dc");
        p.testParser();
    }
}
