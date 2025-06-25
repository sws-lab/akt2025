package eksam3;

import eksam3.ast.SimpNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static eksam3.ast.SimpNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpAstTest {

    @Test
    public void test01_num() {
        legal("5;", prog(num(5)));
        legal("123;", prog(num(123)));
        legal("0;", prog(num(0)));

        illegal("5"); // semikoolon puudu!
        illegal("05;");
    }

    @Test
    public void test02_mem() {
        legal("m1;", prog(mem(1)));
        legal("m7;", prog(mem(7)));

        illegal("m0;");
        illegal("m10;");
        illegal("x;");
        illegal("foo;");
        illegal("_;");
    }

    @Test
    public void test03_ops() {
        legal("5+6;", prog(add(num(5), num(6))));
        legal("37*9;", prog(mul(num(37), num(9))));
        illegal("56++;");
        illegal("5++6;");
        illegal("5**6;");
    }

    @Test
    public void test04_paren() {
        legal("(5+6);", prog(add(num(5), num(6))));
        legal("(37)*9;", prog(mul(num(37), num(9))));
        legal("37*(9*100);", prog(mul(num(37), mul(num(9), num(100)))));
        legal("(37);", prog(num(37)));

        illegal("5(+6);");
        illegal("(1;");
        illegal("1);");
        illegal("();");
    }

    @Test
    public void test05_neg() {
        legal("-1;", prog(num(-1)));
        legal("-2000;", prog(num(-2000)));
        legal("37+-9;", prog(add(num(37), num(-9))));
        legal("5+6;", prog(add(num(5), num(6))));
        legal("37*-9;", prog(mul(num(37), num(-9))));

        illegal("- 5;");
        illegal("-0;");
        illegal("--9;");
        illegal("5-+6;");
        illegal("5(+6);");
        illegal("5-6;");
        illegal("-(9+8);");
    }

    @Test
    public void test06_semi() {
        legal("1; 2;", prog(num(1), num(2)));
        legal("1; m1 + 1; m1;", prog(num(1), add(mem(1), num(1)), mem(1)));
        legal("1 ; 2;", prog(num(1), num(2)));
        legal("1;2;", prog(num(1), num(2)));
        legal(" 1; 2 ; ", prog(num(1), num(2)));

        illegal("1; 2");
        illegal("1 5; 2;");
    }

    @Test
    public void test07_prioassoc() {
        fail("See test avalikustatakse pärast eksamit! Kontrollime lihtsalt aritmeetika prioriteete ja assotsiatiivsust.");
    }

    @Test
    public void test08_memdef() {
        fail("See test avalikustatakse pärast eksamit! Kas lihtsalt 'm' on käsitletud?");
    }


    private void legal(String input, SimpNode expectedAst) {
        SimpNode actualAst = SimpAst.makeSimpAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            SimpAst.makeSimpAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}

