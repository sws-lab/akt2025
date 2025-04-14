package week10;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import utils.MainRunner;
import week9.AktkBinding;
import week7.AktkAst;
import week7.ast.AstNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkCompilerTest {
    public static String lastTestDescription = "";
    public static String successMessage = "kompileeritud programm töötas õigesti!";

    protected static final Path AKTK_OUT = Paths.get("aktk-out");

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    @Test
    public void test01_expressions() {
        testProgram("1", "test01", "", "");
    }

    @Test
    public void test02_variables() {
        testProgram("var x = 1", "test02", "", "");
    }

    @Test
    public void test03_output() {
        testProgram("print(42)", "test03", "", "42\n");
    }

    @Test
    public void test04_input() {
        testProgram("var x = readInt(); print(x)", "test04", "127", "127\n");
    }

    @Test
    public void test05_successor() {
        Map<String, String> ioPairs = new HashMap<>();
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            int x = r.nextInt(888);
            ioPairs.put(Integer.toString(x), (x + 1) + "\n");
        }

        testProgram("var x = readInt(); print(x+1)", "test05", ioPairs);
    }

    @Test
    public void test06_whileStatements() {
        testProgram("""
                var n = readInt();
                while n > 0 do {
                   print(n*n);   n = n - 1}
                """, "test06", "4\n", "16\n9\n4\n1\n");
    }

    @Test
    public void test07_ifStatements() {
        Map<String, String> ioPairs = new HashMap<>();
        ioPairs.put("24\n", "4\n");
        ioPairs.put("6\n", "2\n");
        ioPairs.put("7\n", "1\n");

        testProgram("""
                var n = readInt();
                if n % 2 == 0 then {
                   if (n / 2) % 2 == 0 then print(4)
                   else print(2)} else print(1)\s
                """, "test07", ioPairs);
    }

    @Test
    public void test08_builtins() {
        Map<String, String> ioPairs = new HashMap<>();
        ioPairs.put("234\n12\n", "6\n6\n");
        ioPairs.put("67\n7\n", "1\n1\n");

        testProgram("""
                var a = readInt();\r
                var b = readInt();\r
                \r
                /* Arvutan suurima ühisteguri "standardteegi" abil */\r
                print(gcd(a,b));\r
                \r
                \r
                /* Nüüd arvutan sama asja aktk vahenditega */\r
                while b > 0 do {\r
                    var c = a % b;\r
                    a = b;\r
                    b = c\r
                };\r
                \r
                print(a)""", "test08", ioPairs);
    }

    @Test
    public void test09_interactive() {
        Map<String, String> ioPairs = new HashMap<>();
        ioPairs.put("234\n12\n0\n", "1\n1\n0\n");
        ioPairs.put("0\n", "0\n");
        ioPairs.put("-5\n0\n", "-1\n0\n");

        testProgram("""
                /* Küsib standardsisendist täisarvu ja väljastab selle märgi.\r
                   Kordab seda niikaua kuni sisestatakse 0 */\r
                   \r
                var x = readInt();\r
                \r
                while x != 0 do {\r
                    if x > 0 then print(1) else print(-1);\r
                    x = readInt()\r
                };\r
                \r
                print(x)""", "test09", ioPairs);
    }

    @Test
    public void test10_scopes() {
        Map<String, String> ioPairs = new HashMap<>();
        ioPairs.put("4\n", "4\n4\n");
        ioPairs.put("5\n", "1\n5\n");

        testProgram("""
                var x = readInt();

                if x % 2 == 0 then {
                    print(x)
                } else {
                    var x = 1;
                    print(x)
                };

                print(x)""", "test10", ioPairs);
    }

    protected void testProgram(String aktkSource, String className,
                             String input, String expectedOutput) {
        Map<String, String> ioPairs = new HashMap<>();
        ioPairs.put(input, expectedOutput);
        testProgram(aktkSource, className, ioPairs);
    }

    protected void testProgram(String aktkSource, String className,
                             Map<String, String> ioPairs) {
        lastTestDescription = "Katsetan sellist programmi:\n\n>"
                + aktkSource.replaceAll("\r\n", "\n").replaceAll("\n", "\n>");



        try {
            compileProgram(aktkSource, className);

            for (Map.Entry<String, String> ioPair : ioPairs.entrySet()) {
                String input = ioPair.getKey();
                String expectedOutput = ioPair.getValue();

                try {
                    MainRunner.ExecutionResult result = runJavaClass(className, input);

                    // Väljundi kontrollimisel pean arvestama, et Windowsi reavahetus on \r\n, aga mujal on \n.
                    // Samuti tahan ma olla paindlik selle suhtes, kas väljundi lõpus on reavahetus või mitte.
                    expectedOutput = expectedOutput.replace("\r\n", "\n").replaceFirst("\\n$", "");
                    String actualOutput = result.out.replace("\r\n", "\n").replaceFirst("\\n$", "");

                    if (!result.err.isEmpty()) {
                        fail("Sisendiga\n>" + input.replaceAll("\n", "\n>") + "\nandis programm vea: "
                                + result.err);
                    }
                    /*else if (result.returnCode != 0) {
                        fail("Sisendiga\n>" + input.replaceAll("\n", "\n>") + "\nandis programm veakoodi "
                                + result.returnCode);
                    }*/
                    else if (!actualOutput.equals(expectedOutput)) {
                        fail("Sisendiga\n>" + input.replaceAll("\n", "\n>") + "\npidi tulema väljund\n>"
                                + expectedOutput.replaceAll("\n", "\n>") + "\naga tuli\n>"
                                + actualOutput.replaceAll("\n", "\n>"));
                    }
                }
                catch (Exception e) {
                    fail("Sisendiga\n" + input.replaceAll("\n", "\n>") + "\nsain vea:" + e);
                }
            }
            // Kui testid läbisid, siis kustutame ka klassi ära:
            //Files.deleteIfExists(AKTK_OUT.resolve(className + ".class"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Kompileerimisel sain vea: " + e);
        }
    }

    protected void compileProgram(String aktkSource, String className) throws IOException {
        AstNode ast = AktkAst.createAst(aktkSource);
        AktkBinding.bind(ast);
        byte[] bytes = AktkCompiler.createClass(ast, className);
        Files.createDirectories(AKTK_OUT);
        Files.write(AKTK_OUT.resolve(className + ".class"), bytes);
    }

    public static MainRunner.ExecutionResult runJavaClass(String className, String input, String... args) throws IOException {
        MainRunner mainRunner = new MainRunner(className, AktkCompilerBuiltins.class, Collections.singletonList(AKTK_OUT.toUri().toURL()));
        return mainRunner.runJavaClass(input, args);
    }
}
