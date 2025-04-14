package toylangs.bolog;

import cma.CMaInterpreter;
import cma.CMaStack;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.bolog.ast.BologNode;

import static org.junit.Assert.assertEquals;
import static toylangs.bolog.ast.BologNode.*;
import static toylangs.bolog.BologCompiler.compile;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BologCompilerTest {

    @Test
    public void test01_basic() {
        checkCompile(1, tv(true));
        checkCompile(0, tv(false));
        checkCompile(0, var("P"), 0);
        checkCompile(1, var("P"), 1);
    }

    @Test
    public void test02_ops() {
        checkCompile(1, nand(var("P"), var("Q")), 0, 0);
        checkCompile(1, nand(var("P"), var("Q")), 1, 0);
        checkCompile(1, nand(var("P"), var("Q")), 0, 1);
        checkCompile(0, nand(var("P"), var("Q")), 1, 1);
    }

    @Test
    public void test03_ops() {
        checkCompile(1, nand(var("P"), var("Q")), 0, 0);
        checkCompile(1, nand(var("P"), var("Q")), 1, 0);
        checkCompile(1, nand(var("P"), var("Q")), 0, 1);
        checkCompile(0, nand(var("P"), var("Q")), 1, 1);
    }

    @Test
    public void test04_imp() {
        checkCompile(1, imp(var("P"), var("Q")), 0, 0);
        checkCompile(1, imp(var("P"), var("Q")), 1, 0);
        checkCompile(0, imp(var("P"), var("Q")), 0, 1);
        checkCompile(1, imp(var("P"), var("Q")), 1, 1);

        checkCompile(1, imp(var("P"), var("Q"), var("R")), 1, 0, 0);
        checkCompile(1, imp(var("P"), var("Q"), var("R")), 1, 0, 1);
        checkCompile(1, imp(var("P"), var("Q"), var("R")), 1, 1, 0);
        checkCompile(1, imp(var("P"), var("Q"), var("R")), 1, 1, 1);
        checkCompile(0, imp(var("P"), var("Q"), var("R")), 0, 1, 1);
        checkCompile(1, imp(var("P"), var("Q"), var("R")), 0, 1, 0);
        checkCompile(1, imp(var("P"), var("Q"), var("R")), 0, 0, 1);
        checkCompile(1, imp(var("P"), var("Q"), var("R")), 0, 0, 0);
    }


    private void checkCompile(int expected, BologNode ast, int... stack) {
        CMaStack initialStack = new CMaStack(stack);
        CMaStack finalStack = CMaInterpreter.run(compile(ast), initialStack);
        assertEquals(expected, finalStack.peek());
    }
}
