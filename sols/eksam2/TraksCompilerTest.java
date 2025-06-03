package eksam2;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import eksam2.ast.TraksStmt;
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

import static eksam2.ast.TraksNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TraksCompilerTest {

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    private Map<String, Integer> initialEnv;

    @Before
    public void setUp() {
        initialEnv = new HashMap<>();
        initialEnv.put("X", 1);
        initialEnv.put("Y", 2);
        initialEnv.put("Z", 3);
        initialEnv.put("Foo", 4);
        initialEnv.put("Bar", 5);
        initialEnv.put("Raha", 1000);
        initialEnv.put("Tulemus", 0);

        initialEnv = Collections.unmodifiableMap(initialEnv);
    }

    @Test
    public void test01_numvar() {
        checkCompile(assign("X", num(10)), Map.of("X", 10));
        checkCompile(assign("X", num(-5)), Map.of("X", -5));
        checkCompile(assign("Z", num(1)), Map.of("Z", 1));
        checkCompile(assign("Y", num(0)), Map.of("Y", 0));
        checkCompile(assign("Foo", num(100)), Map.of("Foo", 100));

        checkCompile(assign("X", var("Y")), Map.of("X", 2));
        checkCompile(assign("X", var("Foo")), Map.of("X", 4));
        checkCompile(assign("X", var("Bar")), Map.of("X", 5));
    }

    @Test
    public void test02_ops() {
        checkCompile(assign("X", add(num(2), num(3))), Map.of("X", 5));
        checkCompile(assign("X", sub(num(2), num(3))), Map.of("X", -1));

        checkCompile(assign("X", geq(num(3), num(3))), Map.of("X", 1));
        checkCompile(assign("X", geq(num(4), num(3))), Map.of("X", 1));
        checkCompile(assign("X", geq(num(2), num(3))), Map.of("X", 0));
    }

    @Test
    public void test03_blocks() {
        checkCompile(block(), Map.of());
        checkCompile(block(assign("X", var("Y")), assign("Y", var("Z"))), Map.of("X", 2, "Y", 3));
        checkCompile(block(assign("Y", var("Z")), assign("X", var("Y"))), Map.of("X", 3, "Y", 3));
        checkCompile(block(block(assign("Y", var("Z"))), block(assign("X", var("Y")))), Map.of("X", 3, "Y", 3));

        checkCompileException(assign("X", var("A")));
        checkCompileException(assign("Boo", num(1)));
    }

    @Test
    public void test04_abort() {
        checkCompile(action(block(check(num(1)), assign("X", num(10)))), Map.of("X", 10));
        checkCompile(action(block(check(num(0)), assign("X", num(10)))), Map.of());

        checkCompileException(action(assign("A", num(1))));
    }

    @Test
    public void test05_restore() {
        checkCompile(action(block(assign("X", num(10)), check(num(1)))), Map.of("X", 10));
        checkCompile(action(block(assign("X", num(10)), check(num(0)))), Map.of());

        checkCompile(block(
                        assign("X", num(10)),
                        action(block(
                                assign("Y", num(20)),
                                check(num(1))))),
                Map.of("X", 10, "Y", 20));
        checkCompile(block(
                        assign("X", num(10)),
                        action(block(
                                assign("Y", num(20)),
                                check(num(0))))),
                Map.of("X", 10));
    }

    @Test
    public void test06_nested() {
        // tehing katkeb!
        checkCompile(block(
                        assign("Raha", num(10)),
                        action(block(
                                assign("Y", sub(var("Y"), var("Raha"))),
                                check(geq(var("Y"), num(0))),
                                assign("X", add(var("X"), var("Raha"))))),
                        assign("Tulemus", var("X"))),
                Map.of("Raha", 10, "Tulemus", 1));

        // tehing toimub!
        checkCompile(block(
                        assign("Raha", num(1)),
                        action(block(
                                assign("Y", sub(var("Y"), var("Raha"))),
                                check(geq(var("Y"), num(0))),
                                assign("X", add(var("X"), var("Raha"))))),
                        assign("Tulemus", var("X"))),
                Map.of("Raha", 1, "Tulemus", 2, "Y", 1, "X", 2));

        checkCompile(
                block(
                        assign("Y", num(9)),
                        action(block(
                                assign("X", var("Y")),
                                check(var("Y"))))),
                Map.of("Y", 9, "X", 9));

        checkCompile(
                block(
                        assign("Y", num(0)),
                        action(block(
                                assign("X", var("Y")),
                                check(var("Y"))))),
                Map.of("Y", 0));

        checkCompile(action(block(
                        assign("X", num(10)),
                        action(assign("Y", num(20))))),
                Map.of("X", 10, "Y", 20));
        checkCompile(action(block(
                        assign("X", num(10)),
                        action(block(
                                check(num(0)),
                                assign("Y", num(20)))))),
                Map.of("X", 10));

        checkCompile(action(block(
                assign("X", num(10)),
                action(block(
                        assign("Y", num(20)),
                        check(num(1)))))),
                Map.of("X", 10, "Y", 20));
        checkCompile(action(block(
                assign("X", num(10)),
                action(block(
                        assign("Y", num(20)),
                        check(num(0)))))),
                Map.of("X", 10));
    }

    @Test
    public void test07_default() {
        checkCompile(check(num(0)), Map.of());
        checkCompile(block(assign("X", num(10)), check(num(0))), Map.of());
        checkCompile(block(assign("X", num(10)), check(num(1))), Map.of("X", 10));
    }

    @Test
    public void test08_multiple() {
        checkCompile(action(block(assign("X", num(10)), action(block(assign("Y", num(20)), check(num(1)))))), Map.of("X", 10, "Y", 20));
        checkCompile(action(block(assign("X", num(10)), action(block(assign("Y", num(20)), check(num(0)))))), Map.of("X", 10));
        checkCompile(action(block(assign("X", num(10)), action(block(assign("Y", num(0)), check(num(0)))), check(var("Y")))), Map.of("X", 10));
        checkCompile(action(block(assign("X", num(10)), action(block(assign("Y", num(0)), check(num(1)))), check(var("Y")))), Map.of());

        checkCompile(action(block(assign("X", num(20)), block(assign("Y", num(30)), check(num(0)), assign("Z", num(40))), assign("Foo", var("X")))), Map.of()); // action-i ja check-i vahel kaks block-i
    }

    // täiendavad testid, mida eksamil ei kasutatud
    @Test
    public void test09_advanced() {
        // nested implicit default
        checkCompile(block(assign("X", num(10)), action(block(assign("Y", num(0)), check(num(0)))), check(var("Y"))), Map.of("X", 10)); // not restored
        checkCompile(block(assign("X", num(10)), action(block(assign("Y", num(0)), check(num(1)))), check(var("Y"))), Map.of()); // restored
    }

    private void checkCompile(TraksStmt stmt, Map<String, Integer> expectedEnvDiff) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Integer> entry : initialEnv.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(entry.getValue());
        }

        CMaProgram program = TraksCompiler.compile(stmt, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        Map<String, Integer> expected = new HashMap<>(initialEnv);
        expected.putAll(expectedEnvDiff);
        CMaStack expectedStack = new CMaStack();
        for (String variable : variables) {
            expectedStack.push(expected.get(variable));
        }
        assertEquals(expectedStack, finalStack);
    }

    private void checkCompileException(TraksStmt stmt) {
        List<String> variables = new ArrayList<>(initialEnv.keySet());
        assertThrows(TraksException.class, () -> TraksCompiler.compile(stmt, variables));
    }
}
