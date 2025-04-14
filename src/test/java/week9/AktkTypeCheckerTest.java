package week9;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import week7.AktkAst;
import week7.ast.AstNode;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkTypeCheckerTest {
    public static String lastTestDescription = "";

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    @Test
    public void test01_expressions() {
        legal("3");
        legal("\"tere\"");
        legal("1 + 2");
        illegal("\"tere\" / \"kere\"");
        illegal("\"tere\" == 5");
    }

    @Test
    public void test02_variables() {
        legal("var x : Integer = 5");
        legal("var x : String = \"tere\"");
        illegal("var x : Integer = \"tere\"");
        illegal("var x : String = 5");
        illegal("var x : Taisarv = 1");
    }

    @Test
    public void test03_assignments() {
        legal("var x: Integer; x = 1");
        illegal("var x : String; x = 1");
    }

    @Test
    public void test04_typeInference() {
        legal("var x : Integer");
        legal("var x:Integer = 12");
        legal("var x = 3");
        legal("var x = 3; x = 7");
        illegal("var x:Integer = \"tere\"");
        illegal("var y");
    }

    @Test
    public void test05_ifStatements() {
        legal("if 0 then 0 else 1");
        illegal("if \"foo\" then 0 else 1");
        illegal("if 0 then \"a\"+1 else 1");
        illegal("if 0 then 0 else \"a\"+1");
    }

    @Test
    public void test06_whileStatements() {
        legal("while 0 do 1");
        illegal("while \"foo\" do 1");
        illegal("while 0 do \"a\"+1");
    }

    @Test
    public void test07_functionCallArguments() {
        legal("print(1)");
        legal("print(\"1\")");
        legal("print(\"tere\")");
        illegal("print(1,2,3)");
        illegal("print(1,2)");
        illegal("print()");
    }

    @Test
    public void test08_functionCallReturns() {
        legal("var x: String = input(\"palun nimi: \")");
        illegal("var x: Integer = input(\"palun nimi: \")");
    }

    @Test
    public void test09_nestedFunctionCalls() {
        legal("print(lower(\"AKTK\"))");
        legal("print(upper(input()))");
        legal("print(gcd(1, stringToInteger(\"3\")))");
        illegal("print(gcd(1, \"a\"))");
        illegal("print(power(2, integerToString(3)))");
        illegal("print(print(1))");
    }

    @Test
    public void test10_functionDefinitions() {
        legal("fun f1(a:Integer, b:Integer) -> Integer {return a + b}; print(f1(3,4))");
        legal("fun f1(a:String) -> Integer {print(a); return 0}; f1(\"tere\")");
        legal("fun f1(a:String) {print(a)}; f1(\"tere\"); f1(\"\")");
        illegal("fun f1(a:Integer, b:Integer) -> Integer {return a + b}; var x: String = f1(3,4)");
        illegal("fun f1(a:String) -> String {print(a); return 1}; f1(3)");
        illegal("fun f1(a:Integer, b:Integer) -> Integer {return \"tere\"}; print(f1(3,4))");
    }

    private void legal(String program) {
        check(program, false);
    }

    private void illegal(String program) {
        check(program, true);
    }

    public void check(String program, boolean hasErrors) {
        lastTestDescription = "Programm: " + monospaceBlock(program);

        try {
            AstNode ast = AktkAst.createAst(program);
            AktkTypeChecker.check(ast);
            if (hasErrors) {
                fail("Programm on vigane, aga check ei visanud erindit");
            }
        } catch (Exception e) {
            if (!hasErrors) {
                e.printStackTrace();
                fail("Programm ei ole vigane, aga check viskas erindi: " + e);
            }

        }
    }

    private static String monospaceBlock(String s) {
        return "\n>" + s.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\n>") + "\n";
    }
}
