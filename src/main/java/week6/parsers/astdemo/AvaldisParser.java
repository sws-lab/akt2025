package week6.parsers.astdemo;

import week6.parsers.Node;
import week6.parsers.Parser;

public class AvaldisParser extends Parser {

    public AvaldisParser(String input) {
        super(input);
    }

    public static void main(String[] args) {
        AvaldisParser parser = new AvaldisParser(args[0]);
        parser.testParser();
    }

    // S → T R
    @Override
    protected Node s() {
        Node n = new Node("S");
        n.add(t());
        n.add(r());
        return n;
    }

    // R → '+' T R | ε
    private Node r() {
        Node n = new Node("R");
        switch (peek()) {
            case '+':
                n.add(match('+'));
                n.add(t());
                n.add(r());
                break;
            case '$':
            case ')':
                n.add(epsilon());
                break;
            default:
                expected('+', '$', ')');
        }
        return n;
    }

    // T → F Q
    private Node t() {
        Node n = new Node("T");
        n.add(f());
        n.add(q());
        return n;
    }

    // Q → '*' F Q | ε
    private Node q() {
        Node n = new Node("Q");
        switch (peek()) {
            case '*':
                n.add(match('*'));
                n.add(f());
                n.add(q());
                break;
            case '$':
            case '+':
            case ')':
                n.add(epsilon());
                break;
            default:
                expected('*', '+', '$', ')');
        }
        return n;
    }

    // F → 'x' | '(' S ')'
    private Node f() {
        Node n = new Node("F");
        switch (peek()) {
            case '(':
                n.add(match('('));
                n.add(s());
                n.add(match(')'));
                break;
            case 'x':
                n.add(match('x'));
                break;
            default:
                expected('(', 'x');
        }
        return n;
    }

}
