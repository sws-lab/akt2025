package eksam1;

import eksam1.ast.PointyNode;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static eksam1.ast.PointyNode.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PointyEvaluatorTest {

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
        checkEval(assign("x", num(10)), Map.of("x", 10));
        checkEval(assign("z", num(1)), Map.of("z", 1));
        checkEval(assign("y", num(0)), Map.of("y", 0));
        checkEval(assign("z", num(10)), Map.of("z", 10));
        checkEval(assign("foo", num(100)), Map.of("foo", 100));

        checkEval(assign("x", var("y")), Map.of("x", 2));
        checkEval(assign("x", var("foo")), Map.of("x", 15));
        checkEval(assign("x", var("bar")), Map.of("x", 27));
    }

    @Test
    public void test02_ops() {
        checkEval(assign("x", sub(num(2), num(3))), Map.of("x", -1));
        checkEval(assign("x", neg(num(2))), Map.of("x", -2));
        checkEval(assign("x", sub(num(10), neg(num(2)))), Map.of("x", 12));
    }

    @Test
    public void test03_comma() {
        checkEval(comma(assign("x", var("y")), assign("y", var("z"))), Map.of("x", 2, "y", 3));
        checkEval(comma(assign("y", var("z")), assign("x", var("y"))), Map.of("x", 3, "y", 3));
        checkEval(comma(comma(assign("y", var("z"))), comma(assign("x", var("y")))), Map.of("x", 3, "y", 3));
    }

    @Test
    public void test04_addrof() {
        checkEval(assign("x", addrOf("x")), Map.of("x", 0));
        checkEval(assign("x", addrOf("foo")), Map.of("x", 3));
        checkEval(assign("x", addrOf("bar")), Map.of("x", 4));
    }

    @Test
    public void test05_deref() {
        checkEval(assign("x", deref(num(0))), Map.of("x", 1));
        checkEval(assign("x", deref(num(3))), Map.of("x", 15));
        checkEval(assign("x", deref(num(4))), Map.of("x", 27));
        checkEval(assign("x", deref(var("z"))), Map.of("x", 15));
        checkEval(assign("x", deref(addrOf("bar"))), Map.of("x", 27));
        checkEval(assign("x", deref(sub(addrOf("bar"), num(1)))), Map.of("x", 15));

        checkEvalException(assign("x", deref(num(9))));
        checkEvalException(assign("x", deref(num(-1))));
        checkEvalException(assign("x", deref(var("foo"))));
    }

    @Test
    public void test06_nested() {
        checkEval(assign("x", assign("y", num(10))), Map.of("x", 10, "y", 10));
        checkEval(assign("x", comma(num(5), num(10))), Map.of("x", 10));
        checkEval(assign("x", comma(assign("y", num(10)), num(20))), Map.of("x", 20, "y", 10));
        checkEval(comma(
                        assign("x", addrOf("foo")),
                        assign("y", addrOf("bar")),
                        assign("z", sub(deref(var("x")), neg(deref(var("y")))))),
                Map.of("x", 3, "y", 4, "z", 42));

        checkEvalException(sub(num(10), deref(num(9))));
    }

    @Test
    public void test07_exceptions() {
        fail("See test avalikustatakse pärast eksamit! Igasugune defineerimata muutuja kasutamine peaks viskama õige erindi!");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt keerulisemad avaldised.");
    }


    private void checkEval(PointyNode node, Map<String, Integer> expectedEnvDiff) {
        Map<String, Integer> expected = new HashMap<>(initialEnv);
        expected.putAll(expectedEnvDiff);
        assertEquals(expected, PointyEvaluator.eval(node, initialEnv, initialEnv.keySet().stream().toList()));
    }

    private void checkEvalException(PointyNode node) {
        assertThrows(PointyException.class, () -> PointyEvaluator.eval(node, initialEnv, initialEnv.keySet().stream().toList()));
    }
}
