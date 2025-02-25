package week3.machines;

public class HtmlStripMachine {

    public String process(char c) {
        return Character.toString(c);
    }

    // Masina kasutamine
    public static void main(String[] args) {
        String input = "<a href='>'>foo</a>";
        System.out.println(cleanUp(input));
    }

    private static String cleanUp(String s) {
        StringBuilder sb = new StringBuilder();
        HtmlStripMachine machine = new HtmlStripMachine();
        for (char c : s.toCharArray()) sb.append(machine.process(c));
        return sb.toString();
    }
}
