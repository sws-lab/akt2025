package week4.baselangs.bool;

import org.junit.Test;
import week4.baselangs.bool.ast.BoolNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static week4.baselangs.bool.ast.BoolNode.*;

public class BoolEvaluatorTest {

    public static final BoolNode exp1 = not(not(not(var('A'))));
    public static final BoolNode exp2 = not(imp(var('B'), var('C')));
    public static final BoolNode exp3 = not(imp(or(var('E'), var('A')), not(var('A'))));
    public static final BoolNode exp4 = not(or(or(var('F'), var('D')), not(or(var('F'), var('C')))));
    public static final BoolNode exp5 = not(imp(or(or(var('A'), var('A')), imp(var('A'), var('A'))), or(var('A'), var('A'))));
    public static final BoolNode exp6 = or(imp(imp(var('D'), var('C')), or(var('D'), var('C'))), or(imp(var('B'), var('E')), not(var('C'))));
    public static final BoolNode exp10 = not(imp(or(var('B'), var('A')), or(var('C'), var('C'))));
    public static final BoolNode exp11 = or(not(or(var('E'), var('A'))), imp(or(var('B'), var('C')), imp(var('C'), var('C'))));
    public static final BoolNode exp12 = or(not(or(or(var('D'), var('E')), or(var('F'), var('E')))), not(imp(not(var('E')), not(var('A')))));
    public static final BoolNode exp13 = not(or(or(var('B'), var('D')), not(or(var('D'), var('A')))));
    public static final BoolNode exp14 = or(not(or(or(var('A'), var('A')), or(var('A'), var('A')))), or(or(var('A'), var('A')), imp(var('A'), var('A'))));
    public static final BoolNode exp15 = not(or(or(not(var('C')), or(var('B'), var('C'))), or(not(var('A')), or(var('C'), var('A')))));
    public static final BoolNode exp16 = not(not(not(not(or(not(var('A')), or(var('A'), var('A')))))));
    public static final BoolNode exp17 = or(or(not(not(var('B'))), or(not(var('B')), or(var('A'), var('B')))), not(or(not(not(var('A'))), or(or(var('B'), var('A')), or(var('B'), var('C'))))));
    public static final BoolNode exp18 = or(not(or(or(or(var('F'), var('E')), imp(var('B'), var('B'))), not(or(var('D'), var('B'))))), or(or(or(or(var('B'), var('C')), or(var('E'), var('B'))), or(var('F'), var('D'))), imp(not(var('B')), or(var('F'), var('E')))));
    public static final BoolNode exp19 = or(not(not(or(or(or(var('B'), var('A')), not(var('B'))), imp(or(var('B'), var('A')), not(not(var('A'))))))), or(or(or(var('A'), var('B')), or(var('A'), var('B'))), not(not(not(not(var('B')))))));
    public static final BoolNode exp20 = or(or(not(or(or(var('B'), var('A')), or(var('B'), var('B')))), imp(or(not(var('C')), or(var('A'), var('A'))), imp(not(var('C')), not(var('C'))))), or(not(or(or(var('C'), var('B')), or(var('A'), var('C')))), or(not(not(or(var('C'), var('B')))), not(or(or(var('A'), var('C')), not(var('C')))))));
    public static final BoolNode exp21 = not(or(or(or(imp(not(var('A')), or(var('A'), var('A'))), not(not(var('A')))), or(or(or(var('B'), var('B')), not(var('C'))), or(var('C'), var('B')))), or(imp(not(or(var('C'), var('A'))), not(imp(var('A'), var('A')))), not(not(or(var('C'), var('B')))))));
    public static final BoolNode exp22 = not(or(not(or(or(not(or(not(var('A')), or(var('A'), var('A')))), or(not(var('A')), not(var('A')))), not(or(or(not(var('A')), or(var('A'), var('A'))), or(var('A'), var('A')))))), not(or(not(imp(or(var('A'), var('A')), or(var('A'), var('A')))), or(or(or(var('A'), var('A')), not(imp(var('A'), var('A')))), or(not(not(var('A'))), not(or(var('A'), var('A')))))))));
    public static final BoolNode exp23 = not(or(or(or(not(or(var('B'), var('B'))), not(or(var('A'), var('B')))), not(imp(not(not(var('E'))), not(or(var('A'), var('A')))))), not(not(not(or(not(var('A')), not(var('B'))))))));
    public static final BoolNode exp24 = or(or(not(or(not(not(or(var('A'), var('A')))), imp(not(or(var('A'), var('A'))), imp(var('A'), var('A'))))), or(or(or(not(not(var('A'))), or(or(var('A'), var('A')), not(var('A')))), not(or(or(var('A'), var('A')), not(var('A'))))), not(not(or(var('A'), var('A')))))), not(not(not(or(not(or(var('A'), var('A'))), or(var('A'), var('A')))))));
    public static final BoolNode exp25 = not(or(or(or(not(or(or(or(var('A'), var('A')), not(var('A'))), not(or(var('A'), var('A'))))), or(not(or(var('A'), var('A'))), or(or(var('A'), var('A')), or(var('A'), var('A'))))), or(not(or(or(not(var('A')), or(var('A'), var('A'))), or(or(var('A'), var('A')), or(var('A'), var('A'))))), not(or(or(or(var('A'), var('A')), not(var('A'))), or(var('A'), var('A')))))), not(or(not(not(not(imp(var('A'), var('A'))))), imp(imp(or(var('A'), var('A')), or(var('A'), var('A'))), or(not(var('A')), or(var('A'), var('A'))))))));

    @SafeVarargs
    private static <T> Set<T> set(T... characters) {
        return new HashSet<>(Arrays.asList(characters));
    }

    @Test
    public void test01_boolConst() {
        assertFalse(BoolEvaluator.eval(var('A'), set()));
        assertTrue(BoolEvaluator.eval(var('A'), set('A')));
        assertFalse(BoolEvaluator.eval(var('B'), set('A')));
    }

    @Test
    public void test02_boolOps() {
        assertFalse(BoolEvaluator.eval(var('A'), set()));
        assertTrue(BoolEvaluator.eval(var('A'), set('A')));

        assertTrue(BoolEvaluator.eval(not(var('A')), set()));
        assertFalse(BoolEvaluator.eval(not(var('A')), set('A')));

        assertFalse(BoolEvaluator.eval(or(var('A'), var('B')), set()));
        assertTrue(BoolEvaluator.eval(or(var('A'), var('B')), set('A')));
        assertTrue(BoolEvaluator.eval(or(var('A'), var('B')), set('B')));
        assertTrue(BoolEvaluator.eval(or(var('A'), var('B')), set('A', 'B')));

        assertTrue(BoolEvaluator.eval(imp(var('A'), var('B')), set()));
        assertFalse(BoolEvaluator.eval(imp(var('A'), var('B')), set('A')));
        assertTrue(BoolEvaluator.eval(imp(var('A'), var('B')), set('B')));
        assertTrue(BoolEvaluator.eval(imp(var('A'), var('B')), set('A', 'B')));
    }

    @Test
    public void test03_boolCombo() {
        assertFalse(BoolEvaluator.eval(var('A'), set()));
        assertTrue(BoolEvaluator.eval(var('A'), set('A')));
        assertFalse(BoolEvaluator.eval(var('B'), set('A')));

        assertTrue(BoolEvaluator.eval(exp1, set()));
        assertFalse(BoolEvaluator.eval(exp1, set('A')));

        // not(B -> C)
        assertFalse(BoolEvaluator.eval(exp2, set()));
        assertTrue(BoolEvaluator.eval(exp2, set('B')));
        assertFalse(BoolEvaluator.eval(exp2, set('C')));
        assertFalse(BoolEvaluator.eval(exp2, set('B', 'C')));

        assertTrue(BoolEvaluator.eval(exp5, set()));
        assertFalse(BoolEvaluator.eval(exp5, set('A')));
    }

    @Test
    public void test04_boolGen1() {
        assertFalse(BoolEvaluator.eval(exp10, set()));
        assertTrue(BoolEvaluator.eval(exp10, set('A')));
        assertTrue(BoolEvaluator.eval(exp10, set('B')));
        assertTrue(BoolEvaluator.eval(exp10, set('A', 'B')));
        assertFalse(BoolEvaluator.eval(exp10, set('C')));
        assertFalse(BoolEvaluator.eval(exp10, set('A', 'C')));
        assertFalse(BoolEvaluator.eval(exp10, set('B', 'C')));
        assertFalse(BoolEvaluator.eval(exp10, set('A', 'B', 'C')));
        assertTrue(BoolEvaluator.eval(exp11, set()));
        assertTrue(BoolEvaluator.eval(exp11, set('A')));
        assertTrue(BoolEvaluator.eval(exp11, set('B')));
        assertTrue(BoolEvaluator.eval(exp11, set('A', 'B')));
        assertTrue(BoolEvaluator.eval(exp11, set('C')));
        assertTrue(BoolEvaluator.eval(exp11, set('A', 'C')));
        assertTrue(BoolEvaluator.eval(exp11, set('B', 'C')));
        assertTrue(BoolEvaluator.eval(exp11, set('A', 'B', 'C')));
        assertTrue(BoolEvaluator.eval(exp11, set('E')));
        assertTrue(BoolEvaluator.eval(exp11, set('A', 'E')));
        assertTrue(BoolEvaluator.eval(exp11, set('B', 'E')));
        assertTrue(BoolEvaluator.eval(exp11, set('A', 'B', 'E')));
        assertTrue(BoolEvaluator.eval(exp11, set('C', 'E')));
        assertTrue(BoolEvaluator.eval(exp11, set('A', 'C', 'E')));
        assertTrue(BoolEvaluator.eval(exp11, set('B', 'C', 'E')));
        assertTrue(BoolEvaluator.eval(exp11, set('A', 'B', 'C', 'E')));
        assertTrue(BoolEvaluator.eval(exp12, set()));
        assertTrue(BoolEvaluator.eval(exp12, set('A')));
        assertFalse(BoolEvaluator.eval(exp12, set('D')));
        assertTrue(BoolEvaluator.eval(exp12, set('A', 'D')));
        assertFalse(BoolEvaluator.eval(exp12, set('E')));
        assertFalse(BoolEvaluator.eval(exp12, set('A', 'E')));
        assertFalse(BoolEvaluator.eval(exp12, set('D', 'E')));
        assertFalse(BoolEvaluator.eval(exp12, set('A', 'D', 'E')));
        assertFalse(BoolEvaluator.eval(exp12, set('F')));
        assertTrue(BoolEvaluator.eval(exp12, set('A', 'F')));
        assertFalse(BoolEvaluator.eval(exp12, set('D', 'F')));
        assertTrue(BoolEvaluator.eval(exp12, set('A', 'D', 'F')));
        assertFalse(BoolEvaluator.eval(exp12, set('E', 'F')));
        assertFalse(BoolEvaluator.eval(exp12, set('A', 'E', 'F')));
        assertFalse(BoolEvaluator.eval(exp12, set('D', 'E', 'F')));
        assertFalse(BoolEvaluator.eval(exp12, set('A', 'D', 'E', 'F')));
        assertFalse(BoolEvaluator.eval(exp13, set()));
        assertTrue(BoolEvaluator.eval(exp13, set('A')));
        assertFalse(BoolEvaluator.eval(exp13, set('B')));
        assertFalse(BoolEvaluator.eval(exp13, set('A', 'B')));
        assertFalse(BoolEvaluator.eval(exp13, set('D')));
        assertFalse(BoolEvaluator.eval(exp13, set('A', 'D')));
        assertFalse(BoolEvaluator.eval(exp13, set('B', 'D')));
        assertFalse(BoolEvaluator.eval(exp13, set('A', 'B', 'D')));
        assertTrue(BoolEvaluator.eval(exp14, set()));
        assertTrue(BoolEvaluator.eval(exp14, set('A')));
        assertFalse(BoolEvaluator.eval(exp15, set()));
        assertFalse(BoolEvaluator.eval(exp15, set('A')));
        assertFalse(BoolEvaluator.eval(exp15, set('B')));
        assertFalse(BoolEvaluator.eval(exp15, set('A', 'B')));
        assertFalse(BoolEvaluator.eval(exp15, set('C')));
        assertFalse(BoolEvaluator.eval(exp15, set('A', 'C')));
        assertFalse(BoolEvaluator.eval(exp15, set('B', 'C')));
        assertFalse(BoolEvaluator.eval(exp15, set('A', 'B', 'C')));
        assertTrue(BoolEvaluator.eval(exp16, set()));
        assertTrue(BoolEvaluator.eval(exp16, set('A')));
    }

    @Test
    public void test05_boolGen2() {
        assertTrue(BoolEvaluator.eval(exp17, set()));
        assertTrue(BoolEvaluator.eval(exp17, set('A')));
        assertTrue(BoolEvaluator.eval(exp17, set('B')));
        assertTrue(BoolEvaluator.eval(exp17, set('A', 'B')));
        assertTrue(BoolEvaluator.eval(exp17, set('C')));
        assertTrue(BoolEvaluator.eval(exp17, set('A', 'C')));
        assertTrue(BoolEvaluator.eval(exp17, set('B', 'C')));
        assertTrue(BoolEvaluator.eval(exp17, set('A', 'B', 'C')));
        assertFalse(BoolEvaluator.eval(exp18, set()));
        assertTrue(BoolEvaluator.eval(exp18, set('B')));
        assertTrue(BoolEvaluator.eval(exp18, set('C')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C')));
        assertTrue(BoolEvaluator.eval(exp18, set('D')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'D')));
        assertTrue(BoolEvaluator.eval(exp18, set('C', 'D')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C', 'D')));
        assertTrue(BoolEvaluator.eval(exp18, set('E')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'E')));
        assertTrue(BoolEvaluator.eval(exp18, set('C', 'E')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C', 'E')));
        assertTrue(BoolEvaluator.eval(exp18, set('D', 'E')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'D', 'E')));
        assertTrue(BoolEvaluator.eval(exp18, set('C', 'D', 'E')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C', 'D', 'E')));
        assertTrue(BoolEvaluator.eval(exp18, set('F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('C', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('D', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'D', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('C', 'D', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C', 'D', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('E', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'E', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('C', 'E', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C', 'E', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('D', 'E', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'D', 'E', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('C', 'D', 'E', 'F')));
        assertTrue(BoolEvaluator.eval(exp18, set('B', 'C', 'D', 'E', 'F')));
        assertTrue(BoolEvaluator.eval(exp19, set()));
        assertTrue(BoolEvaluator.eval(exp19, set('A')));
        assertTrue(BoolEvaluator.eval(exp19, set('B')));
        assertTrue(BoolEvaluator.eval(exp19, set('A', 'B')));
        assertTrue(BoolEvaluator.eval(exp20, set()));
        assertTrue(BoolEvaluator.eval(exp20, set('A')));
        assertTrue(BoolEvaluator.eval(exp20, set('B')));
        assertTrue(BoolEvaluator.eval(exp20, set('A', 'B')));
        assertTrue(BoolEvaluator.eval(exp20, set('C')));
        assertTrue(BoolEvaluator.eval(exp20, set('A', 'C')));
        assertTrue(BoolEvaluator.eval(exp20, set('B', 'C')));
        assertTrue(BoolEvaluator.eval(exp20, set('A', 'B', 'C')));
        assertFalse(BoolEvaluator.eval(exp21, set()));
        assertFalse(BoolEvaluator.eval(exp21, set('A')));
        assertFalse(BoolEvaluator.eval(exp21, set('B')));
        assertFalse(BoolEvaluator.eval(exp21, set('A', 'B')));
        assertFalse(BoolEvaluator.eval(exp21, set('C')));
        assertFalse(BoolEvaluator.eval(exp21, set('A', 'C')));
        assertFalse(BoolEvaluator.eval(exp21, set('B', 'C')));
        assertFalse(BoolEvaluator.eval(exp21, set('A', 'B', 'C')));
        assertTrue(BoolEvaluator.eval(exp22, set()));
        assertFalse(BoolEvaluator.eval(exp22, set('A')));
        assertFalse(BoolEvaluator.eval(exp23, set()));
        assertFalse(BoolEvaluator.eval(exp23, set('A')));
        assertTrue(BoolEvaluator.eval(exp23, set('B')));
        assertFalse(BoolEvaluator.eval(exp23, set('A', 'B')));
        assertFalse(BoolEvaluator.eval(exp23, set('E')));
        assertFalse(BoolEvaluator.eval(exp23, set('A', 'E')));
        assertTrue(BoolEvaluator.eval(exp23, set('B', 'E')));
        assertFalse(BoolEvaluator.eval(exp23, set('A', 'B', 'E')));
        assertTrue(BoolEvaluator.eval(exp24, set()));
        assertTrue(BoolEvaluator.eval(exp24, set('A')));
        assertFalse(BoolEvaluator.eval(exp25, set()));
        assertFalse(BoolEvaluator.eval(exp25, set('A')));
    }
}
