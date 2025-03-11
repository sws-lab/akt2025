package toylangs.imp;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.imp.ast.ImpNode;

import static org.junit.Assert.*;
import static toylangs.imp.ast.ImpNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImpEvaluatorTest {

    private static final ImpNode expr1 = div(add(num(5), add(num(3), neg(num(2)))), num(2));
    private static final ImpNode expr2 = add(add(num(5), div(num(3), neg(num(2)))), num(2));
    private static final ImpNode expr3 = div(div(num(8), num(2)), num(2));
    private static final ImpNode expr4 = div(num(8), div(num(2), num(2)));
    private static final ImpNode expr5 = add(neg(expr4), add(expr2, expr3));

    @Test
    public void test01_noAssign() {
        assertEquals(10, ImpEvaluator.eval(prog(num(10))));
        assertEquals(4, ImpEvaluator.eval(prog(add(num(2), num(2)))));
        assertEquals(3, ImpEvaluator.eval(prog(expr1)));
        assertEquals(6, ImpEvaluator.eval(prog(expr2)));
        assertEquals(2, ImpEvaluator.eval(prog(expr3)));
        assertEquals(8, ImpEvaluator.eval(prog(expr4)));
        assertEquals(0, ImpEvaluator.eval(prog(expr5)));
    }

    @Test
    public void test02_singleAssign() {
        assertEquals(5, ImpEvaluator.eval(prog(var('x'), assign('x', num(5)))));
        assertEquals(3, ImpEvaluator.eval(prog(var('x'), assign('x', expr1))));
        assertEquals(3 + 6, ImpEvaluator.eval(prog(add(var('y'), expr2), assign('y', expr1))));
    }

    @Test
    public void test03_multipleAssign() {
        assertEquals(3 + 6, ImpEvaluator.eval(prog(add(var('y'), var('z')), assign('y', expr1), assign('z', expr2))));
        assertEquals(5 + 1, ImpEvaluator.eval(prog(var('y'), assign('x', num(5)), assign('y', add(var('x'), num(1))))));
    }

    @Test
    public void test04_reassign() {
        assertEquals(7, ImpEvaluator.eval(prog(var('x'), assign('x', num(5)), assign('y', num(6)), assign('x', num(7)))));
        assertEquals(5 + 1 + 1, ImpEvaluator.eval(prog(var('x'), assign('x', num(5)), assign('y', add(var('x'), num(1))), assign('x', add(var('y'), num(1))))));
    }

    @Test(expected = RuntimeException.class)
    public void test05_undefined() {
        ImpEvaluator.eval(prog(var('y'), assign('x', num(5))));
    }
}
