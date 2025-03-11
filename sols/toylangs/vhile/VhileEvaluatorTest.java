package toylangs.vhile;

import toylangs.vhile.ast.VhileStmt;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static toylangs.vhile.ast.VhileNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VhileEvaluatorTest {

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
        checkEval(assign("x", num(10)), Map.of("x", 10));
        checkEval(assign("x", num(-5)), Map.of("x", -5));
    }

    @Test
    public void test02_var() {
        checkEval(assign("x", var("y")), Map.of("x", 2));
        checkEval(assign("x", var("foo")), Map.of("x", 4));
        checkEval(assign("x", var("bar")), Map.of("x", 5));
        checkEvalException(assign("x", var("a")));
    }

    @Test
    public void test03_ops() {
        checkEval(assign("x", eq(num(2), num(2))), Map.of("x", 1));
        checkEval(assign("x", eq(num(2), num(3))), Map.of("x", 0));
        checkEval(assign("x", add(num(2), num(3))), Map.of("x", 5));
        checkEval(assign("x", mul(num(2), num(3))), Map.of("x", 6));
    }

    @Test
    public void test04_assign() {
        checkEval(assign("z", num(1)), Map.of("z", 1));
        checkEval(assign("y", num(0)), Map.of("y", 0));
        checkEval(assign("foo", num(100)), Map.of("foo", 100));
        checkEvalException(assign("a", num(10)));
    }

    @Test
    public void test05_block_loop() {
        checkEval(block(assign("x", add(var("foo"), var("bar"))), assign("y", mul(var("foo"), var("bar")))), Map.of("x", 9, "y", 20));

        checkEval(loop(neq(var("bar"), num(0)), block(assign("x", mul(var("x"), var("bar"))), assign("bar", add(var("bar"), num(-1))))), Map.of("bar", 0, "x", 120)); // faktoriaal
        checkEval(loop(num(0), assign("x", num(10))), Map.of());

        checkEval(loop(var("z"), block(
                assign("z", add(var("z"), num(-1)))
        )), Map.of("z", 0));
    }

    @Test
    public void test06_loop_escape() {
        checkEval(loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), loop(eq(var("x"), num(5)), escape(2)))), Map.of("x", 5));
        checkEval(loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), loop(eq(var("x"), num(5)), escape(1)))), Map.of("x", 10));

        checkEval(loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), loop(eq(var("x"), num(5)), block(escape(2))))), Map.of("x", 5));

        checkEval(escape(1), Map.of());
        checkEval(block(escape(1), assign("x", num(5))), Map.of());

        checkEval(loop(num(1),
                escape(1)
        ), Map.of());
        checkEval(loop(num(1), block(
                escape(1),
                escape(1)
        )), Map.of());
        checkEval(loop(num(1), block(
                escape(1),
                assign("x", num(5))
        )), Map.of());
    }

    @Test
    public void test07_multiple_no_escape() {
        checkEval(assign("x", num(2048)), Map.of("x", 2048));
        checkEval(assign("x", var("z")), Map.of("x", 3));

        checkEval(assign("x", add(num(2), mul(num(2), num(3)))), Map.of("x", 8));
        checkEval(assign("x", mul(add(num(2), num(3)), num(3))), Map.of("x", 15));

        checkEval(loop(var("x"), block(assign("y", num(10)), assign("x", num(0)))), Map.of("x", 0, "y", 10));
        checkEval(loop(var("z"), block(assign("y", num(10)), assign("z", num(0)))), Map.of("z", 0, "y", 10));
    }

    @Test
    public void test08_multiple_escape() {
        checkEval(loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), escape(1), assign("x", add(var("x"), num(1))))), Map.of("x", 2));
        checkEval(block(loop(num(1), escape(2)), assign("x", num(5))), Map.of());
        checkEval(block(loop(num(1), block(assign("y", num(7)), escape(2))), assign("x", num(5))), Map.of("y", 7));

        checkEval(
                loop(num(1), block(
                        loop(eq(var("z"), num(1)),
                             escape(1)
                        ),
                        loop(eq(var("z"), num(2)),
                             escape(2)
                        ),
                        assign("z", add(var("z"), num(-1)))
                     )
                ), Map.of("z", 2)
        );
    }


    private void checkEval(VhileStmt stmt, Map<String, Integer> expectedEnvDiff) {
        Map<String, Integer> expected = new HashMap<>(initialEnv);
        expected.putAll(expectedEnvDiff);
        assertEquals(expected, VhileEvaluator.eval(stmt, initialEnv));
    }

    private void checkEvalException(VhileStmt stmt) {
        assertThrows(VhileException.class, () -> VhileEvaluator.eval(stmt, initialEnv));
    }
}
