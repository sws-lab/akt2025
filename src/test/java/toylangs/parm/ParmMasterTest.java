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
public class ParmMasterTest {

    @Test
    public void test01_const() {
        checkCompile(10, lit(10));
        checkCompile(23, var("X"), 23, 400);
        checkCompile(400, var("Y"), 23, 400);
        checkCompile(22, plus(lit(10), lit(12)));
        checkCompile(13, seq(lit(10), lit(13)));
        checkCompile(15, plus(seq(lit(10), lit(13)), lit(2)));
    }

    @Test
    public void test02_seqs() {
        checkCompile(3, seq(up("X", lit(3)), var("X")));

        checkCompile(100, seq(up("X", lit(3)), var("Y")), 55, 100);
        checkCompile(55, seq(up("Y", var("X")), var("Y")), 55, 100);

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

    @Test
    public void test03_par() {
        checkCompile(4, seq(up("Y", lit(4)), par(up("X", lit(3)), var("Y"))));

        checkCompile(10, seq(par(up("X", lit(10)), up("Y", lit(23))), var("X")));
        checkCompile(23, seq(par(up("X", lit(10)), up("Y", lit(23))), var("Y")));
        checkCompile(33, seq(par(up("X", lit(10)), up("Y", lit(23))), plus(var("X"), var("Y"))));

        ParmNode par1 = par(up("X", plus(var("X"), lit(1))), up("Y", var("X")));
        checkCompile(8, seq(up("X", lit(7)), seq(par1, var("X"))));
        checkCompile(7, seq(up("X", lit(7)), seq(par1, var("Y"))));
        checkCompile(15, seq(up("X", lit(7)), seq(par1, plus(var("X"), var("Y")))));
        ParmNode seq1 = seq(up("X", plus(var("X"), lit(1))), up("Y", var("X")));
        checkCompile(8, seq(up("X", lit(7)), seq(seq1, var("X"))));
        checkCompile(8, seq(up("X", lit(7)), seq(seq1, var("Y"))));
        checkCompile(16, seq(up("X", lit(7)), seq(seq1, plus(var("X"), var("Y")))));

        ParmNode init = seq(up("X", lit(3)), seq(up("Y", lit(4)), up("Z", lit(5))));
        ParmNode par2 = par(up("X", var("Y")), up("Y", var("X")));
        checkCompile(4, seq(init, seq(par2, var("X"))));
        checkCompile(3, seq(init, seq(par2, var("Y"))));
        ParmNode seq2 = seq(up("X", var("Y")), up("Y", var("X")));
        checkCompile(4, seq(init, seq(seq2, var("X"))));
        checkCompile(4, seq(init, seq(seq2, var("Y"))));

        checkCompile(7, seq(par2, var("X")), 6, 7);
        checkCompile(6, seq(par2, var("Y")), 6, 7);
    }

    @Test
    public void test04_hard() {
        ParmNode init = seq(up("X", lit(3)), seq(up("Y", lit(4)), up("Z", lit(5))));

        ParmNode par3Left = par(par(up("X", var("Z")), up("Y", var("X"))), up("Z", var("Y")));
        checkCompile(5, seq(init, seq(par3Left, var("X"))));
        checkCompile(3, seq(init, seq(par3Left, var("Y"))));
        checkCompile(4, seq(init, seq(par3Left, var("Z"))));

        ParmNode par3Right = par(up("X", var("Z")), par(up("Y", var("X")), up("Z", var("Y"))));
        checkCompile(5, seq(init, seq(par3Right, var("X"))));
        checkCompile(3, seq(init, seq(par3Right, var("Y"))));
        checkCompile(4, seq(init, seq(par3Right, var("Z"))));
    }

    private void checkCompile(int expected, ParmNode ast, int... stack) {
        CMaStack initialStack = new CMaStack(stack);
        for (int i = 0; i < VARS.size() - stack.length; i++) initialStack.push(0);

        CMaStack finalStack = CMaInterpreter.run(ParmMaster.compile(ast), initialStack);
        assertEquals(expected, finalStack.peek());
    }
}
