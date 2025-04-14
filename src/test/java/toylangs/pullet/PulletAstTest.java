package toylangs.pullet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.pullet.ast.PulletNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.pullet.ast.PulletNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PulletAstTest {

    @Test
    public void test01_literaalMuutuja() {
        legal("0", num(0));
        legal("97519", num(97519));
        legal("a", var("a"));
        legal("A", var("A"));
        legal("aBxxU", var("aBxxU"));

        illegal("");
        illegal("-1");
        illegal("+1");
        illegal("1 2 3");
        illegal("a77");
        illegal("8h");
        illegal("in");
        illegal("let");
        illegal("sum");
    }

    @Test
    public void test02_suludVahe() {
        legal("1-1", diff(num(1),num(1)));
        legal("82-aG", diff(num(82),var("aG")));
        legal("(a)", var("a"));
        legal("(((((((a)))))))", var("a"));
        legal("(1-x)-(c-55)", diff(diff(num(1),var("x")), diff(var("c"),num(55))));

        illegal("-1-a");
        illegal("()");
        illegal("-(10)");
        illegal("((((a)))");
        illegal("((a))))");
        illegal("((a))-4)-(5)");
        illegal("33--x");
        illegal("1+x");
    }

    @Test
    public void test03_lihtneLetSum() {
        legal("let x=5 in x", let("x",num(5),var("x")));
        legal("let x=(10-3) in x-100", let("x", diff(num(10),num(3)), diff(var("x"),num(100))));
        legal("sum i=1 to 4 in i", sum("i",num(1),num(4),var("i")));
        legal("sum i=1 to (10-x) in i-x", sum("i",num(1), diff(num(10),var("x")), diff(var("i"),var("x"))));
        legal("  let x  = 11    in\n" +
                        "10  -   r  ",
                let("x",num(11), diff(num(10),var("r"))));

        illegal("let x in x");
        illegal("let x=100");
        illegal("let 100 in 10");
        illegal("let y=19 in");
        illegal("sum x in x");
        illegal("sum x=1 to 5");
        illegal("sum x=-1 to 10 in x");
        illegal("sum x=1 in x");
        illegal("sum = 1 to 10");
    }

    @Test
    public void test04_assocPrio() {
        legal("let x = 1 - x in 11", let("x", diff(num(1),var("x")),num(11)));
        legal("""
                        let x = 44 in
                        let y = 2 in
                        let z = 0 in
                        x - y""",
                let("x",num(44),let("y",num(2),let("z",num(0), diff(var("x"),var("y"))))));
        legal("""
                        let a =
                            let b =
                                let c = 11 in c
                            in b - 10
                        in let b = 10
                        in a - b""",
                let("a",let("b",let("c",num(11),var("c")), diff(var("b"),num(10))),let("b",num(10), diff(var("a"),var("b")))));
        legal("""
                        let x =
                            sum i = 1 to 4 in i
                        in x""",
                let("x",sum("i",num(1),num(4),var("i")),var("x")));
        legal("let a = 1 in \n" +
                "    sum i = 5 to y in a - i", let("a",num(1),sum("i",num(5),var("y"), diff(var("a"),var("i")))));
        legal("""
                        sum i =\s
                            sum j = 1 to 100 in 0\s
                            to 5 in i""",
                sum("i",sum("j",num(1),num(100),num(0)),num(5),var("i")));
        legal("10 - x - let y = 10 in 100 - 9",
                diff(diff(num(10),var("x")),let("y",num(10), diff(num(100),num(9)))));

        illegal("let x = 10 let y = 11 in x - y");
    }

    @Test
    public void test05_varia() {
        legal("let x = 666 in (sum i = 0 to 3; j = 0 to i in i-j) - 1",
                let("x",num(666), diff(sum("i",num(0),num(3),sum("j",num(0),var("i"), diff(var("i"),var("j")))),num(1))));
        legal("""
                        sum\s
                            x = let y = 1 in y to 11;
                            y = 11 to 0;
                            z = x to y
                        in
                            let\s
                                a = 11;
                                b = 12
                            in x-a-b""",
                sum("x",let("y",num(1),var("y")),num(11),sum("y",num(11),num(0),sum("z",var("x"),var("y"),let("a",num(11),let("b",num(12), diff(diff(var("x"),var("a")),var("b"))))))));
    }


    private void legal(String input, PulletNode expectedAst) {
        PulletNode actualAst = PulletAst.makePulletAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            PulletAst.makePulletAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
