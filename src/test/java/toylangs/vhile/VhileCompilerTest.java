package toylangs.vhile;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import toylangs.vhile.ast.VhileStmt;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static toylangs.vhile.ast.VhileNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VhileCompilerTest {

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    private Map<String, Integer> initialEnv;

    @Before
    public void setUp() {
        initialEnv = new HashMap<>();
        initialEnv.put("x", 1);
        initialEnv.put("y", 2);
        initialEnv.put("z", 3);
        initialEnv.put("foo", 4);
        initialEnv.put("bar", 5);

        initialEnv = Collections.unmodifiableMap(initialEnv);
    }

    @Test
    public void test01_num() {
        checkCompile(assign("x", num(10)), Map.of("x", 10));
        checkCompile(assign("x", num(-5)), Map.of("x", -5));
    }

    @Test
    public void test02_var() {
        checkCompile(assign("x", var("y")), Map.of("x", 2));
        checkCompile(assign("x", var("foo")), Map.of("x", 4));
        checkCompile(assign("x", var("bar")), Map.of("x", 5));
        checkCompileException(assign("x", var("a")));
    }

    @Test
    public void test03_ops() {
        checkCompile(assign("x", eq(num(2), num(2))), Map.of("x", 1));
        checkCompile(assign("x", eq(num(2), num(3))), Map.of("x", 0));
        checkCompile(assign("x", add(num(2), num(3))), Map.of("x", 5));
        checkCompile(assign("x", mul(num(2), num(3))), Map.of("x", 6));
    }

    @Test
    public void test04_assign() {
        checkCompile(assign("z", num(1)), Map.of("z", 1));
        checkCompile(assign("y", num(0)), Map.of("y", 0));
        checkCompile(assign("foo", num(100)), Map.of("foo", 100));
        checkCompileException(assign("a", num(10)));
    }

    @Test
    public void test05_block_loop() {
        checkCompile(block(assign("x", add(var("foo"), var("bar"))), assign("y", mul(var("foo"), var("bar")))), Map.of("x", 9, "y", 20));

        checkCompile(loop(neq(var("bar"), num(0)), block(assign("x", mul(var("x"), var("bar"))), assign("bar", add(var("bar"), num(-1))))), Map.of("bar", 0, "x", 120)); // faktoriaal
        checkCompile(loop(num(0), assign("x", num(10))), Map.of());

        checkCompile(loop(var("z"), block(
                assign("z", add(var("z"), num(-1)))
        )), Map.of("z", 0));
    }

    @Test
    public void test06_loop_escape() {
        checkCompile(loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), loop(eq(var("x"), num(5)), escape(2)))), Map.of("x", 5));
        checkCompile(loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), loop(eq(var("x"), num(5)), escape(1)))), Map.of("x", 10));

        checkCompile(loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), loop(eq(var("x"), num(5)), block(escape(2))))), Map.of("x", 5));

        checkCompile(escape(1), Map.of());
        checkCompile(block(escape(1), assign("x", num(5))), Map.of());

        checkCompile(loop(num(1),
                escape(1)
        ), Map.of());
        checkCompile(loop(num(1), block(
                escape(1),
                escape(1)
        )), Map.of());
        checkCompile(loop(num(1), block(
                escape(1),
                assign("x", num(5))
        )), Map.of());
    }

    @Test
    public void test07_multiple_no_escape() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt keerulisemad laused ilma escape-ita.");
    }

    @Test
    public void test08_multiple_escape() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt keerulisemad laused koos escape-iga.");
    }


    private void checkCompile(VhileStmt stmt, Map<String, Integer> expectedEnvDiff) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Integer> entry : initialEnv.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(entry.getValue());
        }

        CMaProgram program = VhileCompiler.compile(stmt, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        Map<String, Integer> expected = new HashMap<>(initialEnv);
        expected.putAll(expectedEnvDiff);
        CMaStack expectedStack = new CMaStack();
        for (String variable : variables) {
            expectedStack.push(expected.get(variable));
        }
        assertEquals(expectedStack, finalStack);
    }

    private void checkCompileException(VhileStmt stmt) {
        List<String> variables = new ArrayList<>(initialEnv.keySet());
        assertThrows(VhileException.class, () -> VhileCompiler.compile(stmt, variables));
    }
}
