package toylangs.parm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.parm.ast.ParmNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static toylangs.parm.ast.ParmNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParmEvaluatorTest {


    @Test
    public void test01_const() {
        checkEval(10, lit(10));
        checkEval(22, plus(lit(10), lit(12)));
        checkEval(36, up("Kala", lit(36)));
        checkEval(49, plus(up("Koer", lit(13)), up("Kala", lit(36))));
        checkEval(13, seq(lit(10), lit(13)));
        checkEval(15, plus(seq(lit(10), lit(13)), lit(2)));
    }

    @Test
    public void test02_seqs() {
        checkEval(3, seq(up("X", lit(3)), var("X")));

        checkEval(3, seq(up("Y", lit(4)), seq(up("X", lit(3)), var("X"))));
        checkEval(4, seq(up("Y", lit(4)), seq(up("X", lit(3)), var("Y"))));
        checkEval(7, seq(up("Y", lit(4)), seq(up("X", lit(3)), plus(var("X"), var("Y")))));

        checkEval(10, seq(seq(up("X", lit(10)), up("Y", lit(23))), var("X")));
        checkEval(23, seq(seq(up("X", lit(10)), up("Y", lit(23))), var("Y")));
        checkEval(33, seq(seq(up("X", lit(10)), up("Y", lit(23))), plus(var("X"), var("Y"))));

        checkEval(8, seq(up("X", lit(7)), seq(up("Y", plus(var("X"), lit(1))), var("Y"))));
        checkEval(8, seq(up("X", lit(7)), seq(up("X", plus(var("X"), lit(1))), var("X"))));
        checkEval(16, plus(up("X", lit(8)), var("X")));
    }

    @Test
    public void test03_par() {
        checkEval(4, seq(up("Y", lit(4)), par(up("X", lit(3)), var("Y"))));

        checkEval(10, seq(par(up("X", lit(10)), up("Y", lit(23))), var("X")));
        checkEval(23, seq(par(up("X", lit(10)), up("Y", lit(23))), var("Y")));
        checkEval(33, seq(par(up("X", lit(10)), up("Y", lit(23))), plus(var("X"), var("Y"))));

        ParmNode par1 = par(up("X", plus(var("X"), lit(1))), up("Y", var("X")));
        checkEval(8, seq(up("X", lit(7)), seq(par1, var("X"))));
        checkEval(7, seq(up("X", lit(7)), seq(par1, var("Y"))));
        checkEval(15, seq(up("X", lit(7)), seq(par1, plus(var("X"), var("Y")))));
        ParmNode seq1 = seq(up("X", plus(var("X"), lit(1))), up("Y", var("X")));
        checkEval(8, seq(up("X", lit(7)), seq(seq1, var("X"))));
        checkEval(8, seq(up("X", lit(7)), seq(seq1, var("Y"))));
        checkEval(16, seq(up("X", lit(7)), seq(seq1, plus(var("X"), var("Y")))));

        ParmNode init = seq(up("X", lit(3)), seq(up("Y", lit(4)), up("Z", lit(5))));
        ParmNode par2 = par(up("X", var("Y")), up("Y", var("X")));
        checkEval(4, seq(init, seq(par2, var("X"))));
        checkEval(3, seq(init, seq(par2, var("Y"))));
        ParmNode seq2 = seq(up("X", var("Y")), up("Y", var("X")));
        checkEval(4, seq(init, seq(seq2, var("X"))));
        checkEval(4, seq(init, seq(seq2, var("Y"))));

        ParmNode par3Left = par(par(up("X", var("Z")), up("Y", var("X"))), up("Z", var("Y")));
        checkEval(5, seq(init, seq(par3Left, var("X"))));
        checkEval(3, seq(init, seq(par3Left, var("Y"))));
        checkEval(4, seq(init, seq(par3Left, var("Z"))));

        ParmNode par3Right = par(up("X", var("Z")), par(up("Y", var("X")), up("Z", var("Y"))));
        checkEval(5, seq(init, seq(par3Right, var("X"))));
        checkEval(3, seq(init, seq(par3Right, var("Y"))));
        checkEval(4, seq(init, seq(par3Right, var("Z"))));
    }

    private static void checkEval(int expected, ParmNode prog) {
        int actual = ParmEvaluator.eval(prog);
        assertEquals(expected, actual);
    }

}
