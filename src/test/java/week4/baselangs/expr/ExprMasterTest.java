package week4.baselangs.expr;

import org.junit.Test;
import week4.baselangs.expr.ast.ExprNode;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static week4.baselangs.expr.ast.ExprNode.*;
import static week4.baselangs.expr.ExprMaster.*;

public class ExprMasterTest {

    private final ExprNode expr1 = div(add(num(5), add(num(3), neg(num(2)))), num(2));
    private final ExprNode expr2 = add(add(num(5), div(num(3), neg(num(2)))), num(2));
    private final ExprNode expr3 = div(div(num(8), num(2)), num(2));
    private final ExprNode expr4 = div(num(8), div(num(2), num(2)));
    private final ExprNode expr5 = add(neg(expr4), add(expr2, expr3));
    private final ExprNode expr6 = add(neg(expr4), neg(add(expr2, neg(expr3))));

    // 5 + -(3 + -(2 + -6) + 2))
    private final ExprNode demo1 = add(num(5), neg(add(num(3), add(neg(add(num(2), num(-6))), num(2)))));
    // 3+24/(4+8)+-(8/2+1)
    private final ExprNode demo2 = add(num(3), add(div(num(24), add(num(4), num(8))), neg(add(div(num(8), num(2)), num(1)))));
    // 7 + -(3/6 + -(9/3))
    private final ExprNode demo3 = add(num(7), neg(add(div(num(3), num(6)), neg(div(num(9), num(3))))));


    @Test
    public void testValueList() {
        assertEquals(Arrays.asList(5, 3, 2, 2), valueList(expr1));
        assertEquals(Arrays.asList(5, 3, 2, 2), valueList(expr2));
        assertEquals(Arrays.asList(8, 2, 2), valueList(expr3));
        assertEquals(Arrays.asList(8, 2, 2), valueList(expr4));
        assertEquals(Arrays.asList(8, 2, 2, 5, 3, 2, 2, 8, 2, 2), valueList(expr5));
        assertEquals(Arrays.asList(5, 3, 2, -6, 2), valueList(demo1));
        assertEquals(Arrays.asList(3, 24, 4, 8, 8, 2, 1), valueList(demo2));
    }


    @Test
    public void testReplaceDiv() {
        assertEquals("3", pretty(replaceDivs(expr1)));
        assertEquals("5+-1+2", pretty(replaceDivs(expr2)));
        assertEquals("-8+5+-1+2+2", pretty(replaceDivs(expr5)));
        assertEquals("5+-(3+-(2+-6)+2)", pretty(replaceDivs(demo1)));
        assertEquals("3+2+-(4+1)", pretty(replaceDivs(demo2)));
    }


    @Test
    public void testSignedValueList() {
        assertEquals(Collections.singletonList(5), signedValueList(num(5)));
        assertEquals(Collections.singletonList(-5), signedValueList(neg(num(5))));
        
        assertEquals(Collections.singletonList(3), signedValueList(replaceDivs(expr1)));
        assertEquals(Arrays.asList(5, -1, 2), signedValueList(replaceDivs(expr2)));
        assertEquals(Arrays.asList(-8, 5, -1, 2, 2), signedValueList(replaceDivs(expr5)));
        
        assertEquals(Arrays.asList(5, -3, 2, -6, -2), signedValueList(replaceDivs(demo1)));
        assertEquals(Arrays.asList(3, 2, -4, -1), signedValueList(replaceDivs(demo2)));
    }

    @Test
    public void testElimNeg() {
        assertEquals("(5+3+-2)/2", pretty(elimNeg(expr1)));
        assertEquals("5+3/-2+2", pretty(elimNeg(expr2)));
        assertEquals("-8/(2/2)+5+3/-2+2+8/2/2", pretty(elimNeg(expr5)));
        assertEquals("-8/(2/2)+-5+-3/-2+-2+8/2/2", pretty(elimNeg(expr6)));
        assertEquals("5+-3+2+-6+-2", pretty(elimNeg(demo1)));
        assertEquals("3+24/(4+8)+-8/2+-1", pretty(elimNeg(demo2)));
        assertEquals("7+-3/6+9/3", pretty(elimNeg(demo3)));
    }

    @Test
    public void testPretty() {
        assertEquals("10", pretty(num(10)));
        assertEquals("2+2", pretty(add(num(2), num(2))));
        assertEquals("8/(2+2)", pretty(div(num(8), add(num(2), num(2)))));
        assertEquals("(5+3+-2)/2", pretty(expr1));
        assertEquals("5+3/-2+2", pretty(expr2));
        assertEquals("8/2/2", pretty(expr3));
        assertEquals("8/(2/2)", pretty(expr4));
    }
}
