package toylangs.parm;

import cma.CMaInterpreter;
import cma.CMaStack;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.parm.ast.ParmNode;

import static org.junit.Assert.assertEquals;
import static toylangs.parm.ast.ParmNode.*;
import static toylangs.parm.ParmCompiler.VARS;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParmCompilerTest {

    @Test
    public void test01_basic() {
        checkCompile(10, lit(10));
        checkCompile(23, var("X"), 23, 400);
        checkCompile(400, var("Y"), 23, 400);
    }

    @Test
    public void test02_ops() {
        checkCompile(22, plus(lit(10), lit(12)));
        checkCompile(13, seq(lit(10), lit(13)));
        checkCompile(15, plus(seq(lit(10), lit(13)), lit(2)));
    }

    @Test
    public void test03_up() {
        checkCompile(3, seq(up("X", lit(3)), var("X")));

        checkCompile(100, seq(up("X", lit(3)), var("Y")), 55, 100);
        checkCompile(55, seq(up("Y", var("X")), var("Y")), 55, 100);
    }

    @Test
    public void test04_all() {
        checkCompile(3, seq(up("Y", lit(4)), seq(up("X", lit(3)), var("X"))));
        checkCompile(4, seq(up("Y", lit(4)), seq(up("X", lit(3)), var("Y"))));
        checkCompile(7, seq(up("Y", lit(4)), seq(up("X", lit(3)), plus(var("X"), var("Y")))));

        checkCompile(10, seq(seq(up("X", lit(10)), up("Y", lit(23))), var("X")));
        checkCompile(23, seq(seq(up("X", lit(10)), up("Y", lit(23))), var("Y")));
        checkCompile(33, seq(seq(up("X", lit(10)), up("Y", lit(23))), plus(var("X"), var("Y"))));

        checkCompile(8, seq(up("X", lit(7)), seq(up("Y", plus(var("X"), lit(1))), var("Y"))));
        checkCompile(8, seq(up("X", lit(7)), seq(up("X", plus(var("X"), lit(1))), var("X"))));
        checkCompile(16, plus(up("X", lit(8)), var("X")));
    }


    private void checkCompile(int expected, ParmNode ast, int... stack) {
        CMaStack initialStack = new CMaStack(stack);
        for (int i = 0; i < VARS.size() - stack.length; i++) initialStack.push(0);

        CMaStack finalStack = CMaInterpreter.run(ParmCompiler.compile(ast), initialStack);
        assertEquals(expected, finalStack.peek());
    }
}
