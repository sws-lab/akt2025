package eksam1;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import eksam1.ast.PointyNode;
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

import static eksam1.ast.PointyNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PointyCompilerTest {

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    private Map<String, Integer> initialEnv;

    @Before
    public void setUp() {
        initialEnv = new LinkedHashMap<>(); // linked to fix variable order!
        initialEnv.put("x", 1);
        initialEnv.put("y", 2);
        initialEnv.put("z", 3);
        initialEnv.put("foo", 15);
        initialEnv.put("bar", 27);

        initialEnv = Collections.unmodifiableMap(initialEnv);
    }

    @Test
    public void test01_numvar() {
        checkCompile(assign("x", num(10)), Map.of("x", 10));
        checkCompile(assign("z", num(1)), Map.of("z", 1));
        checkCompile(assign("y", num(0)), Map.of("y", 0));
        checkCompile(assign("z", num(10)), Map.of("z", 10));
        checkCompile(assign("foo", num(100)), Map.of("foo", 100));

        checkCompile(assign("x", var("y")), Map.of("x", 2));
        checkCompile(assign("x", var("foo")), Map.of("x", 15));
        checkCompile(assign("x", var("bar")), Map.of("x", 27));
    }

    @Test
    public void test02_ops() {
        checkCompile(assign("x", sub(num(2), num(3))), Map.of("x", -1));
        checkCompile(assign("x", neg(num(2))), Map.of("x", -2));
        checkCompile(assign("x", sub(num(10), neg(num(2)))), Map.of("x", 12));
    }

    @Test
    public void test03_comma() {
        checkCompile(comma(assign("x", var("y")), assign("y", var("z"))), Map.of("x", 2, "y", 3));
        checkCompile(comma(assign("y", var("z")), assign("x", var("y"))), Map.of("x", 3, "y", 3));
        checkCompile(comma(comma(assign("y", var("z"))), comma(assign("x", var("y")))), Map.of("x", 3, "y", 3));
    }

    @Test
    public void test04_addrof() {
        checkCompile(assign("x", addrOf("x")), Map.of("x", 0));
        checkCompile(assign("x", addrOf("foo")), Map.of("x", 3));
        checkCompile(assign("x", addrOf("bar")), Map.of("x", 4));
    }

    @Test
    public void test05_deref() {
        checkCompile(assign("x", deref(num(0))), Map.of("x", 1));
        checkCompile(assign("x", deref(num(3))), Map.of("x", 15));
        checkCompile(assign("x", deref(num(4))), Map.of("x", 27));
        checkCompile(assign("x", deref(var("z"))), Map.of("x", 15));
        checkCompile(assign("x", deref(addrOf("bar"))), Map.of("x", 27));
        checkCompile(assign("x", deref(sub(addrOf("bar"), num(1)))), Map.of("x", 15));

        checkCompile(assign("x", deref(num(9))), Map.of(), 9);
        checkCompile(assign("x", deref(num(-1))), Map.of(), -1);
        checkCompile(assign("x", deref(var("foo"))), Map.of(), 15);
    }

    @Test
    public void test06_nested() {
        checkCompile(assign("x", assign("y", num(10))), Map.of("x", 10, "y", 10));
        checkCompile(assign("x", comma(num(5), num(10))), Map.of("x", 10));
        checkCompile(assign("x", comma(assign("y", num(10)), num(20))), Map.of("x", 20, "y", 10));
        checkCompile(comma(
                        assign("x", addrOf("foo")),
                        assign("y", addrOf("bar")),
                        assign("z", sub(deref(var("x")), neg(deref(var("y")))))),
                Map.of("x", 3, "y", 4, "z", 42));

        checkCompile(sub(num(10), deref(num(9))), Map.of(), 10, 9);
    }

    @Test
    public void test07_exceptions() {
        checkCompileException(var("a"));
        checkCompileException(addrOf("a"));
        checkCompileException(assign("x", var("a")));
        checkCompileException(assign("x", var("Foo")));
        checkCompileException(assign("x", addrOf("a")));
        checkCompileException(assign("a", var("x")));
    }

    @Test
    public void test08_multiple() {
        checkCompile(comma(num(10), var("x"), addrOf("y"), deref(num(3))), Map.of());

        checkCompile(sub(comma(assign("x", num(5)), num(10)), deref(num(9))), Map.of("x", 5), 10, 9);

        checkCompile(sub(assign("x", var("y")), assign("y", var("z"))), Map.of("x", 2, "y", 3));
        checkCompile(sub(assign("y", var("z")), assign("x", var("y"))), Map.of("x", 3, "y", 3));

        checkCompile(comma(assign("x", num(1)), assign("y", deref(num(0)))), Map.of("y", 1));

        // Alusosa: 9. Sama alamavaldist ei väärtustata mitu korda.
        checkCompile(deref(assign("x", sub(var("x"), neg(num(1))))), Map.of("x", 2));
        checkCompile(comma(assign("x", sub(var("x"), neg(num(1)))), assign("x", sub(var("x"), neg(num(1))))), Map.of("x", 3));
        checkCompile(assign("y", assign("x", sub(var("x"), neg(num(1))))), Map.of("x", 2, "y", 2));
    }


    private void checkCompile(PointyNode node, Map<String, Integer> expectedEnvDiff, int... expectedExtra) {
        List<String> variables = new ArrayList<>();
        CMaStack initialStack = new CMaStack();
        for (Map.Entry<String, Integer> entry : initialEnv.entrySet()) {
            variables.add(entry.getKey());
            initialStack.push(entry.getValue());
        }

        CMaProgram program = PointyCompiler.compile(node, variables);
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);

        Map<String, Integer> expected = new HashMap<>(initialEnv);
        expected.putAll(expectedEnvDiff);
        CMaStack expectedStack = new CMaStack();
        for (String variable : variables) {
            expectedStack.push(expected.get(variable));
        }
        for (int extra : expectedExtra) {
            expectedStack.push(extra);
        }
        assertEquals(expectedStack, finalStack);
    }

    private void checkCompileException(PointyNode node) {
        List<String> variables = new ArrayList<>(initialEnv.keySet());
        assertThrows(PointyException.class, () -> PointyCompiler.compile(node, variables));
    }
}
