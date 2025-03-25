package week6;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static week6.AktkNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkNodeTest {


    private final HashMap<String, Integer> env = new HashMap<>();

    @Before
    public void setUp() {
        env.put("kala", 2);
        env.put("koer", -5);
        env.put("x", 10);
    }

    @Test
    public void test01_basics() {
        check(-4, num(-4));
        check(15, num(15));
    }

    @Test
    public void test02_var() {
        check(2, var("kala"));
        check(-5, var("koer"));
    }


    @Test
    public void test03_ops() {
        check(3, minus(num(5), num(2)));
        check(-1, plus(num(-3), num(2)));
        check(56, mul(num(8), num(7)));
        check(5, div(num(10), num(2)));
    }


    @Test
    public void test04_all() {
        check(8, minus(num(5), minus(var("kala"), num(5))));
        check(12, minus(num(5), minus(var("koer"), var("kala"))));
    }


    private void check(int expected, AktkNode node) {
        assertEquals(node.toString(), expected, node.eval(env));
    }


}
