package toylangs.hulk;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.hulk.ast.HulkNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static toylangs.hulk.ast.HulkNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HulkAstTest {

    @Test
    public void test01_literal() {
        legal("A := A", prog(stmt('A', var('A'), null)));
        legal("B := X", prog(stmt('B', var('X'), null)));
        legal("C := {a}", prog(stmt('C', lit('a'), null)));
        legal("D := {x, y, z}", prog(stmt('D', lit('x', 'y', 'z'), null)));
        legal("D := {}", prog(stmt('D', lit(), null)));

        illegal("A");
        illegal("A = A");
        illegal("A == A");
        illegal("A := x");
        illegal("x := A");
        illegal("AB := A");
        illegal("A := AB");
        illegal("A := A B");
        illegal("D := {X, Y}");
        illegal("D := {x, y,}");
    }

    @Test
    public void test02_recognize_avaldis() {
        legal("A := A + B",
                prog(stmt('A', binop('+', var('A'), var('B')), null)));
        legal("C := A & B",
                prog(stmt('C', binop('&', var('A'), var('B')), null)));
        legal("C := {x} - B",
                prog(stmt('C', binop('-', lit('x'), var('B')), null)));
        legal("B := {x} + {y, u} + A + V + {k, m}",
                prog(stmt('B', binop('+', binop('+', binop('+', binop('+', lit('x'), lit('u', 'y')), var('A')), var('V')), lit('k', 'm')), null)));
        legal("B := {x} - {y, u} + A & V + {k, m}",
                prog(stmt('B', binop('+', binop('&', binop('+', binop('-', lit('x'), lit('u', 'y')), var('A')), var('V')), lit('k', 'm')), null)));
        legal("A := ({x} - {y, u}) + (A & V + {k, m} & ({}))",
                prog(stmt('A', binop('+', binop('-', lit('x'), lit('u', 'y')), binop('&', binop('+', binop('&', var('A'), var('V')), lit('k', 'm')), lit())), null)));

        illegal("A = A");
        illegal("A := A + x");
        illegal("A := -B");
        illegal("A + B");
        illegal("A +- B");
        illegal("A + B +");
        illegal("& A + B");
    }

    @Test
    public void test03_cond() {
        legal("A := B | A subset A",
                prog(stmt('A', var('B'), cond(var('A'), var('A')))));
        legal("A := A + B & C | A subset A",
                prog(stmt('A', binop('&', binop('+', var('A'), var('B')), var('C')), cond(var('A'), var('A')))));
        legal("A := A - {x, y} | {x} subset {x, y}",
                prog(stmt('A', binop('-', var('A'), lit('x', 'y')), cond(lit('x'), lit('x', 'y')))));
        legal("A := A - {x, y} | {x} & A subset {x, y} - {y}",
                prog(stmt('A', binop('-', var('A'), lit('x', 'y')), cond(binop('&', lit('x'), var('A')), binop('-', lit('x', 'y'), lit('y'))))));
        legal("A := A + {x, y} | ({x} & A) + X subset {x, y, z, a, b} - ({y} + A)",
                prog(stmt('A', binop('+', var('A'), lit('x', 'y')), cond(binop('+', binop('&', lit('x'), var('A')), var('X')), binop('-', lit('a', 'b', 'x', 'y', 'z'), binop('+', lit('y'), var('A')))))));
        legal("A := A\nB := X",
                prog(stmt('A', var('A'), null), stmt('B', var('X'), null)));
        legal("""
                        A := A
                        B := X
                        C := {a}
                        D := {x, y, z}
                        A := B | A subset A
                        A := A - {x, y} | {x} & A subset {x, y} - {y}""",
                prog(stmt('A', var('A'), null),
                        stmt('B', var('X'), null),
                        stmt('C', lit('a'), null),
                        stmt('D', lit('x', 'y', 'z'), null),
                        stmt('A', var('B'), cond(var('A'), var('A'))),
                        stmt('A', binop('-', var('A'), lit('x', 'y')), cond(binop('&', lit('x'), var('A')), binop('-', lit('x', 'y'), lit('y'))))));

        illegal("A := B | a subset A");
        illegal("A := B | A subset a");
        illegal("A := B  A subset A");
        illegal("A := B || A subset A");
        illegal("A := B | A subset A subset A");
        illegal("A subset A");
        illegal("A := B | A in A");
        illegal("A := B A := B");
        illegal("A := A\n");
        illegal("A := A\n\nB := X");
    }

    @Test
    public void test04_lisasyntaks() {
        legal("A := A + {x}",
                prog(stmt('A', binop('+', var('A'), lit('x')), null)));
        legal("A <- x",
                prog(stmt('A', binop('+', var('A'), lit('x')), null)));
        legal("A <- x, y, z, a",
                prog(stmt('A', binop('+', var('A'), lit('a', 'x', 'y', 'z')), null)));
        legal("A := A - {x, y, z}",
                prog(stmt('A', binop('-', var('A'), lit('x', 'y', 'z')), null)));
        legal("A -> x, y, z",
                prog(stmt('A', binop('-', var('A'), lit('x', 'y', 'z')), null)));
        legal("A := A + {x} | {x} subset C",
                prog(stmt('A', binop('+', var('A'), lit('x')), cond(lit('x'), var('C')))));
        legal("A <- x | x in C",
                prog(stmt('A', binop('+', var('A'), lit('x')), cond(lit('x'), var('C')))));
        legal("A -> x | y in C + {x}",
                prog(stmt('A', binop('-', var('A'), lit('x')), cond(lit('y'), binop('+', var('C'), lit('x'))))));

        legal("""
                        A := A + {x}
                        A <- x
                        A <- x, y, z, a
                        A <- x | x in C
                        A -> x | y in C + {x}""",
                prog(stmt('A', binop('+', var('A'), lit('x')), null),
                        stmt('A', binop('+', var('A'), lit('x')), null),
                        stmt('A', binop('+', var('A'), lit('a', 'x', 'y', 'z')), null),
                        stmt('A', binop('+', var('A'), lit('x')), cond(lit('x'), var('C'))),
                        stmt('A', binop('-', var('A'), lit('x')), cond(lit('y'), binop('+', var('C'), lit('x'))))));

        illegal("A -> A");
        illegal("A <- A");
        illegal("x <- A");
        illegal("x -> A");
        illegal("A := {x} | A in A");
        illegal("A := A | x in x");
        illegal("A := A | x in X in X");
        illegal("A - > x");
        illegal("A < - x");
    }

    @Test
    public void test05_varia() {
        legal(
                """
                        A := B
                        A := {x, y, z} + A
                        B := A & (B - C) & {}
                        A := A + {x, y} | x in A + B
                        A <- x | {x} + {y} subset A
                        A -> a, b, c""",
                prog(stmt('A', var('B'), null),
                        stmt('A', binop('+', lit('x', 'y', 'z'), var('A')), null),
                        stmt('B', binop('&', binop('&', var('A'), binop('-', var('B'), var('C'))), lit()), null),
                        stmt('A', binop('+', var('A'), lit('x', 'y')), cond(lit('x'), binop('+', var('A'), var('B')))),
                        stmt('A', binop('+', var('A'), lit('x')), cond(binop('+', lit('x'), lit('y')), var('A'))),
                        stmt('A', binop('-', var('A'), lit('a', 'b', 'c')), null)));

        legal("""
                        B   := X
                           C := {a}
                        D := {x, y, z}
                        A   := B | A subset A
                        A := A  - { x, y}  | {x} & A subset {x, y} - {y}
                         A   := A + {x}
                        A <- x
                        A   <- x, y, z, a
                        A <- x |    x   in C
                        A   -> x | y in C  +    { x}""",
                prog(stmt('B', var('X'), null),
                        stmt('C', lit('a'), null),
                        stmt('D', lit('x', 'y', 'z'), null),
                        stmt('A', var('B'), cond(var('A'), var('A'))),
                        stmt('A', binop('-', var('A'), lit('x', 'y')), cond(binop('&', lit('x'), var('A')), binop('-', lit('x', 'y'), lit('y')))),
                        stmt('A', binop('+', var('A'), lit('x')), null),
                        stmt('A', binop('+', var('A'), lit('x')), null),
                        stmt('A', binop('+', var('A'), lit('a', 'x', 'y', 'z')), null),
                        stmt('A', binop('+', var('A'), lit('x')), cond(lit('x'), var('C'))),
                        stmt('A', binop('-', var('A'), lit('x')), cond(lit('y'), binop('+', var('C'), lit('x'))))));
    }

    private void legal(String input, HulkNode expectedAst) {
        HulkNode actualAst = HulkAst.makeHulkAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            HulkAst.makeHulkAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
