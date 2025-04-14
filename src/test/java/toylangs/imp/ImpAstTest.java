package toylangs.imp;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.imp.ast.ImpNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.imp.ast.ImpNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImpAstTest {

    @Test
    public void test01_num() {
        legal("5", prog(num(5)));
        legal("123", prog(num(123)));
        legal("0", prog(num(0)));

        illegal("05");
    }

    @Test
    public void test02_var() {
        legal("x", prog(var('x')));
        legal("y", prog(var('y')));
        legal("X", prog(var('X')));

        illegal("foo");
        illegal("_");
    }

    @Test
    public void test03_ops() {
        legal("-x", prog(neg(var('x'))));
        legal("-10", prog(neg(num(10))));
        legal("x+y", prog(add(var('x'), var('y'))));
        legal("5+6", prog(add(num(5), num(6))));
        legal("x/y", prog(div(var('x'), var('y'))));
        legal("x+y/z", prog(add(var('x'), div(var('y'), var('z')))));
        legal("-x+y", prog(add(neg(var('x')), var('y'))));
        legal("x/y/z", prog(div(div(var('x'), var('y')), var('z'))));
        legal("(x+y)/z", prog(div(add(var('x'), var('y')), var('z'))));
        legal("-(x+y)", prog(neg(add(var('x'), var('y')))));
        legal("x/(y/z)", prog(div(var('x'), div(var('y'), var('z')))));
        legal("x+-y/z", prog(add(var('x'), div(neg(var('y')), var('z')))));
        legal("(x)", prog(var('x')));

        illegal("56++");
        illegal("5++6");
        illegal("5//6");
        illegal("5-+6");
        illegal("5(+6)");
        illegal("5-6");
        illegal("(x");
        illegal("x)");
        illegal("()");
    }

    @Test
    public void test04_assign() {
        legal("x = 1, x", prog(var('x'), assign('x',  num(1))));
        legal("x = 5 / 2, x + 1", prog(add(var('x'), num(1)), assign('x',  div(num(5), num(2)))));
        legal("x = 1, y = 2, y", prog(var('y'), assign('x',  num(1)), assign('y', num(2))));

        illegal("x = 1 x");
        illegal("x = 1,, x");
        illegal(", x");
        illegal("x = 1,");
        illegal("x = 1");
        illegal("x == 1, x");
        illegal("x := 1, x");
        illegal("x = 1 y = 2, y");
    }

    @Test
    public void test05_examples() {
        legal("""
                        x = 5,
                        y = x + 1,
                        x = y + 1,

                        x""",
                prog(var('x'), assign('x', num(5)), assign('y', add(var('x'), num(1))), assign('x', add(var('y'), num(1)))));

        legal("""
                        x = 5, y = 0,
                        y = x + - 1,
                        z = (-(y + x) / 2),
                        (x + 25) / -y""",
                prog(div(add(var('x'), num(25)), neg(var('y'))), assign('x', num(5)), assign('y', num(0)), assign('y', add(var('x'), neg(num(1)))), assign('z', div(neg(add(var('y'), var('x'))), num(2)))));
    }


    private void legal(String input, ImpNode expectedAst) {
        ImpNode actualAst = ImpAst.makeImpAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            ImpAst.makeImpAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}

