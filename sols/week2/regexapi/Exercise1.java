package week2.regexapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exercise1 {

    // Kui kasutada rühmad siit: http://www.unicode.org/reports/tr18/#General_Category_Property
    // Siis peaks töötama põhimõtteliselt igal pool, et võib testida regex101.com-is ja tõsta siia.
    private static final String MAAKOND = "\\p{Lu}\\p{Ll}*\\s*maakond";
    private static final String ALEV = "\\p{Lu}\\p{Ll}*\\s*\\p{Ll}*";
    private static final String TANAV = "\\p{Lu}\\p{Ll}*\\s*\\p{Ll}*";
    private static final String MAJANR = "\\d+";
    private static final String TAPSUSTUS = "\\p{Ll}*\\s*\\d+";

    private static final String REGEX =
            MAAKOND + ", " + ALEV + ", " + TANAV + " " + MAJANR + "(, " + TAPSUSTUS + ")?";

    public static List<String> extractAddresses(String text) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(text);
        List<String> addresses = new ArrayList<>();
        while (matcher.find()) {
            addresses.add(matcher.group());
        }
        return addresses;
    }
}
