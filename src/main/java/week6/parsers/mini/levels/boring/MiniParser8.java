package week6.parsers.mini.levels.boring;

import week6.parsers.Node;
import week6.parsers.Parser;

public class MiniParser8 extends Parser {

    public MiniParser8(String input) {
        super(input);
    }

    public static Node parse(String src) {
        MiniParser8 parser = new MiniParser8(src);
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
            case 'c':
            case '$':
                n.add(epsilon());
                break;
            default:
                expected('a', 'c', '$');
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
            case 'a':
            case 'c':
                n.add(s());
                n.add(match('c'));
                break;
            default:
                expected('a', 'b', 'c');
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
        if (peek() != 'a') expected('a', 'c');
        return n;
    }

}
