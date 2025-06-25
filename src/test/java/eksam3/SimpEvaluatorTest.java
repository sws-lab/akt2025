package eksam3;

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
public class SimpEvaluatorTest {

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    @Test
    public void test01_num() {
        checkEval(prog(num(10)), 10);
        checkEval(prog(num(-1)), -1);
    }

    @Test
    public void test02_arit() {
        checkEval(prog(add(num(2), num(2))), 4);
        checkEval(prog(mul(num(3), num(-2))), -6);
    }

    @Test
    public void test03_seq() {
        checkEval(prog(num(1), num(2), num(3)), 3);
    }

    @Test
    public void test04_mem1() {
        checkEval(prog(num(7), mem(1)), 7);
        checkEval(prog(num(7), add(mem(1), num(1))), 8);
    }

    @Test
    public void test05_memprev() {
        checkEval(prog(num(5), num(6), mem(1)), 6);
        checkEval(prog(num(5), num(6), mem(2)), 5);
        checkEval(prog(num(5), mem(1), num(6), mem(1)), 6);
        checkEval(prog(num(5), mem(1), num(6), mem(2)), 5);
    }

    @Test
    public void test06_memseq() {
        checkEval(prog(num(5), add(mem(1), num(1)), mem(1)), 6);
        checkEval(prog(num(5), add(mem(1), num(1)), mem(2)), 5);
    }

    @Test
    public void test07_memexception() {
        fail("See test avalikustatakse pärast eksamit! Kas viskad õige erindi?");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }


    protected void checkEval(SimpProg prog, int expected) {
        assertEquals(expected, SimpEvaluator.eval(prog));
    }

    protected void checkEvalException(SimpProg prog) {
        assertThrows(SimpException.class, () -> SimpEvaluator.eval(prog));
    }
}
