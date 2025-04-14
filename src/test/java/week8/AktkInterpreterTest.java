package week8;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkInterpreterTest {
    public static String lastTestDescription = "";

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(500, TimeUnit.MILLISECONDS));

    @Test
    public void test01_simplePrints() {
        check("print(3)", "3\n");
        check("print(3+2)", "5\n");
        check("print((2*4)/(3-1))", "4\n");
        check("print(\"tere\")", "tere\n");
    }

    @Test
    public void test02_variables() {
        check("var x = 23; var y = 11; x = 13; print(x+y)", "24\n");
    }

    @Test
    public void test03_ifStatements() {
        check("if (1 == 1) then print(1) else print(2)", "1\n");
        check("if (1 != 1) then print(1) else print(2)", "2\n");
    }

    @Test
    public void test04_nestedIfStatements() {
        check("if (1 != 1) then print(1) else if (2==3) then print(2) else print(\"X\")", "X\n");
    }

    @Test
    public void test05_whileStatements() {
        check("var i = 3; while (i > 0) do {print(i); i = i-1}", "3\n2\n1\n");
        check("var x = 3; while (x > 0) do {print(x); x = x-1}", "3\n2\n1\n");
    }

    @Test
    public void test06_whileIfStatements() {
        check("var i = 30; while (i > 0) do {if (i < 4) then print(i) else 0; i = i-1}", "3\n2\n1\n");
        check("var x = 30; while (x > 0) do {if (x < 4) then print(x) else 0; x = x-1}", "3\n2\n1\n");
    }

    @Test
    public void test07_builtinFunctions() {
        check("var s = input(); print(upper(s))", "kala", "KALA\n");
        check("print(power(2,3))", "8\n");
        check("print(power(2,4))", "16\n");
    }

    @Test
    public void test08_aktkFunctions() {
        check("fun snd(x:Integer, y:Integer) -> Integer {return y}", "");
        check("fun snd(x:Integer, y:Integer) -> Integer {return y}; snd(3,42)", "");
        check("fun snd(x:Integer, y:Integer) -> Integer {return y}; print(snd(3,42))", "42\n");
        check("fun snd(x:Integer, z:Integer) -> Integer {return z}; print(snd(3,42))", "42\n");
        check("fun min(x:Integer, y:Integer) -> Integer {if (x > y) then return y else return x}; print(min(3, 100))", "3\n");
        check("fun min(x:Integer, y:Integer) -> Integer {if (x > y) then return y else return x}; print(min(3, 1))", "1\n");
    }

    @Test
    public void test09_scopes() {
        check("var y = 1; fun snd(x:Integer, y:Integer) -> Integer {return y}; snd(3,42)", "");
        check("var y = 1; fun snd(x:Integer, y:Integer) -> Integer {return y}; print(snd(3,42))", "42\n");
        check("var y = 1; fun snd(x:Integer, z:Integer) -> Integer {return y}; print(snd(3,42))", "1\n");
        check("var y = 1; if (1 > 2) then {var y = 2; print(y)} else {print(y)}", "1\n");
        check("var y = 1; if (1 < 2) then {var y = 2; print(y)} else {print(y)}", "2\n");
        check("var y = 1; if (1 < 2) then {y = 2; print(y)} else {print(y)}; print(y)", "2\n2\n");
        check("var y = 1; if (1 < 2) then {var y = 2} else 0; print(y)", "1\n");
    }

    @Test
    public void test10_recursion() {
        check("fun fact(x:Integer) -> Integer { if (x<=1) then return x else return x * fact(x-1) }; print(fact(6))", "720\n");
        check("fun fact(x:Integer, res:Integer) -> Integer { if (x<=1) then return res else return fact(x-1, res*x) }; print(fact(7,1))", "5040\n");
        check("fun sum(x:Integer) -> Integer {if x > 0 then return (sum(x-1)+x) else return 0}; print(sum(3))", "6\n");
    }


    private void check(String program, String expectedOutput) {
        check(program, "", expectedOutput);
    }

    private void check(String program,
                       String input,
                       String expectedOutput) {

        lastTestDescription = "Programm: " + monospaceBlock(program)
                + "\nSisend: " + monospaceBlock(input);

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;


        try {
            // Suuna ümber System.out
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            // Suuna ümber System.in
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            AktkInterpreter.run(program);

            String actualOutput = outputStream.toString().replaceAll("\\r\\n", "\n");
            if (!actualOutput.equals(expectedOutput)) {
                fail("Ootasin väljundit" + monospaceBlock(expectedOutput)
                        + "\naga tuli"   + monospaceBlock(actualOutput));
            }
        }
        finally {
            // Taasta algsed vood
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    private static String monospaceBlock(String s) {
        return "\n>" + s.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\n>") + "\n";
    }
}
