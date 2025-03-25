package week6.parsers.astdemo;

import week6.parsers.Node;
import week6.parsers.Parser;

public class AvaldisAst extends Parser {

    public AvaldisAst(String input) {
        super(input);
    }

    public static void main(String[] args) {
        AvaldisAst parser = new AvaldisAst(args[0]);
        parser.testParser();
    }

    // S → T R
    @Override
    protected Node s() {
        Node n;
        Node tmp = t();
        n = r(tmp);
        return n;
        // ehk lihtsalt: return r(t())
    }

    // R → '+' T R | ε
    private Node r(Node left) {
        Node n = null;
        switch (peek()) {
            case '+':
                Node tmp = match('+');
                tmp.add(left);
                tmp.add(t());
                n = r(tmp);
                break;
            case '$':
            case ')':
                n = left;
                break;
            default:
                expected('+', '$', ')');
        }
        return n;
    }

    // T → F Q
    private Node t() {
        return q(f());
    }

    // Q → '*' F Q | ε
    private Node q(Node left) {
        Node n = null;
        switch (peek()) {
            case '*':
                n = match('*');
                n.add(left);
                n.add(f());
                n = q(n);
                break;
            case '$':
            case '+':
            case ')':
                n = left;
                break;
            default:
                expected('*', '+', '$', ')');
        }
        return n;
    }

    // F → 'x' | '(' S ')'
    private Node f() {
        Node n = null;
        switch (peek()) {
            case '(':
                match('(');
                n = s();
                match(')');
                break;
            case 'x':
                n = match('x');
                break;
            default:
                expected('(', 'x');
        }
        return n;
    }

}
