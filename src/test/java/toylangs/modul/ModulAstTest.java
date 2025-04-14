package toylangs.modul;

import toylangs.modul.ast.ModulProg;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static toylangs.modul.ast.ModulNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModulAstTest {

    @Test
    public void test01_num() {
        legal("123 (mod 5)", prog(num(123), 5));
        legal("0 (mod 2)", prog(num(0), 2));

        illegal("0123 (mod 5)");
    }

    @Test
    public void test02_var() {
        legal("foo (mod 2)", prog(var("foo"), 2));
        legal("foo__ (mod 2)", prog(var("foo__"), 2));
        legal("a (mod 10)", prog(var("a"), 10));

        illegal("_a (mod 10)");
        illegal("a_a (mod 3)");
    }

    @Test
    public void test03_mod() {
        legal("123 ( mod 5)", prog(num(123), 5));
        legal("123 (mod   5)", prog(num(123), 5));
        legal("123 (mod 5 )", prog(num(123), 5));
        legal("123(mod 5)", prog(num(123), 5));
        illegal("10");  // mod puudu!
        illegal("123 (mod5)");
        illegal("1 (mod -2)");
    }

    @Test
    public void test04_operator() {
        legal("-x (mod 2)", prog(neg(var("x")), 2));
        legal("x + y (mod 2)", prog(add(var("x"), var("y")), 2));
        legal("x * y (mod 2)", prog(mul(var("x"), var("y")), 2));
        legal("x - y (mod 2)", prog(sub(var("x"), var("y")), 2));
        legal("x ^ 30 (mod 2)", prog(pow(var("x"), 30), 2));

        illegal("x ^ y (mod 2)");
        illegal("x ^ -3 (mod 2)");
    }

    @Test
    public void test05_paren() {
        legal("-(x) (mod 2)", prog(neg(var("x")), 2));
        legal("(-x) (mod 2)", prog(neg(var("x")), 2));
        legal("(x + y) (mod 2)", prog(add(var("x"), var("y")), 2));
        legal("((x + y)) (mod 2)", prog(add(var("x"), var("y")), 2));
        illegal("(x (mod 7)");
        illegal("x) (mod 7)");
        illegal("123 (mod 5");
        illegal("123 mod 5)");
        illegal("123 mod 5");
    }

    @Test
    public void test06_prio_assoc() {
        legal("x + y * z (mod 2)", prog(add(var("x"), mul(var("y"), var("z"))), 2));
        legal("(x + y) * z (mod 2)", prog(mul(add(var("x"), var("y")), var("z")), 2));
        legal("-x ^ 30 (mod 2)", prog(neg(pow(var("x"), 30)), 2));
        legal("(- x)^30 (mod 2)", prog(pow(neg(var("x")), 30), 2));
        legal("-x * y (mod 2)", prog(mul(neg(var("x")), var("y")), 2));
    }

    @Test
    public void test07_basicarith() {
        fail("See test avalikustatakse pärast eksamit! Kontrollime lihtsalt aritmeetika prio & assoc (ilma astendamiseta).");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised.");
    }


    private void legal(String input, ModulProg expectedAst) {
        ModulProg actualAst = ModulAst.makeModulAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            ModulAst.makeModulAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
