package toylangs.parm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.parm.ast.ParmNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.parm.ast.ParmNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParmAstTest {

    @Test
    public void test01_basic() {
        legal("X", var("X"));
        legal("Kala", var("Kala"));
        legal("123", lit(123));
        legal("-00", lit(0));
        legal("-1", lit(-1));
        illegal("K20");
        illegal("kala");
        illegal("- 4");
        illegal("--4");
    }

    @Test
    public void test02_plus() {
        legal("X + Y", plus(var("X"), var("Y")));
        legal("X + Y + Z", plus(plus(var("X"), var("Y")), var("Z")));

        legal("X <- 100", up("X", lit(100)));
        illegal("1 2");
        illegal("");
    }

    @Test
    public void test03_up() {
        legal("X <- 100", up("X", lit(100)));
        legal("X <- Y", up("X", var("Y")));
        legal("Y <- X <- 100", up("Y", up("X", lit(100))));
        illegal("1 <- X");
        illegal("X <- 100 <- Y");
    }

    @Test
    public void test04_comp() {
        legal("1; 2", seq(lit(1), lit(2)));
        legal("1;\n2", seq(lit(1), lit(2)));
        legal("1| 2", par(lit(1), lit(2)));
        legal("1; 2 ;3", seq(seq(lit(1), lit(2)), lit(3)));
        legal("1 | 2 |3", par(par(lit(1), lit(2)), lit(3)));
        legal("1\n| 2\n |3", par(par(lit(1), lit(2)), lit(3)));
        illegal("1;"); // Semikoolon ei ole rea lõpetaja, vaid operaator.
        illegal("1|");
        illegal("; 1");
    }

    @Test
    public void test05_paren() {
        legal("(1)", (lit(1)));
        legal("X + (Y + Z)", plus(var("X"), plus(var("Y"), var("Z"))));
        legal("(Y <- X) + 100", plus(up("Y", var("X")), lit(100)));
        legal("1; (2 ;3)", seq(lit(1), seq(lit(2), lit(3))));
        legal("1 | (2|3)", par(lit(1), par(lit(2), lit(3))));
        illegal("()");
        illegal("((20)");
        illegal("(13 + 30))");
    }

    @Test
    public void test06_prio() {
        legal("X + Y ; Z", seq(plus(var("X"), var("Y")), var("Z")));
        legal("X ; Y + Z", seq(var("X"), plus(var("Y"), var("Z"))));
        legal("Y <- X + 100", up("Y", plus(var("X"), lit(100))));
        legal("Y <- X ; 100", seq(up("Y", var("X")), lit(100)));
        legal("Y <- X | 100", par(up("Y", var("X")), lit(100)));
        legal("1 | 2 ;3", seq(par(lit(1), lit(2)), lit(3)));
        legal("1 ; 2 |3", par(seq(lit(1), lit(2)), lit(3)));
    }

    @Test
    public void test07_examples() {
        // eraldi programmid
        legal("Kala <- X + 5", up("Kala", plus(var("X"), lit(5))));
        legal("1 ; 2", seq(lit(1), lit(2)));
        legal("X <- Y | Y <- X", par(up("X", var("Y")), up("Y", var("X"))));
        legal("3 + 3 ; (X <- 4) + 1", seq(plus(lit(3), lit(3)), plus(up("X", lit(4)), lit(1))));

        // kokkupandud programmid
        legal("""
                        Kala <- X + 5;
                        1 ; 2; A <- B <- 30;
                        (X <- Y | Y <- X);
                        3 + 3 ; (X <- -4) + 1""",
                seq(seq(seq(seq(seq(seq(up("Kala", plus(var("X"), lit(5))), lit(1)), lit(2)), up("A", up("B", lit(30)))), par(up("X", var("Y")), up("Y", var("X")))), plus(lit(3), lit(3))), plus(up("X", lit(-4)), lit(1))));
    }

    private void legal(String input, ParmNode expectedAst) {
        ParmNode actualAst = ParmAst.makeParmAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            ParmAst.makeParmAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
