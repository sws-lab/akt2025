package week2.intro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RegexExercise {

    // sõnad "kalad" või "jalad".
    public static final String RE1 = null;

    // Viietähelised sõned, mis lõppevad tähtedega "alad".
    public static final String RE2 = null;

    // color ja colour
    public static final String RE3 = null;

    // jaha, jahaaaaaaaaa!
    public static final String RE4 = null;

    // binaarsõned
    public static final String RE5 = null;

    // eelviimane täht on "a"
    public static final String RE6 = null;

    // tagasiviited
    public static final String RE7 = null;

    // nimede asendamine: regulaaravaldis
    public static final String RE8 = null;
    // millega asendada? (Util.replace'i teine argument)
    public static final String RP8 = null;

    // Eemaldada sulud!
    public static final String RE9 = null;
    // Siin asendatakse regexiga sobituvad juppid tühja sõnega.



    // Testimise meetod, et saaks natuke debuugida, muidu on ka automaattestid.

    public static void main(String[] args) throws IOException {
        List<String> strings = Files.readAllLines(Paths.get("inputs", "regex.txt"));
        printLines(RegexUtils.grep(RE1, strings));
    }

    private static void printLines(List<String> strings) {
        for (String s : strings) System.out.println(s);
    }

}
