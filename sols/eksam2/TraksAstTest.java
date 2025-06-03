package eksam2;

import eksam2.ast.TraksNode;
import eksam2.ast.TraksStmt;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static eksam2.ast.TraksNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TraksAstTest {

    @Test
    public void test01_numvar() {
        illegal("123");
        legal("X <- 123", block(assign("X", num(123))));
        legal("X <- 0",  block(assign("X", num(0))));
        illegal("X <- 00");
        illegal("X <- -0");
        illegal("X <- - 1");
        illegal("X <- 123.0");

        legal("Check <- 0",  block(assign("Check", num(0))));
        legal("X <- Foo",  block(assign("X", var("Foo"))));
        legal("Bar <- X",  block(assign("Bar", var("X"))));
        illegal("ChEcK <- 100");
        illegal("check <- 10");
        illegal("X <- traks");
        illegal("X0 <- 1");
        illegal("x <- 1");
        illegal("X <- -1");
        illegal("X <-");
        illegal("<- 123");
    }

    @Test
    public void test02_ops() {
        legal("Foo <- X + Y",  block(assign("Foo", add(var("X"), var("Y")))));
        legal("Foo <- X - Y",  block(assign("Foo", sub(var("X"), var("Y")))));
        legal("Foo <- (X + Y)",  block(assign("Foo", add(var("X"), var("Y")))));
        legal("Foo <- (X - Y) + Z",  block(assign("Foo", add(sub(var("X"), var("Y")), var("Z")))));
        legal("Foo <- X >= Y",  block(assign("Foo", geq(var("X"), var("Y")))));
    }

    @Test
    public void test03_program() {
        legal("",  block());
        legal("  ",  block());
        legal("X <- 1",  block(assign("X", num(1))));
        legal("X <- 1; Y <- 2",  block(assign("X", num(1)), assign("Y", num(2))));
        illegal("X <- 1;");
        illegal("; X <- 1");
        illegal("X <- 1; ; Y <- 2");
    }

    @Test
    public void test04_block() {
        legal("{}",  block(block()));
        legal("{ }",  block(block()));
        legal("{ X <- 1 }",  block(block(assign("X", num(1)))));
        legal("{ X <- 1; Y <- 2 }",  block(block(assign("X", num(1)), assign("Y", num(2)))));
        legal(" X <- 1; {Y <- 2} ", block(
                assign("X", num(1)),
                block(assign("Y", num(2)))));
        legal(" {X <- 1} ; Y <- 2 ", block(
                block(assign("X", num(1))),
                assign("Y", num(2))));
        legal(" X <- 1; {{ Y <- 2 }; X <- 3 }", block(
                assign("X", num(1)),
                block(
                        block(assign("Y", num(2))),
                        assign("X", num(3)))));
        legal(" X <- 1; { Y <- 2 ; {X <- 3} }", block(
                assign("X", num(1)),
                block(
                        assign("Y", num(2)),
                        block(assign("X", num(3))))));

        illegal("{ X <- 1; }");
        illegal("{ ; X <- 1 }");
        illegal("{ X <- 1; ; Y <- 2 }");
    }

    @Test
    public void test05_traksaction() {
        legal("traks X <- 5", block(action(assign("X", num(5)))));

        legal("traks { X <- 10; check 1 }",  block(
                action(block(
                        assign("X", num(10)),
                        check(num(1))))));
        legal("traks { X <- 10; check 0 }",  block(
                action(block(
                        assign("X", num(10)),
                        check(num(0))))));

        legal("Raha <- 10; traks {Y <- Y-Raha; check Y >= 0; X <- X+Raha}; Tulemus <- X", block(
                assign("Raha", num(10)),
                action(block(
                        assign("Y", sub(var("Y"), var("Raha"))),
                        check(geq(var("Y"), num(0))),
                        assign("X", add(var("X"), var("Raha"))))),
                assign("Tulemus", var("X"))));

        legal("traks traks traks check 5",  block(action(action(action(check(num(5)))))));
        legal("traks{}",  block(action(block())));
        legal("traks X <- 5; Y <- 6", block(action(assign("X", num(5))), assign("Y", num(6))));
        legal("traks check1", block(action(check(num(1)))));

        illegal("traks 5");
        illegal("traks check");
    }

    @Test
    public void test06_when() {
        legal("Y <- 2 when X",  block(
                action(block(
                        check(var("X")),
                        assign("Y", num(2))))));
        legal("Y <- 2 when0",  block(action(block(check(num(0)), assign("Y", num(2))))));
        legal("Y <- 2when 0",  block(action(block(check(num(0)), assign("Y", num(2))))));

        legal("{} when 0",  block(
                action(block(
                        check(num(0)),
                        block()))));
        legal("{} when 1 when 0", block(
                action(block(
                        check(num(0)),
                        action(block(check(num(1)), block()))))));
        legal("Y <- 2 when X when 0", block(
                action(block(
                        check(num(0)),
                        action(block(
                                check(var("X")),
                                assign("Y", num(2))))))));

        illegal("{} when {} when 0");
        illegal("{} when Y <- 2 when X");
    }

    @Test
    public void test07_prio_assoc() {
        legal("Foo <- X + Y + Z",  block(assign("Foo", add(add(var("X"), var("Y")), var("Z")))));
        legal("Foo <- X - Y - Z",  block(assign("Foo", sub(sub(var("X"), var("Y")), var("Z")))));
        legal("Foo <- X - Y + Z",  block(assign("Foo", add(sub(var("X"), var("Y")), var("Z")))));
        legal("Foo <- X - (Y + Z)",  block(assign("Foo", sub(var("X"), add(var("Y"), var("Z"))))));
        legal("Foo <- X >= Y + Z",  block(assign("Foo", geq(var("X"), add(var("Y"), var("Z"))))));
        legal("Foo <- X + Y >= Z",  block(assign("Foo", geq(add(var("X"), var("Y")), var("Z")))));
        illegal("Foo <- X >= Y >= Z");
    }

    @Test
    public void test08_multiple() {
        legal("Y <- 9; traks { X <- Y; check Y }",  block(assign("Y", num(9)), action(block(assign("X", var("Y")), check(var("Y"))))));
        legal("Y <- 0; traks { X <- Y; check Y }",  block(assign("Y", num(0)), action(block(assign("X", var("Y")), check(var("Y"))))));
        legal("X <- 10; check 0",  block(assign("X", num(10)), check(num(0))));
        legal("traks { X <- 10; traks { Y <- 20; check 1 } }",  block(action(block(assign("X", num(10)), action(block(assign("Y", num(20)), check(num(1))))))));
        legal("traks { X <- 10; traks { Y <- 20; check 0 } }",  block(action(block(assign("X", num(10)), action(block(assign("Y", num(20)), check(num(0))))))));
        legal("traks { X <- 10; traks { Y <- 0; check 0 }; check Y }",  block(action(block(assign("X", num(10)), action(block(assign("Y", num(0)), check(num(0)))), check(var("Y"))))));
        legal("traks { X <- 10; traks { Y <- 0; check 1 }; check Y }",  block(action(block(assign("X", num(10)), action(block(assign("Y", num(0)), check(num(1)))), check(var("Y"))))));

        illegal("X <- 10; check 0;");
        illegal("X <- 10 check 0");

        illegal("XY <- 5");
    }


    private void legal(String input, TraksNode expectedAst) {
        TraksStmt actualAst = TraksAst.makeTraksAst(input);
        assertEquals(expectedAst, actualAst);
    }

    @SuppressWarnings("CatchMayIgnoreException") // IntelliJ rumaluse vastu...
    private void illegal(String input) {
        try {
            TraksAst.makeTraksAst(input);
            fail("expected parse error: " + input);
        } catch (Exception e) {
            assertFalse(e instanceof ClassCastException);
        }
    }
}
