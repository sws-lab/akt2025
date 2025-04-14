package toylangs.imp;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.imp.ast.ImpNode;
import toylangs.imp.ast.ImpProg;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static toylangs.imp.ast.ImpNode.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImpCompilerTest {

    private static final ImpNode expr1 = div(add(num(5), add(num(3), neg(num(2)))), num(2));
    private static final ImpNode expr2 = add(add(num(5), div(num(3), neg(num(2)))), num(2));
    private static final ImpNode expr3 = div(div(num(8), num(2)), num(2));
    private static final ImpNode expr4 = div(num(8), div(num(2), num(2)));
    private static final ImpNode expr5 = add(neg(expr4), add(expr2, expr3));

    @Test
    public void test01_noAssign() {
        checkCompile(prog(num(10)), 10);
        checkCompile(prog(add(num(2), num(2))), 4);
        checkCompile(prog(expr1), 3);
        checkCompile(prog(expr2), 6);
        checkCompile(prog(expr3), 2);
        checkCompile(prog(expr4), 8);
        checkCompile(prog(expr5), 0);
    }

    @Test
    public void test02_singleAssign() {
        checkCompile(prog(var('x'), assign('x', num(5))), 5, 5);
        checkCompile(prog(var('x'), assign('x', expr1)), 3, 3);
        checkCompile(prog(add(var('y'), expr2), assign('y', expr1)), 3, 9);
    }

    @Test
    public void test03_multipleAssign() {
        checkCompile(prog(add(var('y'), var('z')), assign('y', expr1), assign('z', expr2)), 3, 6, 9);
        checkCompile(prog(var('y'), assign('x', num(5)), assign('y', add(var('x'), num(1)))), 5, 6, 6);
    }

    @Test
    public void test04_reassign() {
        checkCompile(prog(var('x'), assign('x', num(5)), assign('y', num(6)), assign('x', num(7))), 7, 6, 7);
        checkCompile(prog(var('x'), assign('x', num(5)), assign('y', add(var('x'), num(1))), assign('x', add(var('y'), num(1)))), 7, 6, 7);
        checkCompile(prog(var('x'), assign('x', num(5)), assign('x', add(var('x'), num(4)))), 9, 9);
    }

    @Test(expected = NoSuchElementException.class)
    public void test05_undefined() {
        ImpCompiler.compile(prog(var('y'), assign('x', num(5))));
    }


    private void checkCompile(ImpProg prog, int... expected) {
        CMaProgram program = ImpCompiler.compile(prog);
        CMaStack initialStack = new CMaStack();
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        CMaStack expectedStack = new CMaStack(expected);
        assertEquals(expectedStack, finalStack);
    }
}
