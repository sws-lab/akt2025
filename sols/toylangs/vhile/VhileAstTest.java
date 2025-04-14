package toylangs.vhile;

import toylangs.vhile.ast.VhileNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static toylangs.vhile.ast.VhileNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VhileAstTest {

    @Test
    public void test01_num() {
        illegal("123");
        legal("x = 123", assign("x", num(123)));
        legal("x = -1", assign("x", num(-1)));
        legal("x = 0", assign("x", num(0)));
        illegal("x = 00");
        illegal("x = -0");
        illegal("x = - 1");

        legal("x = foo", assign("x", var("foo")));
        legal("bar = x", assign("bar", var("x")));
        illegal("escape = 10");
        illegal("x = while");
        illegal("x0 = 1");
        illegal("x_ = 1");

        // TODO: 09.06.21 kuidas testida seda, et Var reeglis + asemel * ja ANTLR hoiatab: "non-fragment lexer rule Var can match the empty string"?
        // need testid seda ei erista
        illegal("x =");
        illegal("= 1");
        illegal("x = _");
    }

    @Test
    public void test02_ops() {
        legal("foo = x + y", assign("foo", add(var("x"), var("y"))));
        legal("foo = x * y", assign("foo", mul(var("x"), var("y"))));
        legal("foo = (x * y)", assign("foo", mul(var("x"), var("y"))));
        legal("foo = x == y", assign("foo", eq(var("x"), var("y"))));
        legal("foo = x != y", assign("foo", neq(var("x"), var("y"))));
    }

    @Test
    public void test03_non_assoc() {
        illegal("x = x == y == z");
        illegal("x = x != y != z");
        legal("x = (x == y) == z", assign("x", eq(eq(var("x"), var("y")), var("z"))));
    }

    @Test
    public void test04_block() {
        legal("{}", block());
        legal("{ }", block());
        legal("{ x = 1 }", block(assign("x", num(1))));
        legal("{ x = 1; y = 2 }", block(assign("x", num(1)), assign("y", num(2))));
        illegal("{ x = 1; }");
        illegal("{ ; x = 1 }");
        illegal("{ x = 1; ; y = 2 }");
        illegal("x = 1;");
    }

    @Test
    public void test05_loop() {
        legal("while (x != 10) x = x+1", loop(neq(var("x"), num(10)), assign("x", add(var("x"), num(1)))));
        legal("while (y != 0) { x = x * y; y = y + -1 }", loop(neq(var("y"), num(0)), block(assign("x", mul(var("x"), var("y"))), assign("y", add(var("y"), num(-1)))))); // faktoriaal
        illegal("vhile (x != 10) x = x+1");
    }

    @Test
    public void test06_escape() {
        legal("escape", escape(1));
        legal("escape : 1", escape(1));
        legal("escape:2", escape(2));
        illegal("escape:-1"); // ASTi loomisel kontrollitakse!
        illegal("escape:");
        illegal("escape 0");
        illegal("escape:0");
    }

    @Test
    public void test07_prio_assoc() {
        legal("foo = x + y + z", assign("foo", add(add(var("x"), var("y")), var("z"))));
        legal("foo = x * y * z", assign("foo", mul(mul(var("x"), var("y")), var("z"))));
        legal("foo = x + y * z", assign("foo", add(var("x"), mul(var("y"), var("z")))));
        legal("foo = x + (y * z)", assign("foo", add(var("x"), mul(var("y"), var("z")))));
        legal("foo = (x + y) * z", assign("foo", mul(add(var("x"), var("y")), var("z"))));

        legal("foo = x == y + z", assign("foo", eq(var("x"), add(var("y"), var("z")))));
        legal("foo = x != y + z", assign("foo", neq(var("x"), add(var("y"), var("z")))));
        legal("foo = x == y * z", assign("foo", eq(var("x"), mul(var("y"), var("z")))));
        legal("foo = x != y * z", assign("foo", neq(var("x"), mul(var("y"), var("z")))));
        illegal("foo = x != y == z");
        illegal("foo = x == y != z");
    }

    @Test
    public void test08_multiple() {
        legal("{ escape ; escape }", block(escape(1), escape(1)));
        legal("while (x != 10) { x = x+1; escape; x = x+1 }", loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), escape(1), assign("x", add(var("x"), num(1))))));
        legal("while (x != 10) { x = x+1; while (x == 5) escape : 2}", loop(neq(var("x"), num(10)), block(assign("x", add(var("x"), num(1))), loop(eq(var("x"), num(5)), escape(2)))));
    }


    private void legal(String input, VhileNode expectedAst) {
        VhileNode actualAst = VhileAst.makeVhileAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            VhileAst.makeVhileAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
