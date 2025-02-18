package week2.intro;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RegexExerciseTest {


    private List<String> strings;

    @Before
    public void setUpInput() throws IOException {
        strings = Files.readAllLines(Paths.get("inputs", "regex.txt"));
    }

    @Test
    public void test01() {
        check(RegexExercise.RE1,
                "Mull-mull-mull-mull, väiksed kalad",
                "kus on teie väiksed jalad?");
    }

    @Test
    public void test02() {
        check(RegexExercise.RE2,
                "Mull-mull-mull-mull, väiksed kalad",
                "kus on teie väiksed jalad?",
                "Aastavahetuse magusad palad");
    }

    @Test
    public void test03() {
        check(RegexExercise.RE3,
                "This color is green.",
                "This colour is red.");
    }

    @Test
    public void test04() {
        check(RegexExercise.RE4,
                "jahaaaaaaaaaaaaa!",
                "jaha",
                "jaahaa");
    }

    @Test
    public void test05() {
        check(RegexExercise.RE5,
                "0100010",
                "011110110",
                "100",
                "",
                "1",
                "101");
    }

    @Test
    public void test06() {
        check(RegexExercise.RE6,
                "abab",
                "aaaa",
                "ababab",
                "baab");
    }

    @Test
    public void test07() {
        check(RegexExercise.RE7,
                "abab",
                "aaaa",
                "baba",
                "haha");
    }

    @Test
    public void test08() {
        checkReplace(RegexExercise.RE8, RegexExercise.RP8,
                "Aavik, Johannes",
                "Adamson, Hendrik",
                "Adson, Artur",
                "Afanasjev, Vahur",
                "Alavainu, Ave",
                "Alle, August",
                "Alliksaar, Artur",
                "Alver, Betti",
                "Arder, Ott");
    }


    @Test
    public void test09() {
        checkReplace(RegexExercise.RE9, "",
                "This is important.");
    }

    private void checkReplace(String re, String rp, String... expected) {
        List<String> result = RegexUtils.replace(re, rp, strings);
        assertEquals(Arrays.asList(expected), result);
    }

    private void check(String re, String... expected) {
        List<String> result = RegexUtils.grep(re, strings);
        assertEquals(Arrays.asList(expected), result);
    }

}
