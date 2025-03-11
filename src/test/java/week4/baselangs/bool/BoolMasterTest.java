package week4.baselangs.bool;

import com.google.common.collect.Sets;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week4.baselangs.bool.ast.BoolNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static week4.baselangs.bool.ast.BoolNode.imp;
import static week4.baselangs.bool.ast.BoolNode.var;
import static week4.baselangs.bool.BoolMaster.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class BoolMasterTest {


    private final Set<BoolNode> allExp = set(BoolEvaluatorTest.exp10, BoolEvaluatorTest.exp11, BoolEvaluatorTest.exp12, BoolEvaluatorTest.exp13, BoolEvaluatorTest.exp14, BoolEvaluatorTest.exp15, BoolEvaluatorTest.exp16, BoolEvaluatorTest.exp17, BoolEvaluatorTest.exp18, BoolEvaluatorTest.exp19, BoolEvaluatorTest.exp20, BoolEvaluatorTest.exp21, BoolEvaluatorTest.exp22, BoolEvaluatorTest.exp23, BoolEvaluatorTest.exp24, BoolEvaluatorTest.exp25);


    @Test
    public void test01_analyzeVars() {
        BoolMaster.Stats stats;
        stats = analyze(var('A'));
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(imp(var('A'), var('A')));
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
    }

    @Test
    public void test02_analyzeImp() {
        BoolMaster.Stats stats;
        stats = analyze(var('A'));
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(imp(var('A'), var('A')));
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
    }

    @Test
    public void test03_analyzeMore() {
        BoolMaster.Stats stats = analyze(BoolEvaluatorTest.exp1);
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(BoolEvaluatorTest.exp4);
        assertEquals(set('C', 'D', 'F'), stats.getVariables());
        assertFalse(stats.containsImp());

        stats = analyze(BoolEvaluatorTest.exp5);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());

        stats = analyze(BoolEvaluatorTest.exp6);
        assertEquals(set('D', 'C', 'B', 'E'), stats.getVariables());
        assertTrue(stats.containsImp());
    }

    @Test
    public void test04_analyzeGen1() {
        BoolMaster.Stats stats = analyze(BoolEvaluatorTest.exp10);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp11);
        assertEquals(set('A', 'B', 'C', 'E'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp12);
        assertEquals(set('A', 'D', 'E', 'F'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp13);
        assertEquals(set('A', 'B', 'D'), stats.getVariables());
        assertFalse(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp14);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp15);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertFalse(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp16);
        assertEquals(set('A'), stats.getVariables());
        assertFalse(stats.containsImp());
    }

    @Test
    public void test05_analyzeGen2() {
        BoolMaster.Stats stats = analyze(BoolEvaluatorTest.exp17);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertFalse(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp18);
        assertEquals(set('B', 'C', 'D', 'E', 'F'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp19);
        assertEquals(set('A', 'B'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp20);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp21);
        assertEquals(set('A', 'B', 'C'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp22);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp23);
        assertEquals(set('A', 'B', 'E'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp24);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
        stats = analyze(BoolEvaluatorTest.exp25);
        assertEquals(set('A'), stats.getVariables());
        assertTrue(stats.containsImp());
    }

    // NB! Järgmised testid on ainult adekvaatsed, kui analyze ja eval on õigesti defineeritud.

    @Test
    public void test11_transformBasic() {
        check(imp(var('A'), var('B')));
    }

    @Test
    public void test12_transformMore() {
        check(BoolEvaluatorTest.exp1);
        check(BoolEvaluatorTest.exp2);
        check(BoolEvaluatorTest.exp3);
        check(BoolEvaluatorTest.exp4);
        check(BoolEvaluatorTest.exp5);
        check(BoolEvaluatorTest.exp6);
    }

    @Test
    public void test13_TransformAll() {
        for (BoolNode exp : allExp) check(exp);
    }

    @Test
    public void test14_Diagram() {
        checkDiagram(BoolEvaluatorTest.exp1);
        checkDiagram(BoolEvaluatorTest.exp2);
        checkDiagram(BoolEvaluatorTest.exp3);
        checkDiagram(BoolEvaluatorTest.exp4);
        checkDiagram(BoolEvaluatorTest.exp5);
        checkDiagram(BoolEvaluatorTest.exp6);
    }

    @Test
    public void test15_DiagramAllAll() {
        for (BoolNode exp : allExp) checkDiagram(exp);
    }



    private void check(BoolNode exp) {
        BoolMaster.Stats origStats = analyze(exp);
        BoolNode transformed = transform(exp);
        BoolMaster.Stats transStats = analyze(transformed);
        assertEquals(origStats.getVariables(), transStats.getVariables());
        assertFalse(transStats.containsImp());
        for (Set<Character> tv : Sets.powerSet(transStats.getVariables())) {
            assertEquals(BoolEvaluator.eval(exp, tv), BoolEvaluator.eval(transformed, tv));
        }
    }

    private void checkDiagram(BoolNode exp) {
        BoolMaster.Stats stats = analyze(exp);
        DecisionTree diagram = createDecisionTree(exp);
        for (Set<Character> tv : Sets.powerSet(stats.getVariables())) {
            assertEquals(BoolEvaluator.eval(exp, tv), diagram.eval(tv));
        }
    }

    @SafeVarargs
    private <T> Set<T> set(T... characters) {
        return new HashSet<>(Arrays.asList(characters));
    }

}
