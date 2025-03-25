package week6.parsers.mini.levels.awesome;

import week6.parsers.Node;
import week6.parsers.Parser;

public class MiniParser extends Parser {

    public MiniParser(String input) {
        super(input);
    }

    public static Node parse(String src) {
        MiniParser parser = new MiniParser(src);
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
    // A → bR
    private Node a() {
        Node n = new Node('A');
        n.add(match('b'));
        return r(n);
    }

    // R → Sc | B
    private Node r(Node n) {
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
    // B → bQ
    private Node b() {
        Node n = new Node('B');
        n.add(match('b'));
        return q(n);
    }

    // Q → cQ | ε
    private Node q(Node n) {
        switch (peek()) {
            case 'c':
                Node b = new Node('B');
                b.add(n);
                b.add(match('c'));
                return q(b);
            case 'a':
                return n;
            default:
                expected('a', 'c');
        }
        return n;
    }

}

