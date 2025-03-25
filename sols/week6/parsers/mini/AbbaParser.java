package week6.parsers.mini;

import week6.parsers.Node;
import week6.parsers.Parser;

// S → ABAR
// A → a | aAb
// B → bb | BbSb
// R → +S | ε
public class AbbaParser extends Parser {

    public AbbaParser(String input) {
        super(input);
    }

    // S → ABAR
    @Override
    protected Node s() {
        Node n = new Node('S');
        n.add(a());
        n.add(b());
        n.add(a());
        n.add(r());
        return n;
    }

    // A → a(Ab|ε)
    private Node a() {
        Node n = new Node('A');
        n.add(match('a'));
        switch (peek()) {
            case 'a':
                n.add(a());
                n.add(match('b'));
                break;
            case 'b':
            case '$':
            case '+':
                break;
            default:
                expected('a', 'b', '$', '+');
        }
        return n;
    }
    
    // B → bbQ
    private Node b() {
        Node n = new Node('B');
        n.add(match('b'));
        n.add(match('b'));
        return q(n);
    }

    // Q → bSbQ | ε
    private Node q(Node b) {
        switch (peek()) {
            case 'b':
                Node n = new Node('B');
                n.add(b);
                n.add(match('b'));
                n.add(s());
                n.add(match('b'));
                return q(n);
            case 'a':
                return b;
            default:
                expected('a', 'b');
                return null;
        }
    }

    // R → +S | ε
    private Node r() {
        Node n = new Node('R');
        switch (peek()) {
            case '+':
                n.add(match('+'));
                n.add(s());
                break;
            case 'b':
            case '$':
                n.add(epsilon());
                break;
            default:
                expected('+', '$', 'b');
        }
        return n;
    }

    public static Node parse(String src) {
        AbbaParser parser = new AbbaParser(src);
        return parser.parse();
    }

    public static void main(String[] args) {
        AbbaParser parser = new AbbaParser("abba");
        parser.testParser();
    }

}
