package week6.kalaparser;

/**
 * Kirjutame ise parseri, et saaks parseri abifunktsioonidest aru!
 * Testide läbimiseks ei pea ASTi tagastama, seega on parse meetodi tüüp esialgu void.
 */
public class IntroParser {

    private final String input;
    private int pos;

    public IntroParser(String input) {
        this.input = input + '$';
        this.pos = 0;
    }

    public static void parse(String input) {
        IntroParser parser = new IntroParser(input);
        parser.parse();
    }

    private void parse() {
        s();
        done();
    }

    private void done() {
        if (peek() != '$') throw new RuntimeException();
    }

    // S -> a S b
    private void s() {
        if (peek() == 'a') {
            match('a');
            s();
            match('b');
        }
    }

    private char peek() {
        return input.charAt(pos);
    }

    private void match(char expectedChar) {
        char inputChar = input.charAt(pos);
        if (inputChar == expectedChar) {
            pos++;
        } else {
            throw new RuntimeException("Unexpected char at " + pos);
        }
    }

    public static void main(String[] args) {
        String input = "aaabbb";
        System.out.println("Parsing: " + input);
        try {
            IntroParser.parse(input);
            System.out.print("ACCEPT!");
        } catch (RuntimeException e) {
            System.out.println("REJECT!");
        }
    }

}
