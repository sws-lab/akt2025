package proovieksam;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import proovieksam.ast.EstologNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static proovieksam.ast.EstologNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EstologAstTest {

    @Test
    public void test01_literals_variables() {
        legal("0", prog(lit(false)));  // 0
        legal("1", prog(lit(true)));  // 1
        legal("x", prog(var("x")));  // x
        legal("X", prog(var("X")));  // X
        legal("kala", prog(var("kala")));  // kala
        legal("abcdefgrstu", prog(var("abcdefgrstu")));  // abcdefgrstu

        illegal("2");
        illegal("-1");
        illegal("00");
        illegal("0x");
        illegal("0 0");
        illegal("x y");
        illegal("_");
    }

    @Test
    public void test02_operators() {
        legal("0 JA 0", prog(ja(lit(false), lit(false))));  // (0 JA 0)
        legal("0 VOI 1", prog(voi(lit(false), lit(true))));  // (0 VOI 1)
        legal("x VOI 1", prog(voi(var("x"), lit(true))));  // (x VOI 1)
        legal("x JA kalad", prog(ja(var("x"), var("kalad"))));  // (x JA kalad)
        legal("x NING kalad", prog(ja(var("x"), var("kalad"))));  // (x JA kalad)
        legal("x = 1", prog(vordus(var("x"), lit(true))));  // (x = 1)
        legal("x = z", prog(vordus(var("x"), var("z"))));  // (x = z)
        legal("(x = z) JA (0 VOI z)", prog(ja(vordus(var("x"), var("z")), voi(lit(false), var("z")))));  // ((x = z) JA (0 VOI z))
        legal("(((x = z) JA (0 VOI z)) NING y)", prog(ja(ja(vordus(var("x"), var("z")), voi(lit(false), var("z"))), var("y"))));  // (((x = z) JA (0 VOI z)) JA y)

        illegal("0 JA 3");
        illegal("0 JA 00");
        illegal("0 JA");
        illegal("JA 0");
        illegal("x == y");
        illegal("x == y");
        illegal("x =(= y)");
        illegal("(((x JA 0)");
        illegal("(((x JA 0)))))");
        illegal("x JA NING y");
        illegal("x ja y");
        illegal("x ning y");
        illegal("x voi y");
    }

    @Test
    public void test03_kui() {
        legal("KUI 0 SIIS 1 MUIDU 0", prog(kui(lit(false), lit(true), lit(false))));  // (KUI 0 SIIS 1 MUIDU 0)
        legal("KUI x SIIS y MUIDU maja", prog(kui(var("x"), var("y"), var("maja"))));  // (KUI x SIIS y MUIDU maja)
        legal("KUI (x = 1) SIIS (z VOI y) MUIDU maja", prog(kui(vordus(var("x"), lit(true)), voi(var("z"), var("y")), var("maja"))));  // (KUI (x = 1) SIIS (z VOI y) MUIDU maja)

        illegal("KUI 0 MUIDU 1");
        illegal("SIIS 0 MUIDU 1");
        illegal("KUI SIIS 0 MUIDU 1");
        illegal("KUI y SIIS MUIDU 1");
        illegal("KUI y SIIS a MUIDU");
        illegal("KUI y SIIS a MUIDU b MUIDU x");
        illegal("KUI y KUI x SIIS a MUIDU b");
        illegal("KUI y SIIS JA a MUIDU b");
        illegal("KUI y SIIS ((a) MUIDU b");
    }

    @Test
    public void test04_priority() {
        legal("a VOI b JA c", prog(voi(var("a"), ja(var("b"), var("c")))));  // (a VOI (b JA c))
        legal("a VOI b NING c", prog(ja(voi(var("a"), var("b")), var("c"))));  // ((a VOI b) JA c)
        legal("a JA b NING c", prog(ja(ja(var("a"), var("b")), var("c"))));  // ((a JA b) JA c)
        legal("a JA (b NING c)", prog(ja(var("a"), ja(var("b"), var("c")))));  // (a JA (b JA c))
        legal("a NING b JA c", prog(ja(var("a"), ja(var("b"), var("c")))));  // (a JA (b JA c))
        legal("a JA b JA c NING x VOI e", prog(ja(ja(ja(var("a"), var("b")), var("c")), voi(var("x"), var("e")))));  // (((a JA b) JA c) JA (x VOI e))
        legal("a JA b JA c NING x VOI e JA x JA u VOI k NING l VOI a", prog(ja(ja(ja(ja(var("a"), var("b")), var("c")), voi(voi(var("x"), ja(ja(var("e"), var("x")), var("u"))), var("k"))), voi(var("l"), var("a")))));  // ((((a JA b) JA c) JA ((x VOI ((e JA x) JA u)) VOI k)) JA (l VOI a))
        legal("a = b JA c = x VOI 1", prog(voi(ja(vordus(var("a"), var("b")), vordus(var("c"), var("x"))), lit(true))));  // (((a = b) JA (c = x)) VOI 1)
        legal("KUI 1 SIIS y MUIDU o", prog(kui(lit(true), var("y"), var("o"))));  // (KUI 1 SIIS y MUIDU o)
        legal("KUI (x = y) = 0 SIIS a NING b VOI x MUIDU g VOI h NING a NING f", prog(kui(vordus(vordus(var("x"), var("y")), lit(false)), ja(var("a"), voi(var("b"), var("x"))), ja(ja(voi(var("g"), var("h")), var("a")), var("f")))));  // (KUI ((x = y) = 0) SIIS (a JA (b VOI x)) MUIDU (((g VOI h) JA a) JA f))
    }

    @Test
    public void test05_defines() {
        legal("a := 0; a", prog(var("a"), def("a", lit(false))));
        legal("a := 0; b := 0; a", prog(var("a"), def("a", lit(false)), def("b", lit(false))));
        legal("a:=0 ;b   :=  0;a", prog(var("a"), def("a", lit(false)), def("b", lit(false))));
        legal("a:=0 ;b   :=  a = a;b", prog(var("b"), def("a", lit(false)), def("b", vordus(var("a"), var("a")))));

        illegal("a := 0");
        illegal("a = 0; a");
        illegal("a : = 0; a");
        illegal("a := 0; a; b := 0");
        illegal("; a");
        illegal("a := 0;");
        illegal("a;");
    }

    @Test
    public void test06_examples() {
        legal("""
                        x := 0;
                        y := 1;
                        a := (x JA y);
                        b := (x VOI y);

                        (KUI (x = y) SIIS a MUIDU b)""",
                prog(
                        kui(vordus(var("x"), var("y")), var("a"), var("b")),

                        def("x", lit(false)),
                        def("y", lit(true)),
                        def("a", ja(var("x"), var("y"))),
                        def("b", voi(var("x"), var("y")))
                ));

        legal("""
                        a := kala JA lind;  \s
                        b := 0 = (kass VOI a NING kala);\s
                        c := KUI a = b SIIS 1 MUIDU b JA c;
                        KUI a SIIS KUI c SIIS kala MUIDU hiir""",
                prog(
                        kui(var("a"), kui(var("c"), var("kala"), var("hiir"))),

                        def("a", ja(var("kala"), var("lind"))),
                        def("b", vordus(lit(false), ja(voi(var("kass"), var("a")), var("kala")))),
                        def("c", kui(vordus(var("a"), var("b")), lit(true), ja(var("b"), var("c"))))
                ));
    }

    @Test
    public void test07_kui_without_muidu() {
        fail("See test avalikustatakse pärast eksamit! Kas näiteks (KUI (x = 1) SIIS y) ka töötab?");
    }

    @Test
    public void test08_associativity() {
        fail("See test avalikustatakse pärast eksamit! Kas näiteks (a = b = c) on keelatud?");
    }

    private void legal(String input, EstologNode expectedAst) {
        EstologNode actualAst = EstologAst.makeEstologAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            EstologAst.makeEstologAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
