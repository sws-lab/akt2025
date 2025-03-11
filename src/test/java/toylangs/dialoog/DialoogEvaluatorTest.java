package toylangs.dialoog;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.dialoog.ast.DialoogDecl;
import toylangs.dialoog.ast.DialoogNode;
import toylangs.dialoog.DialoogEvaluator.UndeclaredVariableException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.dialoog.ast.DialoogNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DialoogEvaluatorTest {

    private List<DialoogDecl> evalDecls;
    private Map<String, Object> evalEnv;

    @Before
    public void setUp() {
        evalDecls = decls(bv("a"), bv("b"), iv("x"), iv("y"), iv("z"));

        evalEnv = new HashMap<>();

        evalEnv.put("a", true);
        evalEnv.put("b", false);

        evalEnv.put("x", 1);
        evalEnv.put("y", 2);
        evalEnv.put("z", 6);
    }


    @Test
    public void test01_lit() {
        checkEval(1, prog(evalDecls, il(1)));
        checkEval(42, prog(evalDecls, il(42)));
        checkEval(true, prog(evalDecls, bl(true)));
        checkEval(false, prog(evalDecls, bl(false)));
    }

    @Test
    public void test02_var() {
        checkEval(2, prog(evalDecls, var("y")));
        checkEval(false, prog(evalDecls, var("b")));
        checkEval(null, prog(evalDecls, var("k"))); // deklareerimata & keskkonnas puuduv muutuja
        checkEval(null, prog(decls(), var("x"))); // deklareerimata muutuja!!
    }

    @Test
    public void test03_ops() {
        checkEval(-1, prog(evalDecls, neg(var("x"))));
        checkEval(7, prog(evalDecls, add(var("x"), var("z"))));
        checkEval(2, prog(evalDecls, sub(div(var("z"), var("y")), var("x"))));

        checkEval(true, prog(evalDecls, and(var("a"), not(or(var("b"), bl(false))))));
        checkEval(false, prog(evalDecls, and(var("a"), not(or(var("b"), bl(true))))));

        checkEval(true, prog(evalDecls, eq(var("y"), il(2))));
        checkEval(false, prog(evalDecls, eq(var("b"), bl(true))));
        checkEval(false, prog(evalDecls, eq(var("a"), eq(var("z"), il(5)))));
        checkEval(true, prog(evalDecls, eq(var("a"), eq(var("z"), il(6)))));

        checkEval(true, prog(evalDecls, eq(il(300), sub(il(301), il(1))))); // eq peab kasutama Object.equals() mitte ==
    }

    @Test
    public void test04_ifte() {
        checkEval(1, prog(evalDecls, ifte(bl(true), var("x"), var("y"))));
        checkEval(2, prog(evalDecls, ifte(bl(false), var("x"), var("y"))));
        checkEval(2, prog(evalDecls, ifte(eq(var("x"), var("z")), var("z"), var("y"))));

        checkEval(false, prog(evalDecls, ifte(var("a"), bl(false), bl(true))));
        checkEval(true, prog(evalDecls, ifte(var("b"), bl(false), bl(true))));

        checkEval(20, prog(evalDecls, ifte(bl(true), ifte(bl(false), il(10), il(20)), il(30))));
    }

    @Test
    public void test05_demo() {
        checkEval(13,
                prog(decls(bv("a"), iv("x")),
                        add(add(sub(mul(il(2), add(il(2), il(2))),
                                ifte(eq(add(il(5), il(5)), il(10)),
                                        ifte(eq(add(il(5), il(5)), il(10)),
                                                mul(il(35), var("x")),
                                                il(100)),
                                        il(100))
                        ), il(30)), ifte(and(bl(true), var("a")), il(10), il(300)))));
    }

    private void checkEval(Object expected, DialoogNode prog) {
        if (expected != null) {
            Object actual = DialoogEvaluator.eval(prog, Collections.unmodifiableMap(evalEnv));
            assertEquals(expected, actual);
        }
        else {
            try {
                DialoogEvaluator.eval(prog, Collections.unmodifiableMap(evalEnv));
                fail("Muutuja on deklareerimata, aga eval ei visanud UndeclaredVariableException");
            } catch (UndeclaredVariableException ignored) {

            }
        }
    }

}
