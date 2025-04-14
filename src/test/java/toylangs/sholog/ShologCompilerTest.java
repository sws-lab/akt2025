package toylangs.sholog;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import cma.CMaUtils;
import toylangs.sholog.ast.ShologNode;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static toylangs.sholog.ast.ShologNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShologCompilerTest {

    private Map<String, Boolean> env;

    @Before
    public void setUp() {
        env = new HashMap<>();
        env.put("a", true);
        env.put("b", false);
        env.put("c", false);
        env.put("foo", true);
        env.put("bar", false);

        env = Collections.unmodifiableMap(env);
    }

    @Test
    public void test01_literals() {
        checkCompile(lit(true), true);
        checkCompile(lit(false), false);
    }

    @Test
    public void test02_variables() {
        checkCompile(var("a"), true);
        checkCompile(var("b"), false);
        checkCompile(var("foo"), true);
    }

    @Test
    public void test03_operators_eager() {
        // eand
        checkCompile(eand(lit(true), lit(true)), true);
        checkCompile(eand(lit(true), lit(false)), false);
        checkCompile(eand(lit(false), lit(true)), false);
        checkCompile(eand(lit(false), lit(false)), false);
        checkCompile(eand(var("a"), var("b")), false);
        // eor
        checkCompile(eor(lit(true), lit(true)), true);
        checkCompile(eor(lit(true), lit(false)), true);
        checkCompile(eor(lit(false), lit(true)), true);
        checkCompile(eor(lit(false), lit(false)), false);
        checkCompile(eor(var("a"), var("b")), true);
        // xor
        checkCompile(xor(lit(true), lit(true)), false);
        checkCompile(xor(lit(true), lit(false)), true);
        checkCompile(xor(lit(false), lit(true)), true);
        checkCompile(xor(lit(false), lit(false)), false);
        checkCompile(xor(var("foo"), lit(true)), false);
        checkCompile(xor(var("bar"), lit(true)), true);
        checkCompile(xor(var("a"), var("b")), true);
    }

    @Test
    public void test04_operators_lazy() {
        // land
        checkCompile(land(lit(true), lit(true)), true);
        checkCompile(land(lit(true), lit(false)), false);
        checkCompile(land(lit(false), lit(true)), false);
        checkCompile(land(lit(false), lit(false)), false);
        checkCompile(land(var("a"), var("b")), false);
        // lor
        checkCompile(lor(lit(true), lit(true)), true);
        checkCompile(lor(lit(true), lit(false)), true);
        checkCompile(lor(lit(false), lit(true)), true);
        checkCompile(lor(lit(false), lit(false)), false);
        checkCompile(lor(var("a"), var("b")), true);
    }

    @Test
    public void test05_operators_eager_error() {
        // eand
        checkCompileError(eand(lit(true), error(1)), 1, -1);
        checkCompileError(eand(lit(false), error(1)), 0, -1);
        checkCompileError(eand(error(1), lit(true)), -1);
        checkCompileError(eand(error(1), lit(false)), -1);
        // eor
        checkCompileError(eor(lit(true), error(1)), 1, -1);
        checkCompileError(eor(lit(false), error(1)), 0, -1);
        checkCompileError(eor(error(1), lit(true)), -1);
        checkCompileError(eor(error(1), lit(false)), -1);
        // xor
        checkCompileError(xor(lit(true), error(1)), 1, -1);
        checkCompileError(xor(lit(false), error(1)), 0, -1);
        checkCompileError(xor(error(1), lit(true)), -1);
        checkCompileError(xor(error(1), lit(false)), -1);
    }

    @Test
    public void test06_operators_lazy_error() {
        // land
        checkCompileError(land(lit(true), error(1)), 1, -1);
        checkCompile(land(lit(false), error(1)), false);
        checkCompileError(land(error(1), lit(true)), -1);
        checkCompileError(land(error(1), lit(false)), -1);
        checkCompile(land(var("b"), var("jama")), false);
        // lor
        checkCompile(lor(lit(true), error(1)), true);
        checkCompileError(lor(lit(false), error(1)), 0, -1);
        checkCompileError(lor(error(1), lit(true)), -1);
        checkCompileError(lor(error(1), lit(false)), -1);
        checkCompile(lor(var("a"), var("jama")), true);
    }

    @Test
    public void test07_variables_error() {
        fail("See test avalikustatakse pärast eksamit! Defineerimata muutuja korral magasini tipus -UNDEFINED_CODE?");
    }

    @Test
    public void test08_error_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }

    
    private void checkCompile(ShologNode node, boolean expected) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Boolean> entry : env.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(CMaUtils.bool2int(entry.getValue()));
        }

        CMaProgram program = ShologCompiler.compile(node, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        CMaStack expectedStack = new CMaStack(initialStack);
        expectedStack.push(CMaUtils.bool2int(expected));
        assertEquals(expectedStack, finalStack);
    }

    private void checkCompileError(ShologNode node, int... expected) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Boolean> entry : env.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(CMaUtils.bool2int(entry.getValue()));
        }

        CMaProgram program = ShologCompiler.compile(node, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        CMaStack expectedStack = new CMaStack(initialStack);
        for (int i : expected)
            expectedStack.push(i);
        assertEquals(expectedStack, finalStack);
    }
}
