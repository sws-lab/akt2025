package week6.parsers;

/**
 * Näide Parser raamistiku kasutamise kohta.
 */
public class ParserDemo extends Parser {

    public ParserDemo(String input) {
        super(input);
    }

    // Grammatika reeglid:
    // S -> aSb | ε
    @Override
    protected Node s() {
        Node n = new Node("S");
        switch (peek()) {
            case 'a':
                // S -> aSb
                n.add(match('a'));
                n.add(s());
                n.add(match('b'));
                break;
            case 'b':
            case '$':
                // S -> ε
                n.add(epsilon());
                break;
            default:
                expected('a', 'b', '$');
        }
        return n;
    }

    public static Node parse(String s) {
        ParserDemo parserDemo = new ParserDemo(s);
        return parserDemo.parse();
    }

    public static void main(String[] args) {
        Parser parser = new ParserDemo("aabb");
        parser.testParser();
    }

}
