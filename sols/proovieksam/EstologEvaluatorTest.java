package proovieksam;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import proovieksam.ast.EstologDef;
import proovieksam.ast.EstologNode;
import proovieksam.ast.EstologProg;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static proovieksam.ast.EstologNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EstologEvaluatorTest {

    private static final List<EstologDef> allDefs = Arrays.asList(
            def("x", lit(false)),
            def("y", lit(true)),
            def("z", lit(true)),
            def("a", lit(true)),
            def("b", lit(false)),
            def("c", lit(true))
    );

    @Test
    public void test01_literals() {
        checkEval(prog(lit(false)), false);  // 0
        checkEval(prog(lit(true)), true);  // 1
    }

    @Test
    public void test02_operators() {
        checkEval(prog(ja(lit(false), lit(true))), false);  // (0 JA 1)
        checkEval(prog(ja(lit(true), lit(true))), true);  // (1 JA 1)
        checkEval(prog(voi(lit(false), lit(true))), true);  // (0 VOI 1)
        checkEval(prog(voi(lit(false), lit(false))), false);  // (0 VOI 0)
        checkEval(prog(vordus(lit(false), lit(true))), false);  // (0 = 1)
        checkEval(prog(vordus(lit(false), lit(false))), true);  // (0 = 0)
    }

    @Test
    public void test03_variables() {
        checkEval(prog(var("x"), def("x", lit(false))), false);  // x
        checkEval(prog(var("y"), def("y", lit(true))), true);  // y
        checkEval(prog(vordus(var("x"), lit(false)), def("x", lit(false))), true);  // (x = 0)
        checkEval(prog(vordus(var("x"), var("c")), def("x", lit(false)), def("c", lit(true))), false);  // (x = c)

        // same with allDefs
        checkEvalAllDefsProg(var("x"), false);  // x
        checkEvalAllDefsProg(var("y"), true);  // y
        checkEvalAllDefsProg(vordus(var("x"), lit(false)), true);  // (x = 0)
        checkEvalAllDefsProg(vordus(var("x"), var("c")), false);  // (x = c)
    }

    @Test
    public void test04_kui() {
        checkEvalAllDefsProg(kui(lit(true), lit(true), lit(false)), true);  // (KUI 1 SIIS 1 MUIDU 0)
        checkEvalAllDefsProg(kui(lit(false), lit(true), lit(false)), false);  // (KUI 0 SIIS 1 MUIDU 0)
        checkEvalAllDefsProg(kui(lit(true), var("x"), var("z")), false);  // (KUI 1 SIIS x MUIDU z)

        // nested
        checkEvalAllDefsProg(kui(var("a"), kui(var("a"), lit(true), lit(false)), lit(false)), true);  // (KUI a SIIS (KUI a SIIS 1 MUIDU 0) MUIDU 0)
        checkEvalAllDefsProg(kui(var("a"), kui(var("b"), lit(false), lit(true)), lit(false)), true);  // (KUI a SIIS (KUI b SIIS 0 MUIDU 1) MUIDU 0)
        checkEvalAllDefsProg(kui(var("b"), kui(var("b"), lit(false), lit(false)), lit(true)), true);  // (KUI b SIIS (KUI b SIIS 0 MUIDU 0) MUIDU 1)
    }

    @Test
    public void test05_all() {
        checkEvalAllDefsProg(kui(var("b"), voi(var("x"), var("y")), ja(var("x"), lit(true))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA 1))
        checkEvalAllDefsProg(kui(var("b"), voi(var("x"), var("y")), ja(var("x"), var("y"))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA y))
        checkEvalAllDefsProg(kui(ja(var("b"), vordus(lit(false), lit(true))), voi(var("x"), var("y")), vordus(var("x"), var("y"))), false);  // (KUI (b JA (0 = 1)) SIIS (x VOI y) MUIDU (x = y))
        checkEvalAllDefsProg(voi(voi(ja(var("x"), var("y")), ja(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a JA z)) VOI (b JA c))
        checkEvalAllDefsProg(voi(voi(ja(var("x"), var("y")), vordus(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a = z)) VOI (b JA c))
    }

    @Test
    public void test06_defines() {
        // non-literal define
        checkEval(prog(var("x"), def("x", vordus(lit(false), lit(false)))), true); // x := (0 = 0); x

        // dependent define
        checkEval(prog(var("y"), def("x", lit(false)), def("y", vordus(var("x"), lit(false)))), true); // x := 0; y := (x = 0); y

        // redefine
        checkEval(prog(var("x"), def("x", lit(true)), def("x", lit(false))), false); // x := 1; x := 0; x

        // dependent redefine
        checkEval(prog(var("x"), def("x", lit(false)), def("x", vordus(var("x"), lit(false)))), true); // x := 0; x := (x = 0); x
    }

    @Test
    public void test07_kui_without_muidu() {
        checkEvalAllDefsProg(kui(lit(true), lit(false)), false);  // (KUI 1 SIIS 0)
        checkEvalAllDefsProg(kui(lit(false), lit(false)), true);  // (KUI 0 SIIS 0)
        checkEvalAllDefsProg(kui(lit(true), var("x")), false);  // (KUI 1 SIIS x)
        checkEvalAllDefsProg(kui(lit(false), var("x")), true);  // (KUI 0 SIIS x)

        checkEvalAllDefsProg(kui(var("b"), voi(lit(false), var("y"))), true);  // (KUI b SIIS (0 VOI y))
        checkEvalAllDefsProg(kui(var("b"), voi(var("x"), var("y"))), true);  // (KUI b SIIS (x VOI y))
    }

    @Test(expected = NoSuchElementException.class)
    public void test08_variable_undefined() {
        EstologEvaluator.eval(prog(var("y"), def("x", lit(true)))); // x := 1; y
    }

    private void checkEval(EstologProg prog, boolean expected) {
        assertEquals(expected, EstologEvaluator.eval(prog));
    }

    private void checkEvalAllDefsProg(EstologNode node, boolean expected) {
        checkEval(prog(node, allDefs), expected);
    }
}
