package week2.intro;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static List<String> grep(String regex, List<String> strings) {
        Pattern pattern = Pattern.compile(regex);
        ArrayList<String> result = new ArrayList<>();
        for (String s : strings) if (pattern.matcher(s).find()) result.add(s);
        return result;
    }

    public static List<String> replace(String regex, String repl, List<String> strings) {
        Pattern pattern = Pattern.compile(regex);
        ArrayList<String> result = new ArrayList<>();
        for (String s : strings) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) result.add(matcher.replaceAll(repl));
        }
        return result;

    }
}
