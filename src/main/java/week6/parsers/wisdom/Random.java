package week6.parsers.wisdom;

import week6.parsers.Node;
import week6.parsers.ParseException;
import week6.parsers.Parser;

public class Random extends Parser {
    private static final int N = 1000;
    private final java.util.Random generator;

    public Random(String input) {
        super(input);
        generator = new java.util.Random();
    }

    public static void main(String[] args) {
        Parser recognizer = new Random(args[0]);
        for (int i = 0; i < N; ) {
            try {
                recognizer.parse();
                System.out.println("Attempts: " + i);
                return;
            } catch (ParseException e) {
                i++;
            }
        }
        System.out.printf("REJECT! (After %d random trials.)\n", N);
    }

    // Grammatika reeglid:
    // S -> aSb | ε
    @Override
    protected Node s() {

        switch (generator.nextInt(2)) {
            case 0 -> { // S -> aSb
                match('a');
                s();
                match('b');
            }
            case 1 -> // S -> ε
                epsilon();
        }

        return null;
    }
}
