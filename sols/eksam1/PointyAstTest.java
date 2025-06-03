package eksam1;

import eksam1.ast.PointyNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static eksam1.ast.PointyNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PointyAstTest {

    @Test
    public void test01_numvar() {
        legal("123", num(123));
        legal("0", num(0));
        legal("x", var("x"));
        legal("ChEcK", var("ChEcK"));

        illegal("0123");
        illegal("1.0");
        illegal("x0");
        illegal("00");
    }

    @Test
    public void test02_ops() {
        legal("x - y", sub(var("x"), var("y")));
        legal("5 -6", sub(num(5), num(6)));
        legal("-x", neg(var("x")));
        legal("- x", neg(var("x")));
        legal("-6", neg(num(6)));
        legal("- 6", neg(num(6)));
        legal("&foo", addrOf("foo"));
        legal("&  foo", addrOf("foo"));
        legal("*3", deref(num(3)));
        legal("*z", deref(var("z")));
        legal("* z", deref(var("z")));

        illegal("&3");
        illegal("5 -");
    }

    @Test
    public void test03_assign() {
        legal("x = 10", assign("x", num(10)));
        legal("x = foo", assign("x", var("foo")));
        legal("z = 10", assign("z", num(10)));

        legal("x = y = 10", assign("x", assign("y", num(10))));
        legal("x = (y = 10)", assign("x", assign("y", num(10))));
        illegal("(x = foo) = 10");

        illegal("3 = 10");
        illegal("= 10");
        illegal("x =");
    }

    @Test
    public void test04_add() {
        legal("x + y", sub(var("x"), neg(var("y"))));
        legal("5 +6", sub(num(5), neg(num(6))));

        illegal("5 +");
        illegal("+ 6");
    }

    @Test
    public void test05_comma() {
        legal("10", num(10));
        legal("10, x, 3", comma(num(10), var("x"), num(3)));
        legal("10, (x, 3), y", comma(num(10), comma(var("x"), num(3)), var("y")));

        legal("(10)", num(10));
        legal("y = (x, 3)", assign("y", comma(var("x"), num(3))));
        legal("(y = (x, 3)), 10", comma(assign("y", comma(var("x"), num(3))), num(10)));

        illegal("");
        illegal("10,");
        illegal(",10");
        illegal("10,,10");
        illegal("(10");
        illegal("10)");
        illegal(")10(");
    }

    @Test
    public void test06_arith_prio_assoc() {
        legal("1 - 2 - 3", sub(sub(num(1), num(2)), num(3)));
        legal("1 - (2 - 3)", sub(num(1), sub(num(2), num(3))));

        legal("1 + 2 - 3", sub(sub(num(1), neg(num(2))), num(3)));
        legal("(1 + 2) - 3", sub(sub(num(1), neg(num(2))), num(3)));
        legal("1 + (2 - 3)", sub(num(1), neg(sub(num(2), num(3)))));

        legal("1 - 2 + 3", sub(sub(num(1), num(2)), neg(num(3))));
        legal("(1 - 2) + 3", sub(sub(num(1), num(2)), neg(num(3))));
        legal("1 - (2 + 3)", sub(num(1), sub(num(2), neg(num(3)))));

        legal("1 - - 2", sub(num(1), neg(num(2))));
        legal("1 + - 2", sub(num(1), neg(neg(num(2)))));
        legal("-1 - 2", sub(neg(num(1)), num(2)));
        legal("(-1) - 2", sub(neg(num(1)), num(2)));
        legal("-(1 - 2)", neg(sub(num(1), num(2))));
    }

    @Test
    public void test07_prio() {
        legal("*x - 1", sub(deref(var("x")), num(1)));
        legal("*x - *y", sub(deref(var("x")), deref(var("y"))));
        illegal("&(x+1)");
        illegal("&*z");

        legal("&x - 1", sub(addrOf("x"), num(1)));
        legal("*x - &y", sub(deref(var("x")), addrOf("y")));
        legal("&x - *y", sub(addrOf("x"), deref(var("y"))));

        legal("*x + 1", sub(deref(var("x")), neg(num(1))));

        legal("x = y - z", assign("x", sub(var("y"), var("z"))));
    }

    @Test
    public void test08_multiple() {
        legal("10, x, &y, *3", comma(num(10), var("x"), addrOf("y"), deref(num(3))));
        legal("x = *&z", assign("x", deref(addrOf("z"))));
        legal("x = &foo, y = &bar, z = *x + *y", comma(
                assign("x", addrOf("foo")),
                assign("y", addrOf("bar")),
                assign("z", sub(deref(var("x")), neg(deref(var("y")))))));

        legal("y = (x, 3), 10", comma(assign("y", comma(var("x"), num(3))), num(10)));
        legal("y = x, 3, 10", comma(assign("y", var("x")), num(3), num(10)));

        legal("--x", neg(neg(var("x"))));
        legal("-*&x", neg(deref(addrOf("x"))));
        legal("*-&x", deref(neg(addrOf("x"))));
        legal("**x", deref(deref(var("x"))));
        illegal("&&x");

        legal("(1, 2)", comma(num(1), num(2)));
    }


    private void legal(String input, PointyNode expectedAst) {
        PointyNode actualAst = PointyAst.makePointyAst(input);
        assertEquals(expectedAst, actualAst);
    }

    @SuppressWarnings("CatchMayIgnoreException") // IntelliJ rumaluse vastu...
    private void illegal(String input) {
        try {
            PointyAst.makePointyAst(input);
            fail("expected parse error: " + input);
        } catch (Exception e) {
            assertFalse(e instanceof ClassCastException);
        }
    }
}
