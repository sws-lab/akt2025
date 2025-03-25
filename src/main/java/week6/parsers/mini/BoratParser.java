package week6.parsers.mini;

import week6.parsers.Node;
import week6.parsers.Parser;


// S → AB
// A → boA | baA | ε
// B → Blo | Bbi | rat
public class BoratParser extends Parser {

    public BoratParser(String input) {
        super(input);
    }

    // S → AB
    @Override
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static Node parse(String src) {
        BoratParser parser = new BoratParser(src);
        return parser.parse();
    }

}

