package cma;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.JVM)
public class CMaInterpreterTest {

    private CMaProgramWriter pw;

    @Before
    public void setUp() {
        pw = new CMaProgramWriter();
    }

    private void assertInterpreted(CMaStack expected) {
        assertInterpreted(expected, new CMaStack());
    }

    private void assertInterpreted(CMaStack expected, CMaStack initial) {
        CMaStack actual = CMaInterpreter.run(pw.toProgram(), initial);
        assertEquals(expected, actual);
    }

    @Test
    public void test1() {
        pw.visit(LOADC, 1);
        pw.visit(LOADC, 7);
        pw.visit(ADD);

        assertInterpreted(new CMaStack(8));
    }

    @Test
    public void test2() {
        int x = 0, y = 1;

        pw.visit(LOADC, y);
        pw.visit(LOAD);
        pw.visit(LOADC, 1);
        pw.visit(SUB);
        pw.visit(LOADC, x);
        pw.visit(STORE);

        assertInterpreted(new CMaStack(19, 20, 19), new CMaStack(10, 20));
    }

    @Test
    public void test2b() {
        int x = 0, y = 1;

        pw.visit(LOADA, y);
        pw.visit(LOADC, 1);
        pw.visit(SUB);
        pw.visit(STOREA, x);

        assertInterpreted(new CMaStack(19, 20, 19), new CMaStack(10, 20));
    }

    @Test
    public void test3() {
        int x = 0, y = 1;

        CMaLabel A = new CMaLabel();
        CMaLabel B = new CMaLabel();
        pw.visit(LOADA, x);
        pw.visit(LOADA, y);
        pw.visit(GE);
        pw.visit(JUMPZ, A);
        pw.visit(LOADA, x);
        pw.visit(LOADA, y);
        pw.visit(SUB);
        pw.visit(STOREA, x);
        pw.visit(POP);
        pw.visit(JUMP, B);
        pw.visit(A);
        pw.visit(LOADA, y);
        pw.visit(LOADA, x);
        pw.visit(SUB);
        pw.visit(STOREA, y);
        pw.visit(POP);
        pw.visit(B);

        assertInterpreted(new CMaStack(15, 5), new CMaStack(15, 20));
        assertInterpreted(new CMaStack(5, 15), new CMaStack(20, 15));
    }

    @Test
    public void test4() {
        int a = 0, b = 1, c = 2;

        CMaLabel A = new CMaLabel();
        CMaLabel B = new CMaLabel();
        pw.visit(A);
        pw.visit(LOADA, a);
        pw.visit(LOADC, 0);
        pw.visit(GE);
        pw.visit(JUMPZ, B);
        pw.visit(LOADA, c);
        pw.visit(LOADC, 1);
        pw.visit(ADD);
        pw.visit(STOREA, c);
        pw.visit(POP);
        pw.visit(LOADA, a);
        pw.visit(LOADA, b);
        pw.visit(SUB);
        pw.visit(STOREA, a);
        pw.visit(POP);
        pw.visit(JUMP, A);
        pw.visit(B);

        assertInterpreted(new CMaStack(0, 5, 4), new CMaStack(20, 5, 0));
        assertInterpreted(new CMaStack(-4, 6, 4), new CMaStack(20, 6, 0));
        assertInterpreted(new CMaStack(-1, 7, 3), new CMaStack(20, 7, 0));
    }

    @Test
    public void test5() {
        int n = 0, i = 1, r = 2;

        CMaLabel _while = new CMaLabel();
        CMaLabel _end = new CMaLabel();
        pw.visit(_while);
        pw.visit(LOADA, i);
        pw.visit(LOADA, n);
        pw.visit(LEQ);
        pw.visit(JUMPZ, _end);

        pw.visit(LOADA, r);
        pw.visit(LOADA, i);
        pw.visit(MUL);
        pw.visit(STOREA, r);
        pw.visit(POP);

        pw.visit(LOADA, i);
        pw.visit(LOADC, 1);
        pw.visit(ADD);
        pw.visit(STOREA, i);
        pw.visit(POP);

        pw.visit(JUMP, _while);
        pw.visit(_end);
        pw.visit(HALT);

        assertInterpreted(new CMaStack(5, 6, 120), new CMaStack(5, 1, 1));
        assertInterpreted(new CMaStack(6, 7, 720), new CMaStack(6, 1, 1));
    }

    @Test
    public void test6() {
        int n = 0, x = 1, z = 2;

        CMaLabel _while = new CMaLabel();
        CMaLabel _even = new CMaLabel();
        CMaLabel _end = new CMaLabel();
        pw.visit(LOADC, 1);
        pw.visit(STOREA, z);
        pw.visit(POP);

        pw.visit(_while);
        pw.visit(LOADA, n);
        pw.visit(LOADC, 0);
        pw.visit(GR);
        pw.visit(JUMPZ, _end);

        pw.visit(LOADA, n);
        pw.visit(LOADC, 1);
        pw.visit(AND);
        pw.visit(JUMPZ, _even);

        pw.visit(LOADA, z);
        pw.visit(LOADA, x);
        pw.visit(MUL);
        pw.visit(STOREA, z);
        pw.visit(POP);

        pw.visit(_even);
        pw.visit(LOADA, x);
        pw.visit(DUP);
        pw.visit(MUL);
        pw.visit(STOREA, x);
        pw.visit(POP);

        pw.visit(LOADA, n);
        pw.visit(LOADC, 2);
        pw.visit(DIV);
        pw.visit(STOREA, n);
        pw.visit(POP);

        pw.visit(JUMP, _while);
        pw.visit(_end);
        pw.visit(HALT);

        assertInterpreted(new CMaStack(0, 43046721, 177147), new CMaStack(11, 3, 0));
    }

    @Test(expected = CMaException.class)
    public void test_missing_label() {
        CMaLabel _label = new CMaLabel();
        pw.visit(JUMP, _label);

        CMaInterpreter.run(pw.toProgram(), new CMaStack());
    }

    @Test(expected = CMaException.class)
    public void test_multiple_label() {
        CMaLabel _label = new CMaLabel();
        pw.visit(_label);
        pw.visit(JUMP, _label);
        pw.visit(_label);
    }
}
