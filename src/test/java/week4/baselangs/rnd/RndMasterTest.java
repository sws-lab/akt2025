package week4.baselangs.rnd;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week4.baselangs.rnd.ast.RndNode;

import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

import static org.junit.Assert.assertEquals;
import static week4.baselangs.rnd.ast.RndNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RndMasterTest {

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
    public static final RndNode ex11 = flip(ex2, ex2);

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
    public void test01_allval() {
        assertEquals(Set.of(10,11), RndMaster.evalNondeterministic(ex4));
        assertEquals(Set.of(12,36), RndMaster.evalNondeterministic(ex7));
        assertEquals(Set.of(10,11, 12,36), RndMaster.evalNondeterministic(flip(ex4, ex7)));
        assertEquals(Set.of(22,46, 23,47), RndMaster.evalNondeterministic(add(ex4, ex7)));
    }

    // Tagasta tõenäosus, et avaldise tulemus võrdub etteantud väärtusega.
    private static double prob(RndNode expr, int value) {
        Map<Integer, Double> dist = RndMaster.evalDistribution(expr);
        return dist.getOrDefault(value, 0.0);
    }

    @Test
    public void test02_prob() {
        assertEquals(1.0, prob(ex1, 10), 0.0);
        assertEquals(0.0, prob(ex1, 40), 0.0);

        assertEquals(0.5, prob(ex2, 4), 0.0);
        assertEquals(0.5, prob(ex2, 6), 0.0);
        assertEquals(0.0, prob(ex2, 10), 0.0);

        assertEquals(0.25, prob(ex3, 4), 0.0);
        assertEquals(0.25, prob(ex3, 5), 0.0);
        assertEquals(0.50, prob(ex3, 6), 0.0);

        // See on see oluline asi, et (0|1)+(0|1) puhul on summa 1 tõenäolisem.
        assertEquals(0.25, prob(ex9, 8), 0.0);
        assertEquals(0.25, prob(ex9, 12), 0.0);
        assertEquals(0.50, prob(ex9, 10), 0.0);

        assertEquals(0.25, prob(ex8, 8), 0.0);
        assertEquals(0.125, prob(ex8, 7), 0.0);

        assertEquals(0.5, prob(ex10, -4), 0.0);
        assertEquals(0.5, prob(ex10, -6), 0.0);
        assertEquals(0.0, prob(ex10, -10), 0.0);

        assertEquals(0.5, prob(ex11, 4), 0.0);
        assertEquals(0.5, prob(ex11, 6), 0.0);
        assertEquals(0.0, prob(ex11, 10), 0.0);
    }
}
