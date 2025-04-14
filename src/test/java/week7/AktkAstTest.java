package week7;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week7.ast.AstNode;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkAstTest {

    @Test
    public void test01_variables() {
        legal("a", "Block(ExpressionStatement(Variable(\"a\")))");
        legal("abc", "Block(ExpressionStatement(Variable(\"abc\")))");
        legal("AcB", "Block(ExpressionStatement(Variable(\"AcB\")))");
        legal("A_234_c", "Block(ExpressionStatement(Variable(\"A_234_c\")))");
        legal("A___", "Block(ExpressionStatement(Variable(\"A___\")))");
        legal("Ab2", "Block(ExpressionStatement(Variable(\"Ab2\")))");

        illegal("2Ab2");
        illegal("Ab$");
        illegal("@");
        illegal(".");
    }

    @Test
    public void test02_literals() {
        // integers
        legal("0", "Block(ExpressionStatement(IntegerLiteral(0)))");
        legal("1", "Block(ExpressionStatement(IntegerLiteral(1)))");
        legal("21", "Block(ExpressionStatement(IntegerLiteral(21)))");
        legal("986234276", "Block(ExpressionStatement(IntegerLiteral(986234276)))");

        illegal("07");

        // strings
        legal("\"\"", "Block(ExpressionStatement(StringLiteral(\"\")))");
        legal("\" \"", "Block(ExpressionStatement(StringLiteral(\" \")))");
        legal("\"tere\"", "Block(ExpressionStatement(StringLiteral(\"tere\")))");
        legal("\"1234\"", "Block(ExpressionStatement(StringLiteral(\"1234\")))");
        legal("\"tere vana kere!\"", "Block(ExpressionStatement(StringLiteral(\"tere vana kere!\")))");
        legal("\" -- Tere\tVana kere ,.;\\/123\"", "Block(ExpressionStatement(StringLiteral(\" -- Tere\\tVana kere ,.;\\\\/123\")))");

        illegal("'Tere vana kere'");
        illegal("\"");
    }

    @Test
    public void test03_operations() {
        legal("a+b", "Block(ExpressionStatement(FunctionCall(\"+\", Variable(\"a\"), Variable(\"b\"))))");
        legal("a*b", "Block(ExpressionStatement(FunctionCall(\"*\", Variable(\"a\"), Variable(\"b\"))))");
        legal("10/0", "Block(ExpressionStatement(FunctionCall(\"/\", IntegerLiteral(10), IntegerLiteral(0))))");
        legal("10-5", "Block(ExpressionStatement(FunctionCall(\"-\", IntegerLiteral(10), IntegerLiteral(5))))");
        legal("3+3/4", "Block(ExpressionStatement(FunctionCall(\"+\", IntegerLiteral(3), FunctionCall(\"/\", IntegerLiteral(3), IntegerLiteral(4)))))");
        legal("3-10/0", "Block(ExpressionStatement(FunctionCall(\"-\", IntegerLiteral(3), FunctionCall(\"/\", IntegerLiteral(10), IntegerLiteral(0)))))");
        legal("4+2+3+4", "Block(ExpressionStatement(FunctionCall(\"+\", FunctionCall(\"+\", FunctionCall(\"+\", IntegerLiteral(4), IntegerLiteral(2)), IntegerLiteral(3)), IntegerLiteral(4))))");
        legal("12/0/0/0", "Block(ExpressionStatement(FunctionCall(\"/\", FunctionCall(\"/\", FunctionCall(\"/\", IntegerLiteral(12), IntegerLiteral(0)), IntegerLiteral(0)), IntegerLiteral(0))))");
        legal("a+b+c-3", "Block(ExpressionStatement(FunctionCall(\"-\", FunctionCall(\"+\", FunctionCall(\"+\", Variable(\"a\"), Variable(\"b\")), Variable(\"c\")), IntegerLiteral(3))))");
        legal("3+kol_pol + 4-(32-12)", "Block(ExpressionStatement(FunctionCall(\"-\", FunctionCall(\"+\", FunctionCall(\"+\", IntegerLiteral(3), Variable(\"kol_pol\")), IntegerLiteral(4)), FunctionCall(\"-\", IntegerLiteral(32), IntegerLiteral(12)))))");
        legal("a > b", "Block(ExpressionStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\"))))");
        legal("3+3/4 > x + 44-x", "Block(ExpressionStatement(FunctionCall(\">\", FunctionCall(\"+\", IntegerLiteral(3), FunctionCall(\"/\", IntegerLiteral(3), IntegerLiteral(4))), FunctionCall(\"-\", FunctionCall(\"+\", Variable(\"x\"), IntegerLiteral(44)), Variable(\"x\")))))");
        legal("a > (b > c)", "Block(ExpressionStatement(FunctionCall(\">\", Variable(\"a\"), FunctionCall(\">\", Variable(\"b\"), Variable(\"c\")))))");
        legal("(a >= b) <= c", "Block(ExpressionStatement(FunctionCall(\"<=\", FunctionCall(\">=\", Variable(\"a\"), Variable(\"b\")), Variable(\"c\"))))");
        legal("(x != y) == true", "Block(ExpressionStatement(FunctionCall(\"==\", FunctionCall(\"!=\", Variable(\"x\"), Variable(\"y\")), Variable(\"true\"))))");
        legal("((((((((3+4))))))))", "Block(ExpressionStatement(FunctionCall(\"+\", IntegerLiteral(3), IntegerLiteral(4))))");
        legal("3 + (2*(3))", "Block(ExpressionStatement(FunctionCall(\"+\", IntegerLiteral(3), FunctionCall(\"*\", IntegerLiteral(2), IntegerLiteral(3)))))");
        legal("3 % 2", "Block(ExpressionStatement(FunctionCall(\"%\", IntegerLiteral(3), IntegerLiteral(2))))");

        illegal("a > 8 == 1");
        illegal("x == y == z");
        illegal("3++12");
        illegal("3/12/");
        illegal("3 (+ 2)");
        illegal("3 + (2");
        illegal("3 + (2))");
        illegal("()"); // tühjad sulud on lubatud ainult funktsiooni väljakutses
        illegal("[2+3]");
    }

    @Test
    public void test04_functionCalls() {
        legal("print()", "Block(ExpressionStatement(FunctionCall(\"print\")))");
        legal("sin(3)", "Block(ExpressionStatement(FunctionCall(\"sin\", IntegerLiteral(3))))");
        legal("sin(x)", "Block(ExpressionStatement(FunctionCall(\"sin\", Variable(\"x\"))))");
        legal("(cos(y))", "Block(ExpressionStatement(FunctionCall(\"cos\", Variable(\"y\"))))");
        legal("x * -sin(3)", "Block(ExpressionStatement(FunctionCall(\"*\", Variable(\"x\"), FunctionCall(\"-\", FunctionCall(\"sin\", IntegerLiteral(3))))))");
        legal("sin(x) * cos(y)", "Block(ExpressionStatement(FunctionCall(\"*\", FunctionCall(\"sin\", Variable(\"x\")), FunctionCall(\"cos\", Variable(\"y\")))))");
        legal("print(a+b)", "Block(ExpressionStatement(FunctionCall(\"print\", FunctionCall(\"+\", Variable(\"a\"), Variable(\"b\")))))");
        legal("print(a+b, sin(sin(x)), 8)", "Block(ExpressionStatement(FunctionCall(\"print\", FunctionCall(\"+\", Variable(\"a\"), Variable(\"b\")), FunctionCall(\"sin\", FunctionCall(\"sin\", Variable(\"x\"))), IntegerLiteral(8))))");
        legal("priNt_ln (     )", "Block(ExpressionStatement(FunctionCall(\"priNt_ln\")))");
        legal("print(/*argumente pole*/)", "Block(ExpressionStatement(FunctionCall(\"print\")))");

        illegal("print(123");
        illegal("print 123");
        illegal("print 123)");
    }

    @Test
    public void test05_variableDeclarations() {
        legal("x=a+b+c-3", "Block(Assignment(\"x\", FunctionCall(\"-\", FunctionCall(\"+\", FunctionCall(\"+\", Variable(\"a\"), Variable(\"b\")), Variable(\"c\")), IntegerLiteral(3))))");
        legal("x = print(x) + 234 == 22", "Block(Assignment(\"x\", FunctionCall(\"==\", FunctionCall(\"+\", FunctionCall(\"print\", Variable(\"x\")), IntegerLiteral(234)), IntegerLiteral(22))))");
        legal("var kala", "Block(VariableDeclaration(\"kala\", null, null))");
        legal("var x = 123", "Block(VariableDeclaration(\"x\", null, IntegerLiteral(123)))");
        legal("var nimi = \"Pedro\"", "Block(VariableDeclaration(\"nimi\", null, StringLiteral(\"Pedro\")))");
        legal("var kala = 123 * kkkk", "Block(VariableDeclaration(\"kala\", null, FunctionCall(\"*\", IntegerLiteral(123), Variable(\"kkkk\"))))");
        legal("var x = print(x)", "Block(VariableDeclaration(\"x\", null, FunctionCall(\"print\", Variable(\"x\"))))");

        illegal("print(x=234)");
        illegal("(x=234)");
        illegal("if x=3 then x else y");
        illegal("var x =");
    }

    @Test
    public void test06_blocks() {
        legal("x = a+b+c-3; print(x); print()", "Block(Assignment(\"x\", FunctionCall(\"-\", FunctionCall(\"+\", FunctionCall(\"+\", Variable(\"a\"), Variable(\"b\")), Variable(\"c\")), IntegerLiteral(3))), ExpressionStatement(FunctionCall(\"print\", Variable(\"x\"))), ExpressionStatement(FunctionCall(\"print\")))");
        legal("var nimi = \"Pedro\"; nimi = 33; print(nimi, 23)", "Block(VariableDeclaration(\"nimi\", null, StringLiteral(\"Pedro\")), Assignment(\"nimi\", IntegerLiteral(33)), ExpressionStatement(FunctionCall(\"print\", Variable(\"nimi\"), IntegerLiteral(23))))");
        legal("var kala; var kala", "Block(VariableDeclaration(\"kala\", null, null), VariableDeclaration(\"kala\", null, null))");
        legal("var kala = 123 * kkkk; kalal = kala; print(sin(34))", "Block(VariableDeclaration(\"kala\", null, FunctionCall(\"*\", IntegerLiteral(123), Variable(\"kkkk\"))), Assignment(\"kalal\", Variable(\"kala\")), ExpressionStatement(FunctionCall(\"print\", FunctionCall(\"sin\", IntegerLiteral(34)))))");
        legal("print(x)\n;kala;\n\tx = 123", "Block(ExpressionStatement(FunctionCall(\"print\", Variable(\"x\"))), ExpressionStatement(Variable(\"kala\")), Assignment(\"x\", IntegerLiteral(123)))");
        legal("{x=3}", "Block(Block(Assignment(\"x\", IntegerLiteral(3))))");
        legal("{x=3};{y=x}", "Block(Block(Assignment(\"x\", IntegerLiteral(3))), Block(Assignment(\"y\", Variable(\"x\"))))");

        illegal("print(x);kala;x = 123;");
        illegal("{x=3");
        illegal("x=3}");
    }

    @Test
    public void test07_controlStructures() {
        // without blocks
        legal("if a > b then a else b", "Block(IfStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\")), Block(ExpressionStatement(Variable(\"a\"))), Block(ExpressionStatement(Variable(\"b\")))))");
        legal("if a > b then\n\ta\nelse\n\tb","Block(IfStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\")), Block(ExpressionStatement(Variable(\"a\"))), Block(ExpressionStatement(Variable(\"b\")))))");
        legal("if a+34 > sin(345*pi) then\n\ta\nelse\n\tb", "Block(IfStatement(FunctionCall(\">\", FunctionCall(\"+\", Variable(\"a\"), IntegerLiteral(34)), FunctionCall(\"sin\", FunctionCall(\"*\", IntegerLiteral(345), Variable(\"pi\")))), Block(ExpressionStatement(Variable(\"a\"))), Block(ExpressionStatement(Variable(\"b\")))))");
        legal("if (a+34) > sin(345*pi) then\n\t(a)\nelse\n\tb", "Block(IfStatement(FunctionCall(\">\", FunctionCall(\"+\", Variable(\"a\"), IntegerLiteral(34)), FunctionCall(\"sin\", FunctionCall(\"*\", IntegerLiteral(345), Variable(\"pi\")))), Block(ExpressionStatement(Variable(\"a\"))), Block(ExpressionStatement(Variable(\"b\")))))");
        legal("if ((a+34) > sin(345*pi)) then\n\t(a)\nelse\n\tb", "Block(IfStatement(FunctionCall(\">\", FunctionCall(\"+\", Variable(\"a\"), IntegerLiteral(34)), FunctionCall(\"sin\", FunctionCall(\"*\", IntegerLiteral(345), Variable(\"pi\")))), Block(ExpressionStatement(Variable(\"a\"))), Block(ExpressionStatement(Variable(\"b\")))))");
        legal("while a > b do print(123*x)", "Block(WhileStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\")), Block(ExpressionStatement(FunctionCall(\"print\", FunctionCall(\"*\", IntegerLiteral(123), Variable(\"x\")))))))");
        legal("while a > b do\n\tprint(123*x)", "Block(WhileStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\")), Block(ExpressionStatement(FunctionCall(\"print\", FunctionCall(\"*\", IntegerLiteral(123), Variable(\"x\")))))))");
        legal("while (a > b) do print(123*x)", "Block(WhileStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\")), Block(ExpressionStatement(FunctionCall(\"print\", FunctionCall(\"*\", IntegerLiteral(123), Variable(\"x\")))))))");

        illegal("(if a > b then a else b)");
        illegal("if a > b then a; else b;");
        illegal("if a > b then print(a); else print b;");
        illegal("while a > b \n\tprint(123*x)");

        // with blocks
        legal("print(x);kala;x = 123; if true then false else true", "Block(ExpressionStatement(FunctionCall(\"print\", Variable(\"x\"))), ExpressionStatement(Variable(\"kala\")), Assignment(\"x\", IntegerLiteral(123)), IfStatement(Variable(\"true\"), Block(ExpressionStatement(Variable(\"false\"))), Block(ExpressionStatement(Variable(\"true\")))))");
        legal("if x==3 then {x} else {y}", "Block(IfStatement(FunctionCall(\"==\", Variable(\"x\"), IntegerLiteral(3)), Block(ExpressionStatement(Variable(\"x\"))), Block(ExpressionStatement(Variable(\"y\")))))");
        legal("if x==3 then {var x = 45; print(x)} else asdf", "Block(IfStatement(FunctionCall(\"==\", Variable(\"x\"), IntegerLiteral(3)), Block(VariableDeclaration(\"x\", null, IntegerLiteral(45)), ExpressionStatement(FunctionCall(\"print\", Variable(\"x\")))), Block(ExpressionStatement(Variable(\"asdf\")))))");
        legal("if x > y then x else y; if a > b then 1 else 0", "Block(IfStatement(FunctionCall(\">\", Variable(\"x\"), Variable(\"y\")), Block(ExpressionStatement(Variable(\"x\"))), Block(ExpressionStatement(Variable(\"y\")))), IfStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\")), Block(ExpressionStatement(IntegerLiteral(1))), Block(ExpressionStatement(IntegerLiteral(0)))))");
        legal("x = a+b+c-3; if x > 14 then print(x) else {print(y);print(kala)}", "Block(Assignment(\"x\", FunctionCall(\"-\", FunctionCall(\"+\", FunctionCall(\"+\", Variable(\"a\"), Variable(\"b\")), Variable(\"c\")), IntegerLiteral(3))), IfStatement(FunctionCall(\">\", Variable(\"x\"), IntegerLiteral(14)), Block(ExpressionStatement(FunctionCall(\"print\", Variable(\"x\")))), Block(ExpressionStatement(FunctionCall(\"print\", Variable(\"y\"))), ExpressionStatement(FunctionCall(\"print\", Variable(\"kala\"))))))");
        legal("while x < y do {x = x + 1; y = y - 1}; print(x)", "Block(WhileStatement(FunctionCall(\"<\", Variable(\"x\"), Variable(\"y\")), Block(Assignment(\"x\", FunctionCall(\"+\", Variable(\"x\"), IntegerLiteral(1))), Assignment(\"y\", FunctionCall(\"-\", Variable(\"y\"), IntegerLiteral(1))))), ExpressionStatement(FunctionCall(\"print\", Variable(\"x\"))))");
        legal("var i = 0; while i <= 10 do {print(i); i = i+1}; print(\"valmis!\")", "Block(VariableDeclaration(\"i\", null, IntegerLiteral(0)), WhileStatement(FunctionCall(\"<=\", Variable(\"i\"), IntegerLiteral(10)), Block(ExpressionStatement(FunctionCall(\"print\", Variable(\"i\"))), Assignment(\"i\", FunctionCall(\"+\", Variable(\"i\"), IntegerLiteral(1))))), ExpressionStatement(FunctionCall(\"print\", StringLiteral(\"valmis!\"))))");

        illegal("if a > b then {print(a);} else {print b;}");
    }

    @Test
    public void test08_large() {
        legal("""
                        var arv1 = int(input("Esimene arv: "));
                        var arv2 = int(input("Teine arv: "));

                        while arv1 > max(arv2, sin(arv1), sin(arv2)) do {
                            if arv2 == 0 then {
                                break
                            }
                            else {
                                print(arv1, arv2);
                                arv1 = randint(0, arv2)
                            }
                        };

                        print("tulemus: " + arv1);
                        print()
                        """,
                "Block(VariableDeclaration(\"arv1\", null, FunctionCall(\"int\", FunctionCall(\"input\", StringLiteral(\"Esimene arv: \")))), VariableDeclaration(\"arv2\", null, FunctionCall(\"int\", FunctionCall(\"input\", StringLiteral(\"Teine arv: \")))), WhileStatement(FunctionCall(\">\", Variable(\"arv1\"), FunctionCall(\"max\", Variable(\"arv2\"), FunctionCall(\"sin\", Variable(\"arv1\")), FunctionCall(\"sin\", Variable(\"arv2\")))), Block(IfStatement(FunctionCall(\"==\", Variable(\"arv2\"), IntegerLiteral(0)), Block(ExpressionStatement(Variable(\"break\"))), Block(ExpressionStatement(FunctionCall(\"print\", Variable(\"arv1\"), Variable(\"arv2\"))), Assignment(\"arv1\", FunctionCall(\"randint\", IntegerLiteral(0), Variable(\"arv2\"))))))), ExpressionStatement(FunctionCall(\"print\", FunctionCall(\"+\", StringLiteral(\"tulemus: \"), Variable(\"arv1\")))), ExpressionStatement(FunctionCall(\"print\")))");

        legal("""
                        /* Muutujate deklaratsioonid */
                        var palk = -990; var nimi = "Teele";
                        \s
                        /* Funktsiooni väljakutse */
                        print(nimi, palk);
                        \s
                        n = int(input("sisesta arv"));
                        \s
                        if n > 100 then {
                            print("norrmaalne!")
                        } else {
                            print("lahja!!")
                        };   /* NB! Ära unusta semikoolonit lausete vahel! */
                        \s
                        var i = 0;
                        \s
                        while i < n do {
                            if (i > (3)) then print(i) else pass;
                            i = i + 1
                        };
                        \s
                        print("The End!") /* viimase lause lõpus pole semikoolonit */""",
                "Block(VariableDeclaration(\"palk\", null, FunctionCall(\"-\", IntegerLiteral(990))), VariableDeclaration(\"nimi\", null, StringLiteral(\"Teele\")), ExpressionStatement(FunctionCall(\"print\", Variable(\"nimi\"), Variable(\"palk\"))), Assignment(\"n\", FunctionCall(\"int\", FunctionCall(\"input\", StringLiteral(\"sisesta arv\")))), IfStatement(FunctionCall(\">\", Variable(\"n\"), IntegerLiteral(100)), Block(ExpressionStatement(FunctionCall(\"print\", StringLiteral(\"norrmaalne!\")))), Block(ExpressionStatement(FunctionCall(\"print\", StringLiteral(\"lahja!!\"))))), VariableDeclaration(\"i\", null, IntegerLiteral(0)), WhileStatement(FunctionCall(\"<\", Variable(\"i\"), Variable(\"n\")), Block(IfStatement(FunctionCall(\">\", Variable(\"i\"), IntegerLiteral(3)), Block(ExpressionStatement(FunctionCall(\"print\", Variable(\"i\")))), Block(ExpressionStatement(Variable(\"pass\")))), Assignment(\"i\", FunctionCall(\"+\", Variable(\"i\"), IntegerLiteral(1))))), ExpressionStatement(FunctionCall(\"print\", StringLiteral(\"The End!\"))))");

        illegal("""
                /* Muutujate deklaratsioonid */
                var palk = -990; var nimi = "Teele";
                /* Funktsiooni valjakutse */ vigane kommentaar */
                print(nimi, palk);
                """);
    }

    @Test
    public void test09_types() {
        legal("var x", "Block(VariableDeclaration(\"x\", null, null))");
        legal("var x: Integer", "Block(VariableDeclaration(\"x\", \"Integer\", null))");
        legal("var x: Suvatyyp", "Block(VariableDeclaration(\"x\", \"Suvatyyp\", null))");
        legal("var x: Integer = 1", "Block(VariableDeclaration(\"x\", \"Integer\", IntegerLiteral(1)))");
        legal("fun foo() {1}", "Block(FunctionDefinition(\"foo\", null, Block(ExpressionStatement(IntegerLiteral(1)))))");
        legal("fun snd(x:Integer, y:Integer) -> Integer {return y}", "Block(FunctionDefinition(\"snd\", FunctionParameter(\"x\", \"Integer\"), FunctionParameter(\"y\", \"Integer\"), \"Integer\", Block(ReturnStatement(Variable(\"y\")))))");

        illegal("var x:");
        illegal("var x Integer");
        illegal("var x: Integer =");
        illegal("var x: Integer 1");
        illegal("print(x:Integer)");
        illegal("print(x):Integer");
        illegal("fun snd(x, y) -> Integer {return y}");
        illegal("fun snd(x:Integer, y:Integer) -> {return y}");
        illegal("fun snd(x:Integer, y:Integer) Integer {return y}");
    }

    @Test
    public void test10_pitfalls() {
        // Vead, millest ei taha palju punkte maha võtta
        legal("----c", "Block(ExpressionStatement(FunctionCall(\"-\", FunctionCall(\"-\", FunctionCall(\"-\", FunctionCall(\"-\", Variable(\"c\")))))))");
        legal("--(--c + 23)", "Block(ExpressionStatement(FunctionCall(\"-\", FunctionCall(\"-\", FunctionCall(\"+\", FunctionCall(\"-\", FunctionCall(\"-\", Variable(\"c\"))), IntegerLiteral(23))))))");
        legal("2 * -x", "Block(ExpressionStatement(FunctionCall(\"*\", IntegerLiteral(2), FunctionCall(\"-\", Variable(\"x\")))))");
        legal("2 - -x", "Block(ExpressionStatement(FunctionCall(\"-\", IntegerLiteral(2), FunctionCall(\"-\", Variable(\"x\")))))");
        legal("-sin(-x-y)", "Block(ExpressionStatement(FunctionCall(\"-\", FunctionCall(\"sin\", FunctionCall(\"-\", FunctionCall(\"-\", Variable(\"x\")), Variable(\"y\"))))))");
        legal("priNt_ln (/* argumente pole, vt. ka blah() */)", "Block(ExpressionStatement(FunctionCall(\"priNt_ln\")))");
        legal("if a > b then a else if b > c then c else b", "Block(IfStatement(FunctionCall(\">\", Variable(\"a\"), Variable(\"b\")), Block(ExpressionStatement(Variable(\"a\"))), Block(IfStatement(FunctionCall(\">\", Variable(\"b\"), Variable(\"c\")), Block(ExpressionStatement(Variable(\"c\"))), Block(ExpressionStatement(Variable(\"b\")))))))");

        illegal("3 + 56 #^!");
        illegal("\"Tere\"vana kere\"");
        illegal("blah /* blah */ blah */");
        illegal("\"Tere\nvana kere\"");
        illegal("\"\nTerevana kere\"");
    }

    protected void legal(String program, String expectedAstString) {
        AstNode actualAst = AktkAst.createAst(program);
        assertEquals(expectedAstString, actualAst.toString());
    }

    protected void illegal(String program) {
        try {
            AktkAst.createAst(program);
            fail("expected parse error: " + program);
        } catch (Exception ignored) {

        }
    }
}
