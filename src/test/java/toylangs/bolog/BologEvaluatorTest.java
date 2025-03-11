package toylangs.bolog;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.bolog.ast.BologNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static toylangs.bolog.ast.BologNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BologEvaluatorTest {

    @SafeVarargs
    private static <T> Set<T> setOf(T... elems) {
        return new HashSet<>(Arrays.asList(elems));
    }

    @Test
    public void test01_bool() {
        checkEval(true, var("X"), setOf("X"));
        checkEval(false, var("X"), setOf());

        checkEval(true, nand(var("P"), var("Q")), setOf());
        checkEval(true, nand(var("P"), var("Q")), setOf("P"));
        checkEval(true, nand(var("P"), var("Q")), setOf("Q"));
        checkEval(false, nand(var("P"), var("Q")), setOf("P", "Q"));

        checkEval(true, nand(var("P"), var("Q")), setOf());
        checkEval(true, nand(var("P"), var("Q")), setOf("P"));
        checkEval(true, nand(var("P"), var("Q")), setOf("Q"));
        checkEval(false, nand(var("P"), var("Q")), setOf("P", "Q"));
    }

    @Test
    public void test02_imp() {
        checkEval(true, imp(var("X")), setOf("X"));
        checkEval(false, imp(var("X")), setOf());
        checkEval(true, imp(var("X"), var("Y"), var("Z")), setOf("Y"));
        checkEval(true, imp(var("X"), var("Y"), var("Z")), setOf("Z", "Y", "X"));
        checkEval(false, imp(var("X"), var("Y"), var("Z")), setOf("Z", "Y"));

        checkEval(false, imp(nand(tv(true), nand(var("X"), var("P"))), var("P"),
                nand(nand(tv(true), var("P")),
                        nand(tv(true), var("Q")))),
                setOf("P", "Q"));
    }

    private static void checkEval(Boolean expected, BologNode prog, Set<String> tv) {
        Boolean actual = BologEvaluator.eval(prog, tv);
        assertEquals(expected, actual);
    }

}
