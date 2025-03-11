package toylangs.pullet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.pullet.ast.PulletNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.pullet.ast.PulletNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PulletEvaluatorTest {

    @Test
    public void test01_literaalVahe() {
        checkEval(num(0), 0);
        checkEval(num(97519), 97519);
        checkEval(diff(num(10),num(5)), 5);
        checkEval(diff(diff(num(10),num(5)),num(9)), -4);
        checkEval(diff(diff(num(10),num(5)), diff(num(9),num(22))), 18);
    }

    @Test
    public void test02_let() {
        checkEval(let("x",num(5),var("x")), 5);
        checkEval(let("x",num(5), diff(var("x"),num(10))), -5);
        checkEval(let("x", diff(num(10),num(3)), diff(var("x"),num(100))), -93);
        checkEval(let("x",num(100),num(5)), 5);
        checkEval(diff(num(55),let("x",num(100),num(5))), 50);
        checkEval(var("a"), null);
        checkEval(let("x",num(100), diff(var("z"),var("x"))), null);
    }

    @Test
    public void test3_sum() {
        checkEval(sum("i",num(1),num(4),var("i")), 10);
        checkEval(sum("i",num(1),num(4),num(1)), 4);
        checkEval(sum("i",num(1),num(40), diff(var("i"),num(10))), 420);
        checkEval(sum("x", diff(num(1),num(1)), diff(diff(num(10),num(1)),num(1)),var("i")), null);
        checkEval(sum("x",num(1),num(4),var("y")), null);
    }

    @Test
    public void test04_multiLevel() {
        checkEval(let("x",num(44),let("y",num(2),let("z",num(0), diff(var("x"),var("y"))))), 42);
        checkEval(let("x",num(1),let("y",num(5),sum("i",var("x"),var("y"),var("i")))), 15);
        checkEval(let("a",let("b",let("c",num(11),var("c")), diff(var("b"),num(10))),let("b",num(10), diff(var("a"),var("b")))), -9);
        checkEval(let("x",sum("i",num(1),num(4),var("i")),var("x")), 10);
        checkEval(sum("i",sum("j",num(1),num(100),num(0)),num(5),var("i")), 15);
        checkEval(diff(diff(num(10),var("x")),let("y",num(10), diff(num(100),num(9)))), null);
        checkEval(let("x", diff(var("x"),num(11)),var("x")), null);
    }

    @Test
    public void test05_varia() {
        checkEval(let("x",num(5),let("x", diff(var("x"),num(1)),var("x"))), 4);

        checkEval(diff(let("x",num(10),var("x")),var("x")), null);
        checkEval(let("x",num(5), diff(let("x",num(10),var("x")),var("x"))), 5);
        checkEval(let("x",num(5), diff(var("x"), let("x",num(10),var("x")))), -5);

        checkEval(let("x",num(666), diff(sum("i",num(0),num(3),sum("j",num(0),var("i"), diff(var("i"),var("j")))),num(1))), 9);
        checkEval(let("x",num(10),let("x",num(1),let("y",num(8),sum("y",var("x"),num(20), diff(var("y"),var("x")))))), 190);
    }


    private void checkEval(PulletNode inputAst, Integer expected) {
        if (expected != null) {
            Integer actual = PulletEvaluator.eval(inputAst);
            assertEquals(expected, actual);
        } else {
            try {
                PulletEvaluator.eval(inputAst);
                fail("Meetod pidi viskama erindi!");
            } catch (RuntimeException ignore) {
            }
        }
    }
}
