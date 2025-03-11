package week4.regex;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week4.regex.ast.RegexNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegexAnalyzerTest {


    @Test
    public void test01_matchesEmptyWordOperators() {
        checkMatchesEmpty("ε", true);
        checkMatchesEmpty("a", false);
        checkMatchesEmpty("ε|a", true);
        checkMatchesEmpty("εa", false);
        checkMatchesEmpty("a*", true);
    }

    @Test
    public void test02_matchesEmptyWordSimpleCombinations() {
        checkMatchesEmpty("a*|ε", true);
        checkMatchesEmpty("aε*|a", false);
        checkMatchesEmpty("(a|(b|ε))*", true);
        checkMatchesEmpty("εεε", true);
    }


    @Test
    public void test03_GetFirst()  {
        checkFirst("a", 'a');
        checkFirst("ab", 'a');
        checkFirst("a|b", 'a', 'b');
        checkFirst("(a|b)c", 'a', 'b');
        checkFirst("(a|b)*", 'a', 'b');
        checkFirst("(a|b)*c", 'a', 'b', 'c');
        checkFirst("ε(a|b)*c", 'a', 'b', 'c');
    }

    @Test
    public void test04_getAllWordsAtoms() {
        checkFinite("ε", "");
        checkFinite("a", "a");
        checkInfinite("a*");
    }

    @Test
    public void test05_getAllWordsAlt() {
        checkFinite("ε", "");
        checkFinite("a|b", "a", "b");
        checkFinite("a|b|c", "a", "b", "c");
        checkFinite("a|b|ε", "a", "b", "");
        checkFinite("a|b|a", "a", "b");
        checkFinite("a|a|a", "a");
        checkFinite("a|(b|a)|b", "a", "b");
        checkInfinite("(a|b)*");
        checkInfinite("(a|b*)");
    }

    @Test
    public void test06_getAllWordsConcatSingleton() {
        checkFinite("ab", "ab");
        checkFinite("abcdefg", "abcdefg");
        checkInfinite("(ab)*");
    }

    @Test
    public void test07_getAllWordsConcatSets() {
        checkFinite("(a|b)(c|d)", "ac", "ad", "bc", "bd");

        checkFinite("(kala|äri)(mees|naine)",
                "kalamees", "kalanaine",
                "ärimees", "ärinaine");
    }

    @Test
    public void test08_getAllWordsEdgecases() {
        checkFinite("(a|b)(c|d)(e|f|g)",
                "ace", "acf", "acg",
                "ade", "adf", "adg",
                "bce", "bcf", "bcg",
                "bde", "bdf", "bdg");

        checkFinite("(kala|äri)(mees|naine|ε)",
                "kalamees", "kalanaine", "kala",
                "ärimees", "ärinaine", "äri");

        checkFinite("ε*", "");
        checkFinite("((ε*)*)*", "");
    }


    private void checkMatchesEmpty(String originalRegex, boolean expected) {

        RegexNode originalTree = RegexParser.parse(originalRegex);
        boolean actualResult = RegexAnalyzer.matchesEmptyWord(originalTree);

        assertEquals(originalRegex + " oodatud: " + expected, expected, actualResult);
    }

    private void checkFirst(String regex, Character... expected) {
        Set<Character> expectedSet = new HashSet<>(Arrays.asList(expected));
        Set<Character> actualSet = RegexAnalyzer.getFirst(regex);
        assertEquals(expectedSet, actualSet);
    }

    private void checkInfinite(String regex) {
        try {
            RegexAnalyzer.getAllWords(RegexParser.parse(regex));
            fail("Should have thrown exception for infinite language");
        }
        catch (Exception e) {
            // OK
        }
    }

    private void checkFinite(String regex, String... expectedWordsArray) {
        RegexNode tree = RegexParser.parse(regex);
        Set<String> expectedWords = new HashSet<>(Arrays.asList(expectedWordsArray));
        Set<String> actualWords = null;
        try {
            actualWords = RegexAnalyzer.getAllWords(tree);
        } catch (Exception e) {
            fail("Ei oleks tohtinud erindi viskama. Sisend: " + regex);
        }
        assertEquals(expectedWords, actualWords);
    }
}
