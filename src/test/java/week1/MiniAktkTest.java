package week1;

import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import utils.MainRunner;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MiniAktkTest {
    @ClassRule public static TestRule classTimeout = new DisableOnDebug(new Timeout(30, TimeUnit.SECONDS));
    @Rule public TestRule timeout = new DisableOnDebug(new Timeout(5, TimeUnit.SECONDS));

    public static String lastTestDescription = "";

    protected Class<?> getClassToTest() {
        return MiniAktk.class;
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test01_basicPrint() throws IOException {
        check("print 3", "3");
        check("print 7", "7");
        check("print 8753", "8753");
    }

    @Test
    public void test02_singlePlusOrSingleMinus() throws IOException {
        check("print 3 + 2", "5");
        check("print 14+0", "14");
        check("print 103 + 5", "108");
        check("print 103+5", "108");
        check("print 103 +  5", "108");
        check("print 30 - 2", "28");
        check("print 3 - 15", "-12");
        check("print 3-15", "-12");
        check("print             3-           15   ", "-12");
    }

    @Test
    public void test03_severalPrints() throws IOException {
        check("""
                print 3
                print 4
                """, "3\n4");
    }

    @Test
    public void test04_plusAndMinus() throws IOException {
        check("print 3 + 2 - 40", "-35");
        check("print 1+2+3+4+5+6+7+8+9+10+11-11-10-9-8-7-6-5-4", "6");
    }

    @Test
    public void test05_simpleVariables() throws IOException {
        check(    "x=34\n"
                + "print x", "34");

        check(    "p=34\n"
                + "print p", "34");

        check("""
                x=34
                p=4
                print p
                print x""", "4\n34");
    }

    @Test
    public void test06_variablesAndExpressions() throws IOException {
        check("""
                x=34
                p=x-x+1
                print p""", "1");

        check("""
                x=34
                p=x-x+1
                print p + p + x""", "36");

        check("""
                X=34
                p=X-X+1
                print p""", "1");

        check("""
                x=34
                P=x-x+1
                print P + P + x""", "36");
    }

    @Test
    public void test07_commentsAndEmptyLines() throws IOException {
        check("""
                x=34
                p=x-x+1

                # kommentaar

                # kommentaar
                print p""", "1");

        check("""
                x=34
                p=x-x+1 # kommentaar
                print p + p + x # kommentaar""", "36");
    }

    @Test
    public void test08_undefinedVariables() throws IOException {
        test05_simpleVariables();
        checkError("print x");
        checkError("print w");
        checkError("print x\n"
                +  "x = 3");
    }

    @Test
    public void test09_syntaxErrors() throws IOException {
        test06_variablesAndExpressions();
        checkError("kala=66");
        checkError("x=6.6");
        checkError("x=");
        checkError("x=3+");
        checkError("x=3-");
        checkError("print");
        checkError("print 3+");
        checkError("print 3-");
        checkError("prnt 3");
    }

    @Test
    public void test10_moreErrors() throws IOException {
        test06_variablesAndExpressions();
        checkError("print 3x");
        checkError("print x3");
        checkError("print8");
        checkError("print 3 3");
        checkError("5=1");
        checkError("_=1");
        checkError("""
                x=34
                5=x-x+1
                print 5""");
    }


    private void checkError(String program) throws IOException {
        lastTestDescription = "Programm: \n>" + quoteTextBlock(program);

        Path path = createFile(program);
        MainRunner.ExecutionResult result = runJavaProgramViaClassReload(getClassToTest(), path.toString());

        // stderr voos peaks olema veateade
        if (result.err.isEmpty()) {
            fail("Ootasin veateadet, aga seda polnud. Väljund oli " + result.out);
        }

        lastTestDescription = "";
    }

    private void check(String program, String expectedOutput) throws IOException {
        lastTestDescription = "Programm: \n>" + quoteTextBlock(program);

        Path path = createFile(program);
        MainRunner.ExecutionResult result = runJavaProgramViaClassReload(getClassToTest(), path.toString());

        // Väljundi kontrollimisel pean arvestama, et Windowsi reavahetus on \r\n, aga mujal on \n.
        // Samuti tahan ma olla paindlik selle suhtes, kas väljundi lõpus on reavahetus või mitte.
        expectedOutput = expectedOutput.replace("\r\n", "\n").replaceFirst("\\n$", "");
        String actualOutput = result.out.replace("\r\n", "\n").replaceFirst("\\n$", "");

        if (!expectedOutput.equals(actualOutput)) {
            fail("Ootasin väljundit\n" + quoteTextBlock(expectedOutput)
                + ", aga väljund oli\n" + quoteTextBlock(actualOutput));
        }
        assertEquals(expectedOutput, actualOutput);

        // stderr voog peaks olema tühi, st. programm ei tohiks anda vigu
        assertTrue(result.err.isEmpty());

        lastTestDescription = "";
    }

    private String quoteTextBlock(String s) {
        return "\n>" + s.replace("\n", "\n>") + "\n";
    }

    private Path createFile(String content) throws IOException {
        Path path = temporaryFolder.newFile().toPath();
        Files.writeString(path, content);
        return path;
    }

    public static MainRunner.ExecutionResult runJavaProgramViaClassReload(Class<?> clazz, String... args) throws IOException {
        return runJavaProgramWithInput(clazz, "", args);
    }

    private static MainRunner.ExecutionResult runJavaProgramWithInput(Class<?> clazz, String input, String... args) throws IOException {
        URL kotlinUrl = kotlin.jvm.internal.Intrinsics.class.getProtectionDomain().getCodeSource().getLocation();
        MainRunner mainRunner = new MainRunner(clazz.getName(), clazz, Collections.singletonList(kotlinUrl));
        return mainRunner.runJavaClass(input, args);
    }
}
