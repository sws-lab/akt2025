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
        Node n = new Node('S');
        n.add(a());
        n.add(b());
        return n;
    }

    // A → b(o|a)A | ε
    private Node a() {
        Node n = new Node('A');
        switch (peek()) {
            case 'b':
                n.add(match('b'));
                switch (peek()) {
                    case 'o' -> n.add(match('o'));
                    case 'a' -> n.add(match('a'));
                    default -> expected('o', 'a');
                }
                n.add(a());
                break;
            case 'r':
                n.add(epsilon());
                break;
            default:
                expected('b', 'r');
        }
        return n;
    }

    //  B → rat R
    private Node b() {
        Node n = new Node('B');
        n.add(match('r'));
        n.add(match('a'));
        n.add(match('t'));
        return r(n);
    }

    // R → biR | loR | ε
    private Node r(Node n) {
        Node b = new Node('B');
        b.add(n);
        switch (peek()) {
            case 'l':
                b.add(match('l'));
                b.add(match('o'));
                return r(b);
            case 'b':
                b.add(match('b'));
                b.add(match('i'));
                return r(b);
            case '$':
                return n;
            default:
                expected('l', 'b', '$');
        }
        return null;
    }

    public static Node parse(String src) {
        BoratParser parser = new BoratParser(src);
        return parser.parse();
    }

}

