package toylangs.safdi;

import toylangs.safdi.ast.SafdiDiv;
import toylangs.safdi.ast.SafdiNode;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static toylangs.safdi.ast.SafdiNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SafdiEvaluatorTest {

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
        checkEval(num(1), 1);
        checkEval(num(0), 0);
        checkEval(num(-1), -1);
    }

    @Test
    public void test02_vars() {
        checkEval(var("a"), 1);
        checkEval(var("b"), 0);
        checkEval(var("c"), 10);
    }

    @Test
    public void test03_ops() {
        checkEval(add(num(2), num(3)), 5);
        checkEval(mul(num(2), num(3)), 6);
        checkEval(neg(num(2)), -2);
        checkEval(div(num(10), num(2)), 5);
        checkEval(div(num(1), num(2)), 0);
    }

    @Test
    public void test04_recover() {
        checkEval(div(num(4), num(0), num(5)), 5);
        checkEval(div(num(4), num(2), num(5)), 2);
        checkEval(div(num(4), add(num(2), neg(num(2))), num(5)), 5);
        checkEval(div(num(4), add(num(2), num(2)), num(5)), 1);
    }

    private static final SafdiDiv DIV0 = div(num(1), num(0));

    @Test
    public void test05_error() {
        checkEvalError(DIV0);
        checkEvalError(div(num(1), num(0), DIV0));
        checkEvalError(div(DIV0, num(0)));
        checkEvalError(div(div(num(1), num(0), num(5)), num(0)));
        checkEvalError(add(num(5), div(num(1), num(0))));
    }

    @Test
    public void test06_error_recover() {
        checkEval(div(num(1), num(0), num(5)), 5);
        checkEval(div(var("jama"), num(0), num(5)), 5);
        checkEval(div(DIV0, num(0), num(5)), 5);
        checkEval(div(num(4), num(2), var("jama")), 2);
        checkEval(div(num(4), num(2), DIV0), 2);
    }

    @Test
    public void test07_var_error() {
        fail("See test avalikustatakse pärast eksamit! Defineerimata muutuja korral vaja visata õige erind!");
    }

    @Test
    public void test08_multiple() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }


    private void checkEval(SafdiNode node, int expected) {
        assertEquals(expected, SafdiEvaluator.eval(node, env));
    }

    private void checkEvalError(SafdiNode node) {
        try {
            SafdiEvaluator.eval(node, env);
            fail("expected SafdiException");
        }
        catch (SafdiEvaluator.SafdiException ignored) {

        }
    }
}
