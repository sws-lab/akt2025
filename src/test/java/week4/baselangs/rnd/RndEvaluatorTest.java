package week4.baselangs.rnd;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week4.baselangs.rnd.ast.RndNode;

import java.util.function.BooleanSupplier;

import static org.junit.Assert.assertEquals;
import static week4.baselangs.rnd.RndEvaluator.eval;
import static week4.baselangs.rnd.ast.RndNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RndEvaluatorTest {

    public static final RndNode ex1 = add(num(4),num(6));
    public static final RndNode ex2 = flip(num(4),num(6));
    public static final RndNode ex3 = flip(flip(num(4), num(5)), num(6));
    public static final RndNode ex4 = add(flip(num(4), num(5)), num(6));
    public static final RndNode ex5 = flip(num(4), flip(num(7), num(66)));
    public static final RndNode ex6 = flip(add(num(4), num(5)), num(8));
    public static final RndNode ex7 = add(num(3), flip(num(33), num(9)));
    public static final RndNode ex8 = flip(ex5, ex6);
    public static final RndNode ex9 = add(ex2, ex2);
    public static final RndNode ex10 = neg(ex2);
    public static final RndNode ex11 = add(flip(num(10), num(20)), flip(num(0), num(1)));

    public static final BooleanSupplier falseCoin = () -> false;
    public static final BooleanSupplier trueCoin = () -> true;

    public static BooleanSupplier altCoin(boolean start) {
        return new BooleanSupplier() {
            boolean state = !start;

            @Override
            public boolean getAsBoolean() {
                state = !state;
                return state;
            }
        };
    }

    @Test
    public void test01_basics() {
        assertEquals(5, eval(num(5), trueCoin));
        assertEquals(-5, eval(neg(num(5)), trueCoin));
        assertEquals(10, eval(ex1, trueCoin));
        assertEquals(10, eval(ex1, falseCoin));
        assertEquals(4, eval(ex2, trueCoin));
        assertEquals(6, eval(ex2, falseCoin));
    }

    @Test
    public void test02_singlechoice() {
        assertEquals(10, eval(ex4, trueCoin));
        assertEquals(11, eval(ex4, falseCoin));

        assertEquals(-4, eval(ex10, trueCoin));
        assertEquals(-6, eval(ex10, falseCoin));

        assertEquals(36, eval(ex7, trueCoin));
        assertEquals(12, eval(ex7, falseCoin));
    }


    @Test
    public void test03_combined() {
        assertEquals(4, eval(ex3, trueCoin));
        assertEquals(6, eval(ex3, falseCoin));
        assertEquals(5, eval(ex3, altCoin(true)));
        assertEquals(6, eval(ex3, altCoin(false)));

        assertEquals(4, eval(ex5, trueCoin));
        assertEquals(66, eval(ex5, falseCoin));
        assertEquals(4, eval(ex5, altCoin(true)));
        assertEquals(7, eval(ex5, altCoin(false)));

        assertEquals(4, eval(ex8, trueCoin));
        assertEquals(8, eval(ex8, falseCoin));
        assertEquals(7, eval(ex8, altCoin(true)));
        assertEquals(9, eval(ex8, altCoin(false)));

        assertEquals(8, eval(ex9, trueCoin));
        assertEquals(12, eval(ex9, falseCoin));
        assertEquals(10, eval(ex9, altCoin(true)));
        assertEquals(10, eval(ex9, altCoin(false)));
    }

    @Test
    public void test04_order() {
        assertEquals(11, eval(ex11, altCoin(true)));
        assertEquals(20, eval(ex11, altCoin(false)));
    }
}
