package toylangs.modul;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import toylangs.modul.ast.ModulMul;
import toylangs.modul.ast.ModulNum;
import toylangs.modul.ast.ModulPow;
import toylangs.modul.ast.ModulProg;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static toylangs.modul.ast.ModulNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModulCompilerTest {

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
        checkCompile(prog(num(0), 2), 0);
        checkCompile(prog(num(1), 2), 1);
        checkCompile(prog(num(10), 16), 10);
        checkCompile(prog(num(256), 1000), 256);
    }

    @Test
    public void test02_var() {
        checkCompile(prog(var("x"), 16), 2);
        checkCompile(prog(var("foo"), 16), 5);
        checkCompile(prog(var("z"), 100), 4);
        checkCompileException(prog(var("a"), 2));
    }

    @Test
    public void test03_ops() {
        checkCompile(prog(add(num(1), num(1)), 16), 2);
        checkCompile(prog(neg(neg(num(6))), 16), 6);
        checkCompile(prog(mul(var("y"), var("z")), 16), 12);
        checkCompile(prog(sub(num(2), num(1)), 16), 1);
        checkCompile(prog(neg(sub(num(5), num(12))), 16), 7);
    }

    @Test
    public void test04_pow() {
        checkCompile(prog(pow(num(1), 8), 16), 1);
        checkCompile(prog(pow(num(0), 8), 16), 0);
        checkCompile(prog(pow(num(0), 0), 16), 1);
        checkCompile(prog(pow(num(8), 0), 16), 1);

        checkCompile(prog(pow(num(2), 10), 2000), 1024);
        checkCompile(prog(pow(num(3), 10), 60000), 59049);

        checkCompile(prog(pow(num(2), 8), 500), 256);
        checkCompile(prog(pow(neg(num(2)), 8), 500), 256);
    }

    @Test
    public void test05_mod() {
        checkCompile(prog(num(7), 5), 2);
        checkCompile(prog(num(-1), 2), 1);
        checkCompile(prog(var("bar"), 5), 1);
        checkCompile(prog(add(num(1), num(1)), 16), 2);
        checkCompile(prog(sub(num(1), var("x")), 3), 2);
        checkCompile(prog(mul(var("y"), var("z")), 7), 5);
    }

    @Test
    public void test06_overflow() {
        final int m = 10000;
        ModulMul sixteen = mul(num(m - 4), num(m - 4));
        ModulNum negOne = num(m - 1);
        ModulMul one = mul(negOne, negOne);

        checkCompile(prog(sixteen, m), 16);
        checkCompile(prog(one, m), 1);

        checkCompile(prog(neg(negOne), m), 1);
        checkCompile(prog(mul(one, sixteen), m), 16);
        checkCompile(prog(neg(mul(negOne, sixteen)), m), 16);

        checkCompile(prog(mul(num(1000000), num(1000000)), 1009), 507); // kas vahepealne num on ka normaliseeritud?
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
    protected CMaProgram compile(ModulProg prog, List<String> variables) {
        return ModulCompiler.compile(prog, variables);
    }

    protected void checkCompile(ModulProg prog, int expected) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Integer> entry : env.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(entry.getValue());
        }

        CMaProgram program = compile(prog, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        CMaStack expectedStack = new CMaStack(initialStack);
        expectedStack.push(expected);
        assertEquals(expectedStack, finalStack);
    }

    protected void checkCompileException(ModulProg prog) {
        List<String> variables = new ArrayList<>(env.keySet());
        assertThrows(ModulException.class, () -> compile(prog, variables));
    }
}
