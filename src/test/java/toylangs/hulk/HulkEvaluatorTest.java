package toylangs.hulk;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.hulk.ast.HulkNode;
import toylangs.hulk.ast.HulkProg;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.hulk.ast.HulkNode.*;
import static toylangs.hulk.HulkEvaluator.eval;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class HulkEvaluatorTest {
    private final Map<Character, Set<Character>> env = new HashMap<>();
    private HulkProg prog;
    private Map<Character, Set<Character>> runningEnv;

    @Before
    public void setUp() {
        env.put('A', set('a'));
        env.put('B', set('b'));
        env.put('X', set('a', 'b', 'c'));
    }

    @Test
    public void test01_eval_literaalMuutuja() {
        prog = prog(stmt('A', lit(), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set());
        checkEval(prog, runningEnv);

        prog = prog(stmt('C', lit('x'), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('C',set('x'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('D', var('A'), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('D',set('a'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('D', lit('x', 'y', 'z'), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('D',set('x','y','z'));
        checkEval(prog, runningEnv);
    }

    @Test
    public void test02_eval_avaldis() {
        prog = prog(stmt('A', binop('+', lit('x'), lit('y')), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set('x','y'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('G', binop('-', var('X'), lit('b')), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('G',set('a','c'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('H', binop('&', var('X'), var('A')), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('H',set('a'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('H', binop('&', binop('&', var('X'), var('A')), var('B')), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('H',set());
        checkEval(prog, runningEnv);

        prog = prog(stmt('H', binop('-', binop('+', var('X'), var('A')), var('B')), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('H',set('a','c'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('V', binop('-', binop('-', var('X'), lit('a')), lit('b')), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('V',set('c'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('V', binop('-', var('X'), binop('-', lit('a'), lit('b'))), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('V',set('b','c'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('B', binop('+', binop('+', binop('+', binop('+', lit('x'), lit('u', 'y')), var('A')), var('V')), lit('k', 'm')), null));
        checkEval(prog, null);
    }

    @Test
    public void test03_eval_tingimus() {
        prog = prog(stmt('A', var('B'), cond(var('A'), var('X'))));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('A', var('B'), cond(var('X'), var('A'))));
        runningEnv = new HashMap<>(env);
        checkEval(prog, runningEnv);

        prog = prog(stmt('A', lit('u', 'v'), cond(var('A'), var('A'))));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set('u','v'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('A', binop('&', binop('+', var('A'), var('B')), var('C')), cond(var('A'), var('A'))));
        checkEval(prog, null);

        prog = prog(stmt('A', binop('-', var('X'), lit('a', 'y')), cond(lit('x'), lit('x', 'y'))));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set('b','c'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('C', binop('+', var('A'), lit('x', 'y')), cond(binop('+', binop('&', lit('x'), var('A')), var('X')), binop('+', binop('-', lit('a', 'b', 'c', 'x', 'y'), binop('+', lit('y'), var('A'))), var('A')))));
        runningEnv = new HashMap<>(env);
        runningEnv.put('C',set('a','x','y'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('C', binop('+', var('A'), lit('x', 'y')), cond(binop('+', binop('&', lit('x'), var('A')), var('X')), binop('-', lit('a', 'b', 'c', 'x', 'y'), binop('+', lit('y'), var('A'))))));
        runningEnv = new HashMap<>(env);
        checkEval(prog, runningEnv);

        prog = prog(stmt('A', binop('&', binop('+', var('A'), var('B')), var('C')), cond(binop('+', var('Y'), var('X')), var('A'))));
        checkEval(prog, null);
    }

    @Test
    public void test04_eval_laused() {
        prog = prog(
                stmt('C', var('B'), null),
                stmt('D', var('C'), null));
        runningEnv = new HashMap<>(env);
        runningEnv.put('C',set('b'));
        runningEnv.put('D',set('b'));
        checkEval(prog, runningEnv);

        prog = prog(
                stmt('A',var('A'),null),
                stmt('B',var('X'),null),
                stmt('C',lit('a'),null),
                stmt('D',lit('x','y','z'),null),
                stmt('A',var('B'), cond(var('A'),var('A'))),
                stmt('A', binop('-', var('A'),lit('a','y')), cond(binop('&', lit('x'),var('D')),lit('x','y'))));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set('b','c'));
        runningEnv.put('B',set('a','b','c'));
        runningEnv.put('C',set('a'));
        runningEnv.put('D',set('x','y','z'));
        checkEval(prog, runningEnv);

        prog = prog(
                stmt('C', var('B'), null),
                stmt('D', binop('+', var('C'), var('F')), null));
        checkEval(prog, null);
    }

    @Test
    public void test05_eval_varia() {

        prog = prog(stmt('A', var('C'), null));
        checkEval(prog, null);

        prog = prog(stmt('A', lit('x', 'y'), cond(lit(), lit())));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set('x','y'));
        checkEval(prog, runningEnv);

        prog = prog(stmt('A', binop('+', var('A'), lit('x', 'y')), cond(binop('&', var('A'), var('B')), binop('-', binop('&', var('X'), lit('a')), var('A')))));
        runningEnv = new HashMap<>(env);
        runningEnv.put('A',set('a','x','y'));
        checkEval(prog, runningEnv);
    }


    private void checkEval(HulkNode node, Map<Character, Set<Character>> expected) {

        Map<Character, Set<Character>> currentEnv = new HashMap<>(env);

        if (expected != null) {
            eval(node, currentEnv);
            assertEquals(expected, currentEnv);
        } else {
            try {

                eval(node, currentEnv);
                fail("Programm pidi viskama erindi!");
            } catch (RuntimeException ignore) {
            }
        }

    }

    public static Set<Character> set(Character... elemendid) {
        return new HashSet<>(Arrays.asList(elemendid));
    }

}
