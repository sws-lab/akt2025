package week4.baselangs.expr;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week4.baselangs.expr.ast.ExprNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static week4.baselangs.expr.ast.ExprNode.*;
import static week4.baselangs.expr.VisitorIntro.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VisitorIntroTest {

    public static final ExprNode expr1 = div(add(num(5), add(num(3), neg(num(2)))), num(2));
    public static final ExprNode expr2 = add(add(num(5), div(num(3), neg(num(2)))), num(2));
    public static final ExprNode expr3 = div(div(num(8), num(2)), num(2));
    public static final ExprNode expr4 = div(num(8), div(num(2), num(2)));
    public static final ExprNode expr5 = add(neg(expr4), add(expr2, expr3));

    private static Set<Integer> set(Integer... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    @Test
    public void test01_exprMethodSimple() {
        assertEquals(10, num(10).eval());
        assertEquals(4, add(num(2), num(2)).eval());
    }

    @Test
    public void test02_exprMethodMore() {
        assertEquals(3, expr1.eval());
        assertEquals(6, expr2.eval());
        assertEquals(2, expr3.eval());
        assertEquals(8, expr4.eval());
        assertEquals(0, expr5.eval());
    }


    @Test
    public void test03_exprInstanceof() {
        assertEquals(10, eval(num(10)));
        assertEquals(4, eval(add(num(2), num(2))));
        assertEquals(3, eval(expr1));
        assertEquals(6, eval(expr2));
        assertEquals(2, eval(expr3));
        assertEquals(8, eval(expr4));
        assertEquals(0, eval(expr5));
    }

    @Test
    public void test04_exprVisitor() {
        assertEquals(10, evalWithVisitor(num(10)));
        assertEquals(4, evalWithVisitor(add(num(2), num(2))));
        assertEquals(3, evalWithVisitor(expr1));
        assertEquals(6, evalWithVisitor(expr2));
        assertEquals(2, evalWithVisitor(expr3));
        assertEquals(8, evalWithVisitor(expr4));
        assertEquals(0, evalWithVisitor(expr5));
    }

    @Test
    public void test05_ExpressionAllValues() {
        assertEquals(set(10), getAllValues(num(10)));
        assertEquals(set(2,8), getAllValues(add(num(2), num(8))));
        assertEquals(set(2, 3, 5, 8), getAllValues(expr5));
    }

    @Test
    public void test06_ExpressionAllValuesBaseVisitor() {
        assertEquals(set(10), getAllValuesBaseVisitor(num(10)));
        assertEquals(set(2,8), getAllValuesBaseVisitor(add(num(2), num(8))));
        assertEquals(set(2, 3, 5, 8), getAllValuesBaseVisitor(expr5));
    }


    @Test
    public void test07_ExpressionAllValuesBaseVisitorImp() {
        assertEquals(set(10), getAllValuesWithBaseVisitorImperative(num(10)));
        assertEquals(set(2,8), getAllValuesWithBaseVisitorImperative(add(num(2), num(8))));
        assertEquals(set(2, 3, 5, 8), getAllValuesWithBaseVisitorImperative(expr5));
    }

}
