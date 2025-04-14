package toylangs.dialoog;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.dialoog.ast.DialoogNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.dialoog.ast.DialoogNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DialoogAstTest {

    @Test
    public void test01_basic() {
        legal("Arvuta: 5", prog(decls(), il(5)));
        legal("Arvuta: 0", prog(decls(), il(0)));
        illegal("Arvuta: 05");

        legal("Arvuta: jah", prog(decls(), bl(true)));
        legal("Arvuta: ei", prog(decls(), bl(false)));
        illegal("Arvuta: EI");

        legal("Arvuta: x", prog(decls(), var("x")));
        legal("Arvuta: foobar", prog(decls(), var("foobar")));
        legal("Arvuta: true", prog(decls(), var("true")));
        illegal("Arvuta: FOO");
        illegal("Arvuta: 5 jura");
    }

    @Test
    public void test02_decls() {

        legal("x on int! Arvuta: 0", prog(decls(iv("x")), il(0)));
        legal("x on bool! Arvuta: 0", prog(decls(bv("x")), il(0)));
        legal("x on intike! Arvuta: 0", prog(decls(iv("x")), il(0)));
        legal("x on boolakas! Arvuta: 0", prog(decls(bv("x")), il(0)));
        illegal("x on in! Arvuta: 0");
        illegal("x on boo! Arvuta: 0");

        legal("x on bool! y on int! z on bool! Arvuta: 0", prog(decls(bv("x"), iv("y"), bv("z")), il(0)));
        illegal("x on int Arvuta: 0");
        illegal("x on int!! Arvuta: 0");
        illegal("!x on int Arvuta: 0");
        illegal("!x on int! Arvuta: 0");
        illegal("x : int! Arvuta: 0");
        illegal("xonbool! Arvuta: 0");
    }

    @Test
    public void test03_ops() {
        legal("Arvuta: -x + y - z", prog(decls(), sub(add(neg(var("x")), var("y")), var("z"))));
        legal("Arvuta: x / -y * z", prog(decls(), mul(div(var("x"), neg(var("y"))), var("z"))));
        illegal("Arvuta: x % y");

        legal("Arvuta: a | b & !c", prog(decls(), or(var("a"), and(var("b"), not(var("c"))))));
        illegal("Arvuta: a && b");
        illegal("Arvuta: a || b");

        legal("Arvuta: x on y", prog(decls(), eq(var("x"), var("y"))));
        illegal("Arvuta: x == y");


        // assoc
        legal("Arvuta: a - b - c", prog(decls(), sub(sub(var("a"), var("b")), var("c"))));

        // prio
        legal("Arvuta: x + y * z", prog(decls(), add(var("x"), mul(var("y"), var("z")))));
        legal("Arvuta: a - y / z", prog(decls(), sub(var("a"), div(var("y"), var("z")))));
        legal("Arvuta: a & b on c + d", prog(decls(), and(var("a"), eq(var("b"), add(var("c"), var("d"))))));
        legal("Arvuta: 1 - 2 + 3", prog(decls(), add(sub(il(1), il(2)), il(3))));
        legal("Arvuta: 1 + 2 - 3", prog(decls(), sub(add(il(1), il(2)), il(3))));
    }

    @Test
    public void test04_assocprio() {
        // assoc
        legal("Arvuta: a - b - c", prog(decls(), sub(sub(var("a"), var("b")), var("c"))));

        // prio
        legal("Arvuta: x + y * z", prog(decls(), add(var("x"), mul(var("y"), var("z")))));
        legal("Arvuta: a - y / z", prog(decls(), sub(var("a"), div(var("y"), var("z")))));
        legal("Arvuta: a & b on c + d", prog(decls(), and(var("a"), eq(var("b"), add(var("c"), var("d"))))));
        legal("Arvuta: 1 - 2 + 3", prog(decls(), add(sub(il(1), il(2)), il(3))));
        legal("Arvuta: 1 + 2 - 3", prog(decls(), sub(add(il(1), il(2)), il(3))));
    }

    @Test
    public void test05_recognize_exprs() {
        legal("Arvuta: Oota 2 + 3 Valmis", prog(decls(), add(il(2), il(3))));
        legal("Arvuta: Oota 2 + 3 Valmis * 5", prog(decls(), mul(add(il(2), il(3)), il(5))));
        illegal("Arvuta: (2 + 3)");
        illegal("Arvuta: Oota 2 + 3");

        legal("Arvuta: Kas a? Jah: 5 Ei: 10 Selge", prog(decls(), ifte(var("a"), il(5), il(10))));
        illegal("Arvuta: Kas a? Jah: 5 Ei: 10");
    }


    @Test
    public void test06_demo() {
        legal("""
                        a on bool! x on int!
                        Arvuta:\s
                          2 * Oota 2 + 2 Valmis -
                          Kas 5 + 5 on 10?\s
                            Jah: Kas 5 + 5 on 10?\s
                                   Jah: 35 * x
                                   Ei: 100\s
                                 Selge\s
                            Ei: 100\s
                          Selge\s
                        + 30 + Kas jah & a?
                                    Jah: 10
                                    Ei: 300
                               Selge""",
                prog(decls(bv("a"), iv("x")),
                        add(add(sub(mul(il(2), add(il(2), il(2))),
                                ifte(eq(add(il(5), il(5)), il(10)),
                                        ifte(eq(add(il(5), il(5)), il(10)),
                                                mul(il(35), var("x")),
                                                il(100)
                                        ),
                                        il(100)
                                )
                                ), il(30)), ifte(and(bl(true), var("a")),
                                il(10),
                                il(300))
                        )
                ));
    }


    private void legal(String input, DialoogNode expectedAst) {
        DialoogNode actualAst = DialoogAst.makeDialoogAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            DialoogAst.makeDialoogAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
