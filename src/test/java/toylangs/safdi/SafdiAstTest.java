package toylangs.safdi;

import toylangs.safdi.ast.SafdiNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static toylangs.safdi.ast.SafdiNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SafdiAstTest {

    @Test
    public void test01_num() {
        legal("1", num(1));
        legal("0", num(0));
        legal("123", num(123));
        legal(" 456  ", num(456));

        illegal("0123");
        illegal("00");
        illegal("1.0");
        illegal("1 2");
    }

    @Test
    public void test02_var() {
        legal("a", var("a"));
        legal("foo", var("foo"));
        legal("aSd", var("aSd"));
        legal("FOO", var("FOO"));
        legal("  b ", var("b"));

        illegal("recover");
        illegal("2a");
        illegal("a2");
    }

    @Test
    public void test03_operator() {
        legal("a + b", add(var("a"), var("b")));
        legal("a * b", mul(var("a"), var("b")));
        legal("a / b", div(var("a"), var("b")));
        legal("-a", neg(var("a")));
        legal("a+b", add(var("a"), var("b")));
        legal("- a", neg(var("a")));
        legal("(a)", var("a"));
        legal("(a + b)", add(var("a"), var("b")));
        legal("((a + b))", add(var("a"), var("b")));

        illegal("a - b");
        illegal("+a");
        illegal("a ^ b");
        illegal("a ** b");
        illegal("a +");
        illegal("* b");
        illegal("a + * b");
        illegal("((a + b)");
        illegal("(a + b))");
        illegal("a (+ b)");
        illegal("()");
    }

    @Test
    public void test04_prio_assoc() {
        legal("a + b * c", add(var("a"), mul(var("b"), var("c"))));
        legal("a + (b * c)", add(var("a"), mul(var("b"), var("c"))));
        legal("(a + b) * c", mul(add(var("a"), var("b")), var("c")));
        
        legal("a + b / c", add(var("a"), div(var("b"), var("c"))));
        legal("a + (b / c)", add(var("a"), div(var("b"), var("c"))));
        legal("(a + b) / c", div(add(var("a"), var("b")), var("c")));
        
        legal("a + b + c", add(add(var("a"), var("b")), var("c")));
        legal("(a + b) + c", add(add(var("a"), var("b")), var("c")));
        legal("a + (b + c)", add(var("a"), add(var("b"), var("c"))));
        
        legal("a + -b + c", add(add(var("a"), neg(var("b"))), var("c")));
        legal("a + -(b + c)", add(var("a"), neg(add(var("b"), var("c")))));

        legal("- -a", neg(neg(var("a"))));
        legal("--a", neg(neg(var("a"))));
    }

    @Test
    public void test05_recover() {
        legal("a / b recover c", div(var("a"), var("b"), var("c")));
        legal("a / b recover (a + b)", div(var("a"), var("b"), add(var("a"), var("b"))));

        illegal("a / b recover");
        illegal("a * b recover c");
        illegal("a + b recover c");
        illegal("a / (b recover c)");
        illegal("a / b (recover c)");
    }

    @Test
    public void test06_recover_prio_assoc() {
        legal("a / b / c recover d", div(div(var("a"), var("b")), var("c"), var("d")));
        legal("a * b / c recover d", div(mul(var("a"), var("b")), var("c"), var("d")));
        legal("a / b recover c * d", mul(div(var("a"), var("b"), var("c")), var("d")));
        legal("a / b recover (c * d)", div(var("a"), var("b"), mul(var("c"), var("d"))));

        legal("a / b recover c + x", add(div(var("a"), var("b"), var("c")), var("x")));
        legal("x + a / b recover c", add(var("x"), div(var("a"), var("b"), var("c"))));
        legal("-a / b recover c", div(neg(var("a")), var("b"), var("c")));
    }

    @Test
    public void test07_div_prio_assoc() {
        fail("See test avalikustatakse pärast eksamit! Paljad (ilma recover) avaldised, näiteks a/b*c...");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }


    private void legal(String input, SafdiNode expectedAst) {
        SafdiNode actualAst = SafdiAst.makeSafdiAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            SafdiAst.makeSafdiAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
