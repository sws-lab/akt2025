package toylangs.safdi;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import toylangs.safdi.ast.SafdiDiv;
import toylangs.safdi.ast.SafdiNode;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static toylangs.safdi.ast.SafdiNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SafdiCompilerTest {

    private Map<String, Integer> env;

    @Before
    public void setUp() {
        env = new HashMap<>();
        env.put("a", 1);
        env.put("b", 0);
        env.put("c", 10);
        env.put("foo", 5);
        env.put("bar", -2);

        env = Collections.unmodifiableMap(env);
    }

    @Test
    public void test01_literals() {
        checkCompile(num(1), 1);
        checkCompile(num(0), 0);
        checkCompile(num(-1), -1);
    }
    @Test
    public void test02_vars() {
        checkCompile(var("a"), 1);
        checkCompile(var("b"), 0);
        checkCompile(var("c"), 10);
    }

    @Test
    public void test03_ops() {
        checkCompile(add(num(2), num(3)), 5);
        checkCompile(mul(num(2), num(3)), 6);
        checkCompile(neg(num(2)), -2);
        checkCompile(div(num(10), num(2)), 5);
        checkCompile(div(num(1), num(2)), 0);
    }

    @Test
    public void test04_recover() {
        checkCompile(div(num(4), num(0), num(5)), 5);
        checkCompile(div(num(4), num(2), num(5)), 2);
        checkCompile(div(num(4), add(num(2), neg(num(2))), num(5)), 5);
        checkCompile(div(num(4), add(num(2), num(2)), num(5)), 1);
    }

    private static final SafdiDiv DIV0 = div(num(1), num(0));

    @Test
    public void test05_error() {
        checkCompileError(DIV0);
        checkCompileError(div(num(1), num(0), DIV0));
        checkCompileError(div(DIV0, num(0)));
        checkCompileError(div(div(num(1), num(0), num(5)), num(0)));
        checkCompileError(add(num(5), div(num(1), num(0))), 5);
    }

    @Test
    public void test06_error_recover() {
        checkCompile(div(num(1), num(0), num(5)), 5);
        checkCompile(div(var("jama"), num(0), num(5)), 5);
        checkCompile(div(DIV0, num(0), num(5)), 5);
        checkCompile(div(num(4), num(2), var("jama")), 2);
        checkCompile(div(num(4), num(2), DIV0), 2);
    }

    @Test
    public void test07_var_error() {
        fail("See test avalikustatakse pärast eksamit! Defineerimata muutuja korral vaja visata õige erind!");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }

    
    private void checkCompile(SafdiNode node, int expected) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Integer> entry : env.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(entry.getValue());
        }

        CMaProgram program = SafdiCompiler.compile(node, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        CMaStack expectedStack = new CMaStack(initialStack);
        expectedStack.push(expected);
        assertEquals(expectedStack, finalStack);
    }

    private void checkCompileError(SafdiNode node, int... expected) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Integer> entry : env.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(entry.getValue());
        }

        CMaProgram program = SafdiCompiler.compile(node, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        CMaStack expectedStack = new CMaStack(initialStack);
        for (int i : expected)
            expectedStack.push(i);
        assertEquals(expectedStack, finalStack);
    }
}
