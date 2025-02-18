package week2;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week2.intro.RegexUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextAnalyzerTest {

    @Test
    public void test01_last() {
        List<String> input = Arrays.asList("cred", "schred!", "bedding", "ed", "eddard", "edx");
        check(TextAnalyzer.RE1, input, "cred", "schred!", "ed", "edx");
    }

    @Test
    public void test02_odd() {
        List<String> input = Arrays.asList("123", "11", "kala", "kalur", "pallur", "palluri", "!");
        check(TextAnalyzer.RE2, input, "123", "kalur", "palluri", "!");
    }

    @Test
    public void test03_beginend() {
        char c = (char) new Random().nextInt();
        char d = (char) new Random().nextInt();
        if (c == d) c++;
        List<String> input = Arrays.asList("aba", "ab", c + "ab" + c, c + "ab" + d, "aab", "baa", "aa", "a");
        check(TextAnalyzer.RE3, input, "aba", c + "ab" + c, "aa", "a");
    }

    @Test
    public void test04_nimed() {
        List<String> input = Arrays.asList("John", "Kalle Kulbok", "vesal vojdani", "Mari maasiKas", "J Jubin");
        check(TextAnalyzer.NAME, input, "Kalle Kulbok", "J Jubin");
    }

    @Test
    public void test05_numbrideasy() {
        List<String> input = Arrays.asList("42312345", "1234-4444", "1234567", "12-343-33", "123-456", "1234+3534");
        check("^("+ TextAnalyzer.NUMBER + ")$", input, "42312345", "1234-4444", "1234567", "123-456");
    }

    @Test
    public void test06_numbrid() {
        List<String> input = Arrays.asList("42312345", "1234-4444", "123", "12-265", "1234567", "12345678",
                "123456789", "1234-567", "123-4567", "123-456", "1234-5678", "12345-6789", "123- 456", "123 -456", "1234", "12345");
        check("^("+ TextAnalyzer.NUMBER + ")$", input, "42312345", "1234-4444", "1234567", "12345678", "1234-567", "123-4567", "123-456", "1234-5678", "1234", "12345");
    }


    @Test
    public void test07_getPhoneNumbers() {
        checkPhoneNumber("Mart Laar, tel: 42312345, e-mail:laar@laar.ee", "Mart Laar","42312345");
        checkPhoneNumber("Suvaline teks ja *+*asf,, Jaak Saar veel miskit +afV+++*., 1234-4444.", "Jaak Saar","12344444");
    }

    @Test
    public void test08_anonymize() {
        checkAnon("Mart Laar, tel: 42312345, e-mail:laar@laar.ee", "<nimi>, tel: <telefoninumber>, e-mail:laar@laar.ee");
        checkAnon("Suvaline teks ja *+*asf,, Jaak Saar veel miskit +afV+++*., 1234-4444.", "Suvaline teks ja *+*asf,, <nimi> veel miskit +afV+++*., <telefoninumber>.");
    }

    @Test
    public void test09_misc() {
        checkAnon("Mart Laar   42312345", "<nimi>   <telefoninumber>");
        checkAnon("Ma La   42312345.", "<nimi>   <telefoninumber>.");
        checkPhoneNumber("Ma La   42312345.", "Ma La", "42312345");
        checkPhoneNumber("Ma La   423 2345.", "Ma La", "4232345");
        checkPhoneNumber("Hai Kala, sündinud 80ndatel, saab kätte numbril 423 2345.", "Hai Kala", "4232345");
        checkPhoneNumber("Karu Kalle, iskukoodiga 38205937492, telefoninumber on 3244-8589.", "Karu Kalle", "32448589");
    }

    @Test
    public void test10_demoCase() {
        TextAnalyzer ta = new TextAnalyzer(
                """
                        Mina olen Kalle Kulbok ja mu telefoninumber on 5556 4272.
                        Mina olen Peeter Peet ja mu telefoninumber on 5234 567.
                        Mari Maasikas siin, mu number on 6723 3434. Tere, olen Jaan Jubin numbriga 45631643.""");

        Map<String, String> phoneBook = ta.getPhoneNumbers();
        assertEquals("55564272", phoneBook.get("Kalle Kulbok"));
        assertEquals("5234567", phoneBook.get("Peeter Peet"));
        assertEquals("67233434", phoneBook.get("Mari Maasikas"));
        assertEquals("45631643", phoneBook.get("Jaan Jubin"));

        assertEquals("""
                        Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
                        Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
                        <nimi> siin, mu number on <telefoninumber>. Tere, olen <nimi> numbriga <telefoninumber>.""",
                ta.anonymize());
    }


    private void checkPhoneNumber(String input, String nimi, String expected) {
        TextAnalyzer ta = new TextAnalyzer(input);
        String actual = ta.getPhoneNumbers().get(nimi);
        assertEquals(input, expected, actual);
    }

    private void checkAnon(String input, String expected) {
        TextAnalyzer ta = new TextAnalyzer(input);
        String actual = ta.anonymize();
        assertEquals(input, expected, actual);
    }

    private void check(String re, List<String> input, String... expected) {
        List<String> result = RegexUtils.grep(re, input);
        assertEquals(Arrays.asList(expected), result);
    }
}
