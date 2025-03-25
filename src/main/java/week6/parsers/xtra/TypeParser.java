package week6.parsers.xtra;

import week6.parsers.Node;
import week6.parsers.Parser;
import week6.parsers.xtra.typeast.*;

// S → T D
// T → 'int'
// T → 'void'
// D → '*' D
// D → A
// A → '(' D ')'
// A → A '[]'
// A → ε
public class TypeParser extends Parser {

    public TypeParser(String input) {
        super(input);
    }

    @Override
    protected Node s() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Parser p = new TypeParser("int*[]");
        System.out.println(((Type) p.parse()).toEnglish());
    }
}
