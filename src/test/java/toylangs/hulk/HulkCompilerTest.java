package toylangs.hulk;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import cma.CMaUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.hulk.ast.HulkNode;
import toylangs.hulk.ast.HulkProg;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static toylangs.hulk.ast.HulkNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HulkCompilerTest {

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
    public void test01_literaalMuutuja() {
        prog = prog(stmt('A', lit(), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set());
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('C', lit('x'), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('C', set('x'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('D', var('A'), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('D', set('a'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('D', lit('x', 'y', 'z'), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('D', set('x', 'y', 'z'));
        checkCompile(prog, runningEnv);
    }

    @Test
    public void test02_avaldis() {
        prog = prog(
                stmt('A', binop('+', lit('x'), lit('y')), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('x', 'y'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('G', binop('-', var('X'), lit('b')), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('G', set('a', 'c'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('H', binop('&', var('X'), var('A')), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('H', set('a'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('H', binop('&', binop('&', var('X'), var('A')), var('B')), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('H', set());
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('H', binop('-', binop('+', var('X'), var('A')), var('B')), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('H', set('a', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('V', binop('-', binop('-', var('X'), lit('a')), lit('b')), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('V', set('c'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('V', binop('-', var('X'), binop('-', lit('a'), lit('b'))), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('V', set('b', 'c'));
        checkCompile(prog, runningEnv);
    }

    @Test
    public void test03_tingimus() {
        prog = prog(stmt('A', var('B'), cond(var('A'), var('X'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('b'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(stmt('A', var('B'), cond(var('X'), var('A'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(stmt('A', lit('u', 'v'), cond(var('A'), var('A'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('u', 'v'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(stmt('A', binop('-', var('X'), lit('a', 'y')), cond(lit('x'), lit('x', 'y'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('b', 'c'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(stmt('C', binop('+', var('A'), lit('x', 'y')), cond(binop('+', binop('&', lit('x'), var('A')), var('X')), binop('+', binop('-', lit('a', 'b', 'c', 'x', 'y'), binop('+', lit('y'), var('A'))), var('A')))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('C', set('a', 'x', 'y'));
        checkCompile(prog, runningEnv);


        prog = prog(stmt('C', binop('+', var('A'), lit('x', 'y')), cond(binop('+', binop('&', lit('x'), var('A')), var('X')), binop('-', lit('a', 'b', 'c', 'x', 'y'), binop('+', lit('y'), var('A'))))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);

    }

    @Test
    public void test04_laused() {
        prog = prog(
                stmt('C', var('B'), null),
                stmt('D', var('C'), null));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a'));
        runningEnv.put('B', set('b'));
        runningEnv.put('C', set('b'));
        runningEnv.put('D', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(
                stmt('A', var('A'), null),
                stmt('B', var('X'), null),
                stmt('C', lit('a'), null),
                stmt('D', lit('x', 'y', 'z'), null),
                stmt('A', var('B'), cond(var('A'), var('A'))),
                stmt('A', binop('-', var('A'), lit('a', 'y')), cond(binop('&', lit('x'), var('D')), lit('x', 'y'))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('b', 'c'));
        runningEnv.put('B', set('a', 'b', 'c'));
        runningEnv.put('C', set('a'));
        runningEnv.put('D', set('x', 'y', 'z'));
        checkCompile(prog, runningEnv);
    }

    @Test
    public void test05_varia() {
        prog = prog(stmt('A', lit('x', 'y'), cond(lit(), lit())));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('x', 'y'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);


        prog = prog(stmt('A', binop('+', var('A'), lit('x', 'y')), cond(binop('&', var('A'), var('B')), binop('-', binop('&', var('X'), lit('a')), var('A')))));
        runningEnv = new HashMap<>();
        runningEnv.put('X', set('a', 'b', 'c'));
        runningEnv.put('A', set('a', 'x', 'y'));
        runningEnv.put('B', set('b'));
        checkCompile(prog, runningEnv);
    }

    private static final List<Character> SET_VARIABLES = Arrays.asList('X', 'A', 'B', 'C', 'D', 'G', 'H', 'V');

    private static int set2int(Set<Character> set) {
        List<Character> ELEM_VARIABLES = Arrays.asList('x', 'y', 'z', 'a', 'b', 'c', 'u', 'v');
        int result = 0;
        if (set != null) {
            for (int i = 0; i < ELEM_VARIABLES.size(); i++) {
                result |= CMaUtils.bool2int(set.contains(ELEM_VARIABLES.get(i))) << i;
            }
        }
        return result;
    }

    private void checkCompile(HulkNode node, Map<Character, Set<Character>> expected) {

        Map<Character, Set<Character>> currentEnv = new HashMap<>(env);

        CMaProgram program = HulkCompiler.compile(node);

        CMaStack initialStack = new CMaStack();
        for (Character variable : SET_VARIABLES) {
            initialStack.push(set2int(currentEnv.get(variable)));
        }

        CMaStack expectedStack = new CMaStack();
        for (Character variable : SET_VARIABLES) {
            expectedStack.push(set2int(expected.get(variable)));
        }

        CMaStack actualStack = CMaInterpreter.run(program, initialStack);

        assertEquals(expectedStack, actualStack);
    }

    private static Set<Character> set(Character... elemendid) {
        return new HashSet<>(Arrays.asList(elemendid));
    }
}
