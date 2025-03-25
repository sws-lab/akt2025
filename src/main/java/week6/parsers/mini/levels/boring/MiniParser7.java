package week6.parsers.mini.levels.boring;

import week6.parsers.Node;
import week6.parsers.Parser;

public class MiniParser7 extends Parser {

    public MiniParser7(String input) {
        super(input);
    }

    public static Node parse(String src) {
        MiniParser7 parser = new MiniParser7(src);
        return parser.parse();
    }

    // S → aAa | ε
    @Override
    protected Node s() {
        Node n = new Node('S');
        switch (peek()) {
            case 'a':
                n.add(match('a'));
                n.add(a());
                n.add(match('a'));
                break;
            default:
                n.add(epsilon());
        }
        return n;
    }

    // A → bSc | bB
    // A → b (Sc|B)
    private Node a() {
        Node n = new Node('A');
        n.add(match('b'));
        switch (peek()) {
            case 'b':
                n.add(b());
                break;
            default:
                n.add(s());
                n.add(match('c'));
        }
        return n;
    }

    // B → b | Bc
    // B → bc*
    private Node b() {
        Node n = new Node('B');
        n.add(match('b'));
        while (peek() == 'c') {
            Node b = new Node('B');
            b.add(n);
            b.add(match('c'));
            n = b;
        }
        return n;
    }

}

