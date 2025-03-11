package toylangs.modul;

import toylangs.modul.ast.ModulMul;
import toylangs.modul.ast.ModulNum;
import toylangs.modul.ast.ModulPow;
import toylangs.modul.ast.ModulProg;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static toylangs.modul.ast.ModulNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModulEvaluatorTest {

    private Map<String, Integer> env;

    @Before
    public void setUp() {
        env = new HashMap<>();
        env.put("x", 2);
        env.put("y", 3);
        env.put("z", 4);
        env.put("foo", 5);
        env.put("bar", 6);

        env = Collections.unmodifiableMap(env);
    }

    @Test
    public void test01_num() {
        checkEval(prog(num(0), 2), 0);
        checkEval(prog(num(1), 2), 1);
        checkEval(prog(num(10), 16), 10);
        checkEval(prog(num(256), 1000), 256);
    }

    @Test
    public void test02_var() {
        checkEval(prog(var("x"), 16), 2);
        checkEval(prog(var("foo"), 16), 5);
        checkEval(prog(var("z"), 100), 4);
        checkEvalException(prog(var("a"), 2));
    }

    @Test
    public void test03_ops() {
        checkEval(prog(add(num(1), num(1)), 16), 2);
        checkEval(prog(neg(neg(num(6))), 16), 6);
        checkEval(prog(mul(var("y"), var("z")), 16), 12);
        checkEval(prog(sub(num(2), num(1)), 16), 1);
        checkEval(prog(neg(sub(num(5), num(12))), 16), 7);
    }

    @Test
    public void test04_pow() {
        checkEval(prog(pow(num(1), 8), 16), 1);
        checkEval(prog(pow(num(0), 8), 16), 0);
        checkEval(prog(pow(num(0), 0), 16), 1);
        checkEval(prog(pow(num(8), 0), 16), 1);

        checkEval(prog(pow(num(2), 10), 2000), 1024);
        checkEval(prog(pow(num(3), 10), 60000), 59049);

        checkEval(prog(pow(num(2), 8), 500), 256);
        checkEval(prog(pow(neg(num(2)), 8), 500), 256);
    }

    @Test
    public void test05_mod() {
        checkEval(prog(num(7), 5), 2);
        checkEval(prog(num(-1), 2), 1);
        checkEval(prog(var("bar"), 5), 1);
        checkEval(prog(add(num(1), num(1)), 16), 2);
        checkEval(prog(sub(num(1), var("x")), 3), 2);
        checkEval(prog(mul(var("y"), var("z")), 7), 5);
    }

    @Test
    public void test06_overflow() {
        final int m = 10000;
        ModulMul sixteen = mul(num(m - 4), num(m - 4));
        ModulNum negOne = num(m - 1);
        ModulMul one = mul(negOne, negOne);

        checkEval(prog(sixteen, m), 16);
        checkEval(prog(one, m), 1);

        checkEval(prog(neg(negOne), m), 1);
        checkEval(prog(mul(one, sixteen), m), 16);
        checkEval(prog(neg(mul(negOne, sixteen)), m), 16);

        checkEval(prog(mul(num(1000000), num(1000000)), 1009), 507); // kas vahepealne num on ka normaliseeritud?
    }

    @Test
    public void test07_overflow_pow() {
        fail("See test avalikustatakse pärast eksamit! Astendamisel overflow? Näiteks 2^100 (mod 7) == 2.");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }


    // Eraldi meetod, et ModulMasterTest saaks overload-ida.
    protected int eval(ModulProg prog, Map<String, Integer> env) {
        return ModulEvaluator.eval(prog, env);
    }

    protected void checkEval(ModulProg prog, int expected) {
        assertEquals(expected, eval(prog, env));
    }

    protected void checkEvalException(ModulProg prog) {
        assertThrows(ModulException.class, () -> eval(prog, env));
    }
}
