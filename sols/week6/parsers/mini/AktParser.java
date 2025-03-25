package week6.parsers.mini;

import week6.parsers.Node;
import week6.parsers.Parser;

// S → AkT
// A -> aA | ε
// T -> t | tSp | Tt
// R -> tR | ε
public class AktParser extends Parser {

    public AktParser(String input) {
        super(input);
    }

    // S → AkT
    @Override
    protected Node s() {
        Node n = new Node('S');
        n.add(a());
        n.add(match('k'));
        n.add(t());
        return n;
    }

    // A -> aA | ε
    private Node a() {
        Node n = new Node('A');
        switch (peek()) {
            case 'a':
                n.add(match('a'));
                n.add(a());
                break;
            case 'k':
                n.add(epsilon());
                break;
            default:
                expected('a', 'k');
        }
        return n;
    }

    //  T -> t | tSp | Tt
    //  T -> t (SP|ε) R
    private Node t() {
        Node n = new Node('T');
        n.add(match('t'));
        switch (peek()) {
            case 'a':
            case 'k':
                n.add(s());
                n.add(match('p'));
                break;
            case 'p':
            case 't':
            case '$':
                break;
            default:
                expected('a','k','p','t','$');
        }
        return r(n);
    }

    // R -> tR | ε
    private Node r(Node n) {
        switch (peek()) {
            case 't':
                Node t = new Node('T');
                t.add(n);
                t.add(match('t'));
                return r(t);
            case 'p':
            case '$':
                return n;
            default:
                expected('t', 'p', '$');
        }
        return null;
    }

    public static Node parse(String src) {
        AktParser parser = new AktParser(src);
        return parser.parse();
    }

}

