package toylangs.bolog;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.bolog.ast.BologNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.bolog.ast.BologNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BologAstTest {

    @Test
    public void test01_basic() {
        legal("X", var("X"));
        legal("JAH", tv(true));
        legal("JAH\n", tv(true));
        legal("EI", tv(false));
        legal("EIP", var("EIP"));
        illegal("X jura");
        illegal("x");
        illegal("Kala");
    }

    @Test
    public void test02_ops() {
        legal("!X", nand(tv(true), var("X")));
        legal("X && P", nand(tv(true), nand(var("X"), var("P"))));
        legal("X || Y", nand(nand(tv(true), var("X")), nand(tv(true), var("Y"))));
        legal("X <> Z", nand(tv(true), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Z"))), nand(var("X"), var("Z")))));
        illegal("X Y");
    }

    @Test
    public void test03_assoc() {
        legal("!!X", nand(tv(true), nand(tv(true), var("X"))));
        legal("X && P && Q", nand(tv(true), nand(nand(tv(true), nand(var("X"), var("P"))), var("Q"))));
        legal("X || Y || Z", nand(nand(tv(true), nand(nand(tv(true), var("X")), nand(tv(true), var("Y")))), nand(tv(true), var("Z"))));
        legal("X <> Y <> Z", nand(tv(true), nand(nand(nand(tv(true), nand(tv(true), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Y"))), nand(var("X"), var("Y"))))), nand(tv(true), var("Z"))), nand(nand(tv(true), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Y"))), nand(var("X"), var("Y")))), var("Z")))));
        illegal("X ||");
        illegal("|| Z");
    }

    @Test
    public void test04_prio() {
        legal("(X)", var("X"));
        legal("X && P || Z", nand(nand(tv(true), nand(tv(true), nand(var("X"), var("P")))), nand(tv(true), var("Z"))));
        legal("(X && P) || Z", nand(nand(tv(true), nand(tv(true), nand(var("X"), var("P")))), nand(tv(true), var("Z"))));
        legal("X && (P || Z)", nand(tv(true), nand(var("X"), nand(nand(tv(true), var("P")), nand(tv(true), var("Z"))))));
        legal("X || P && Z", nand(nand(tv(true), var("X")), nand(tv(true), nand(tv(true), nand(var("P"), var("Z"))))));
        legal("X || (P && Z)", nand(nand(tv(true), var("X")), nand(tv(true), nand(tv(true), nand(var("P"), var("Z"))))));
        legal("(X || P) && Z", nand(tv(true), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("P"))), var("Z"))));
        legal("X || Y <> Z", nand(tv(true), nand(nand(nand(tv(true), nand(nand(tv(true), var("X")), nand(tv(true), var("Y")))), nand(tv(true), var("Z"))), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Y"))), var("Z")))));
        legal("(X || Y) <> Z", nand(tv(true), nand(nand(nand(tv(true), nand(nand(tv(true), var("X")), nand(tv(true), var("Y")))), nand(tv(true), var("Z"))), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Y"))), var("Z")))));
        legal("X || (X <> Z)", nand(nand(tv(true), var("X")), nand(tv(true), nand(tv(true), nand(nand(nand(tv(true), var("X")), nand(tv(true), var("Z"))), nand(var("X"), var("Z")))))));
        illegal("((X)");
        illegal("(X))");
    }

    @Test
    public void test05_kui() {
        legal("X kui P", imp(var("X"), var("P")));
        legal("X kui P ja Q", imp(var("X"), var("P"), var("Q")));
        legal("X kui P, Q ja Q", imp(var("X"), var("P"), var("Q"), var("Q")));
        legal("X kui", imp(var("X")));
        illegal("kui X");
    }

    @Test
    public void test06_demo() {
        legal("""
                        X kui P ja Q
                        X && P kui P, P||Q ja (P kui Q ja R)
                        !X && P kui R && JAH || EI
                        """,
                imp(nand(tv(true), nand(var("X"), var("P"))), var("P"), nand(nand(tv(true), var("P")), nand(tv(true), var("Q"))), imp(var("P"), var("Q"), var("R"))),
                imp(var("X"), var("P"), var("Q")),
                imp(nand(tv(true), nand(nand(tv(true), var("X")), var("P"))), nand(nand(tv(true), nand(tv(true), nand(var("R"), tv(true)))), nand(tv(true), tv(false)))));

        illegal("""
                X kui P, Q
                X && P kui P, P||Q ja P kui Q ja R
                !X && P kui R && JAH || EI
                """);
    }

    @Test
    public void test07_examples() {
        legal("""
                        X kui A, B, C ja D
                        X <> P kui A ja B
                        !X && P kui R && JAH ja EI
                        (AB kui Z) && KALA
                        JAH || EI kui JAH""",
                imp(var("X"), var("A"), var("B"), var("C"), var("D")),
                imp(xor(var("X"), var("P")), var("A"), var("B")),
                imp(and(not(var("X")), var("P")), and(var("R"), tv(true)), tv(false)),
                and(imp(var("AB"), var("Z")), var("KALA")),
                imp(or(tv(true), tv(false)), tv(true)));

        legal("""
                        X kui P ja Q
                        X && P kui P, P||Q ja (P kui Q ja R)
                        (AB kui Z) && KALA
                        !X && P kui R <> JAH || EI
                        JAH""",
                imp(var("X"), var("P"), var("Q")),
                imp(and(var("X"), var("P")), var("P"), or(var("P"), var("Q")), imp(var("P"), var("Q"), var("R"))),
                and(imp(var("AB"), var("Z")), var("KALA")),
                imp(and(not(var("X")), var("P")), xor(var("R"), or(tv(true), tv(false)))),
                tv(true));
    }

    private void legal(String input, BologNode... expectedAsts) {
        Set<BologNode> actualAst = BologAst.makeBologAst(input);
        Set<BologNode> expectedAst = new HashSet<>(Arrays.asList(expectedAsts));
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            BologAst.makeBologAst(input);
            fail("Vigane sisend!");
        } catch (Exception ignored) {

        }
    }
}
