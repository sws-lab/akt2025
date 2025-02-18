package week2.regexapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exercise2 {

    // Kasutame siin ASCII klassid, aga all lisame Unicode flag.
    private static final String VARIABLE = "[\\p{Alpha}_][\\p{Alnum}_]*";

    // Anname siis rühmitusele nimi "fnname":
    // http://stackoverflow.com/questions/415580/regex-named-groups-in-java
    private static final String REGEX =
            "def\\s+(?<fnname>"+ VARIABLE +")\\s*\\(\\s*"+ VARIABLE +"\\s*,\\s*"+ VARIABLE +"\\s*\\)\\s*:";

    public static List<String> extractFunctions(String text){
        List<String> functionNames = new ArrayList<>();
        Pattern pattern = Pattern.compile(REGEX, Pattern.UNICODE_CHARACTER_CLASS); // <-- saab eesti tähtedega kergemini
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            functionNames.add(matcher.group("fnname"));
        }
        return functionNames;
    }
}
