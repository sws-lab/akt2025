package week4.regex;

import week4.regex.ast.*;

import java.text.ParseException;

/**
 * Regulaaravaldise parsimine. Selle kohta räägime veel tulevikus...
 *
 * @author Vesal Vojdani &lt;vesal@cs.ut.ee&gt;
 */
public class RegexParser {
    final public static char EPS = 'ε';
    final private String input;
    private int pos;

    public static RegexNode parse(String regex) {
        RegexParser parser = new RegexParser(regex);
        return parser.parse();
    }

    public RegexParser(String input) {
        this.input = input;
    }

    public  RegexNode parse() {
        pos=0;
        try {
            return start();
        } catch (ParseException e) {
            System.out.println("Parse error: " + e.getMessage());
            System.out.println(input + '$');
            for (int i=0; i < e.getErrorOffset(); i++)
                System.out.print(' ');
            System.out.println('^');
            e.printStackTrace();
            return null;
        }
    }

    private void match(char x) throws ParseException {
        if (pos < input.length()) {
            char y = input.charAt(pos);
            if (y == x) {
                pos++;
            } else
                throw new ParseException("Unexpected char " + y, pos);
        } else
            throw new ParseException("Unexpected end of file.", pos);
    }

    private RegexNode anychar() throws ParseException {
        if (pos < input.length()) {
            char y = input.charAt(pos);
            pos++;
            return new Letter(y);
        } else
            throw new ParseException("Unexpected end of file.", pos);
    }


    private void done() throws ParseException {
        if (pos < input.length()) {
            char y = input.charAt(pos);
            throw new ParseException("Unexpected char " + y, pos);
        }
    }

    private RegexNode start() throws ParseException {
        RegexNode root = regexp();
        done();
        return root;
    }

    private char peek() {
        if (pos < input.length())
            return input.charAt(pos);
        return '$';
    }

    public static void main(String[] args) {
        RegexParser parser = new RegexParser(args[0]);
        System.out.println("Parsing: " + parser.input);
        RegexNode root = parser.parse();
    }


    // RULES OF THE GRAMMAR

    // S -> Concat '|' S
    private RegexNode regexp() throws ParseException {
        RegexNode fst = concat();
        if (peek() == '|') {
            match('|');
            RegexNode snd = regexp();
            return new Alternation(fst, snd);
        } else
            return fst;
    }

    // Concat -> Repeat Concat
    private RegexNode concat() throws ParseException {
        RegexNode fst = repeat();
        if (peek() != '|' && peek() != '$' && peek() != ')') {
            RegexNode snd = concat();
            return new Concatenation(fst, snd);
        } else
            return fst;
    }

    // Rep -> SimpleExp | SimpleExp*
    private RegexNode repeat() throws ParseException {
        RegexNode fst = simple();
        if (peek() == '*') {
            match('*');
            return new Repetition(fst);
        }
        else
            return fst;
    }

    // SimpleExp -> ID | '(' RegExp ')'
    private RegexNode simple () throws ParseException {
        if (peek() == '(') {
            match('(');
            RegexNode res = regexp();
            match(')');
            return res;
        } else if (peek() == EPS) {
            match(EPS);
            return new Epsilon();
        } else
            return anychar();
    }
}
