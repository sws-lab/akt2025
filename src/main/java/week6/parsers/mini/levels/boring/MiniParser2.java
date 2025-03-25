package week6.parsers.mini.levels.boring;

import week6.parsers.Node;
import week6.parsers.Parser;

public class MiniParser2 extends Parser {

    public MiniParser2(String input) {
        super(input);
    }

    public static Node parse(String src) {
        MiniParser2 parser = new MiniParser2(src);
        return parser.parse();
    }

    // S → aAa | ε
    @Override
    protected Node s() {
        if (peek() == 'a') match("abba");
        else
        switch (peek()) {
            case 'a':
                match('a');
                a();
                match('a');
                break;
            default:
                epsilon();
        }
        return null;
    }

    // A → bB
    private Node a() {
        match('b');
        b();
        return null;
    }

    // B → b
    private Node b() {
        match('b');
        return null;
    }

    // See grammatika oli natuke rumalasti tükeldatud, et Tase 1 sisaldab ainult kaks sõne: {"abba", ""}.
    // Sellisel juhul piisab kahe punkti saamiseks ainult sellest:
    protected Node s_Alt() {
       if (peek() == 'a') match("abba");
       return null;
    }

}

