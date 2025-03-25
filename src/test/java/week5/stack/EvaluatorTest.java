package week5.stack;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EvaluatorTest {

    private HashMap<Character, Integer> env;

    @Before
    public void setUp() {
        env = new HashMap<>();
        env.put('x', 5);
        env.put('y', -2);
        env.put('z', 10);
    }

    @Test
    public void test01_Compute1() {
        checkCompute(4, "2+2");
        checkCompute(0, "2+2-4");
        checkCompute(8, "2+2--4");
        checkCompute(-6, "-(2+4)");
    }

    @Test
    public void test02_Compute2() {
        checkCompute(6, "2+(2+2)");
        checkCompute(0, "(2+2)-(2+2)");
        checkCompute(8, "2+2-(-4)");
        checkCompute(4, "2-(-4+4)+2");
        checkCompute(7, "2+(-y)-(-4-2+3)");
        checkCompute(5, "(2+2)-(2+2-(5+(2+2)-(2+2)))");
    }

    @Test
    public void test03_Compute3() {
        checkCompute(4, "2+(2+");
        checkCompute(0, "(2+2)-(2+2");
        checkCompute(8, "2+2-(-4-(5-x");
        checkCompute(1, "2+2-(-4-(-y)+3+(((6");
    }

    @Test
    public void test04_AST1() {
        checkAst("2", "2");
        checkAst("+(2,2)", "2+2");
        checkAst("+(+(2,2),2)", "2+2+2");
        checkAst("-(+(2,2),+(2,2))", "(2+2)-(2+2)");
    }

    @Test
    public void test05_AST2() {
        checkAst("-(2)", "-2");
        checkAst("-(2)", "(-2)");
        checkAst("-(2)", "-(2)");
        checkAst("+(2,+(2,2))", "2+(2+2)");
        checkAst("-(+(2,2),-(4))", "2+2-(-4)");
        checkAst("-(+(2,2),+(-(-(x),2),y))", "2+2-(-x-2+y)");
    }

    private void checkAst(String expected, String input) {
        assertEquals(expected, Evaluator.createAst(Evaluator.tokenize(input)).toString());
    }

    private void checkCompute(int result, String input) {
        assertEquals(result, Evaluator.compute(Evaluator.tokenize(input), env));
    }
}
