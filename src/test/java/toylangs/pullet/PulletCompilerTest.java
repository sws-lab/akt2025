package toylangs.pullet;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.pullet.ast.PulletNode;

import static org.junit.Assert.assertEquals;
import static toylangs.pullet.ast.PulletNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PulletCompilerTest {

    private final CMaStack st0 = new CMaStack(0, 0, 0);
    private final CMaStack st1 = new CMaStack(1, 2, 3);
    private final CMaStack st2 = new CMaStack(44, 100, -76);

    @Test
    public void test01_literaalVahe() {
        checkCompile(st0, num(0), 0);
        checkCompile(st0, num(97519), 97519);
        checkCompile(st0, diff(num(10),num(5)), 5);
        checkCompile(st0, diff(diff(num(10),num(5)),num(9)), -4);
        checkCompile(st0, diff(diff(num(10),num(5)), diff(num(9),num(22))), 18);
    }

    @Test
    public void test02_globals() {
        checkCompile(st0, var("x"), 0);
        checkCompile(st1, var("x"), 1);
        checkCompile(st1, var("y"), 2);
        checkCompile(st2, diff(num(10),var("z")), 86);
        checkCompile(st1, diff(diff(num(10),var("y")),num(9)), -1);
    }

    @Test
    public void test03_let() {
        checkCompile(st0, let("a",num(5),var("a")), 5);
        checkCompile(st0, let("kala",num(5), diff(var("kala"),num(10))), -5);
        checkCompile(st1, let("b", num(100),var("x")), 1);
        checkCompile(st1, let("b", num(100), diff(var("b"), var("x"))), 99);

        checkCompile(st0, let("a",num(44), let("b",num(2), let("c",num(4),
                        diff(var("a"),var("c"))))), 40);
        checkCompile(st0, let("a",num(44), let("b", num(2), let("c", diff(num(10), var("b")),
                diff(var("a"),var("c"))))), 36);
        checkCompile(st1, let("a",num(44), let("b", diff(var("z"), num(2)), let("c", diff(num(10), var("b")),
                diff(var("a"),var("c"))))), 35);
    }

    @Test
    public void test04_all() {
        checkCompile(st0, sum("i",num(1),num(4),var("i")), 10);
        checkCompile(st0, sum("i",num(1),num(4),num(1)), 4);
        checkCompile(st0, sum("i",num(1),num(40), diff(var("i"),num(10))), 420);
        checkCompile(st1, sum("i",num(1),num(4),var("z")), 12);
        checkCompile(st1, let("a", sum("i",num(1), num(4), var("i")),
                diff(var("y"), var("a"))), -8);
        checkCompile(st0, let("a", sum("i",num(1), num(4), var("i")),
                let("b", sum("i",num(1),num(4),num(2)),
                diff(var("b"), var("a")))), -2);

    }


    private void checkCompile(CMaStack stack, PulletNode expr, int expected) {
        CMaProgram program = PulletCompiler.compile(expr);
        int actual = CMaInterpreter.run(program, stack).peek();
        assertEquals(expected, actual);
    }
}
