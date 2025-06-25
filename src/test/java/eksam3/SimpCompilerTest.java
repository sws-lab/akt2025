package eksam3;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import eksam3.ast.SimpProg;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.concurrent.TimeUnit;

import static eksam3.ast.SimpNode.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpCompilerTest {

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    @Test
    public void test01_num() {
        checkCompile(prog(num(10)), 10);
        checkCompile(prog(num(-1)), -1);
    }

    @Test
    public void test02_arit() {
        checkCompile(prog(add(num(2), num(2))), 4);
        checkCompile(prog(mul(num(3), num(-2))), -6);
    }

    @Test
    public void test03_seq() {
        checkCompile(prog(num(1), num(2), num(3)), 1, 2, 3);
    }

    @Test
    public void test04_mem1() {
        checkCompile(prog(num(7), mem(1)), 7, 7);
        checkCompile(prog(num(7), add(mem(1), num(1))), 7, 8);
    }

    @Test
    public void test05_memprev() {
        checkCompile(prog(num(5), num(6), mem(1)), 5, 6, 6);
        checkCompile(prog(num(5), num(6), mem(2)), 5, 6, 5);
        checkCompile(prog(num(5), mem(1), num(6), mem(1)), 5, 5, 6, 6);
        checkCompile(prog(num(5), mem(1), num(6), mem(2)), 5, 5, 6, 5);
    }

    @Test
    public void test06_memseq() {
        checkCompile(prog(num(5), add(mem(1), num(1)), mem(1)), 5, 6, 6);
        checkCompile(prog(num(5), add(mem(1), num(1)), mem(2)), 5, 6, 5);
    }

    @Test
    public void test07_memexception() {
        fail("See test avalikustatakse pärast eksamit! Kas viskad õige erindi?");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }


    private void checkCompile(SimpProg prog, int... expected) {
        CMaProgram program = SimpCompiler.compile(prog);
        CMaStack initialStack = new CMaStack();
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        CMaStack expectedStack = new CMaStack(expected);
        assertEquals(expectedStack, finalStack);
    }

    protected void checkCompileException(SimpProg prog) {
        assertThrows(SimpException.class, () -> SimpCompiler.compile(prog));
    }
}
