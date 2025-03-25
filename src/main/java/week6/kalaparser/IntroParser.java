package week6.kalaparser;

/**
 * Kirjutame ise parseri, et saaks parseri abifunktsioonidest aru!
 * Testide läbimiseks ei pea ASTi tagastama, seega on parse meetodi tüüp esialgu void.
 */
public class IntroParser {

    public static void parse(String input) {
        throw new UnsupportedOperationException();
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
