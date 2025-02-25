package week3.machines;

public class FormatMachine {

    private enum State {
        INIT, PUNC, MIDW
    }
    private State state = State.INIT;

    private static boolean isPunct(char c) {
        return (c == ',' || c == '.' || c == ':' || c == ';' || c == '!' || c == '?');
    }

    public String process(char c) {
        boolean space = false;
        if (c == '(') {
            space = state != State.INIT;
            state = State.INIT;
        } else if (c == ')' || isPunct(c)) {
            state = State.PUNC;
        } else if (c == '\n') {
            state = State.INIT;
        } else if (c == ' ') {
            if (state == State.MIDW) state = State.PUNC;
            return "";
        } else {
            space = state == State.PUNC;
            state = State.MIDW;
        }
        return (space ? " " : "") + c;
    }

    // Masina kasutamine
    public static void main(String[] args) {
        String input =
                """
                        This     text (all of it   )has occasional lapses .. .in
                          punctuation( sometimes,pretty bad ; sometimes ,not so).

                        ( Ha ! )Is this  :fun ! ? !  Or   what  ?""";
        System.out.println(cleanUp(input));
    }

    public static String cleanUp(String s) {
        StringBuilder sb = new StringBuilder();
        FormatMachine machine = new FormatMachine();
        for (char c : s.toCharArray()) sb.append(machine.process(c));
        return sb.toString();
    }
}
