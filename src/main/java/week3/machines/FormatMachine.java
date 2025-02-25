package week3.machines;

public class FormatMachine {

    public String process(char c) {
        return "" + c;
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
