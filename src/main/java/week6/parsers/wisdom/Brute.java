package week6.parsers.wisdom;

import week6.parsers.Node;
import week6.parsers.ParseException;
import week6.parsers.Parser;

public class Brute extends Parser {
    public Brute(String input) {
        super(input);
    }

    // Grammatika reeglid:
    // S -> aSb | ε
    @Override
    protected Node s() {
        int mark = pos;
        try {
            match('a');
            s();
            match('b');
        } catch (ParseException e) {
            pos = mark;
        }
        epsilon();
        return null;
    }
}
