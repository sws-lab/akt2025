package toylangs.hulk;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.hulk.ast.HulkProg;

import static org.junit.Assert.*;
import static toylangs.hulk.ast.HulkNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HulkMasterTest {

    @Test
    public void test01_trivialAnalysis() {
        // A := ({a}+{b})
        // B := {c}
        HulkProg prog1 = prog(
                stmt('A', binop('+', lit('a'), lit('b')), null),
                stmt('B', lit('c'), null));
        assertTrue(HulkMaster.isValidHulkNode(prog1));

        // A := {a}
        // B := C
        HulkProg prog2 = prog(
                stmt('A', lit('a'), null),
                stmt('B', var('C'), null));
        assertFalse(HulkMaster.isValidHulkNode(prog2));
    }

    @Test
    public void test02_varsAnalysis() {
        // A := {a}
        // B := (A+{b})
        HulkProg prog1 = prog(
                stmt('A', lit('a'), null),
                stmt('B', binop('+', var('A'), lit('b')), null));
        assertTrue(HulkMaster.isValidHulkNode(prog1));

        // A := (B&{a, b})
        // B := (A-{a})
        HulkProg prog2 = prog(
                stmt('A', binop('&', var('B'), lit('a', 'b')), null),
                stmt('B', binop('-', var('A'), lit('a')), null));
        assertFalse(HulkMaster.isValidHulkNode(prog2));

        // A := ({a, b}&{a})
        // B := ((A-B)+{c})
        HulkProg prog3 = prog(
                stmt('A', binop('&', lit('a','b'), lit('a')), null),
                stmt('B', binop('+', binop('-', var('A'), var('B')), lit('c')), null));
        assertFalse(HulkMaster.isValidHulkNode(prog3));
    }

    @Test
    public void test03_conditionAnalysis() {
        // A := A | {a} subset {b, c}
        HulkProg prog1 = prog(stmt('A', var('A'), cond(lit('a'), lit('b', 'c'))));
        assertFalse(HulkMaster.isValidHulkNode(prog1));

        // A := {b} | {a} subset {a, b, c}
        // B := (A-{a})
        HulkProg prog2 = prog(
                stmt('A', lit('b'), cond(lit('a'), lit('a', 'b', 'c'))),
                stmt('B', binop('-', var('A'), lit('a')), null));
        assertFalse(HulkMaster.isValidHulkNode(prog2));

        // A := ({a}+{b}) | {a} subset {a, b, c}
        HulkProg prog3 = prog(stmt('A', binop('+', lit('a'), lit('b')),
                cond(lit('a'), lit('a', 'b', 'c'))));
        assertTrue(HulkMaster.isValidHulkNode(prog3));

        // A := {a, b, c}
        // B := (A+{a})
        HulkProg prog4 = prog(
                stmt('A', lit('a', 'b', 'c'), null),
                stmt('B', binop('+', var('A'), lit('a')), null));
        assertTrue(HulkMaster.isValidHulkNode(prog4));
    }

    @Test
    public void test04_mixedAnalysis() {
        // A := {d} | {a} subset {a, b}
        // B := ({b}&{c})
        // A := (B-{a})
        // C := ((A&B)-{c}) | A subset (A&B)
        // B := {a, b} | {a} subset {a, b}
        // A := (A-B)
        HulkProg prog1 = prog(
                stmt('A', lit('d'), cond(lit('a'), lit('a', 'b'))),
                stmt('B', binop('&', lit('b'), lit('c')), null),
                stmt('A', binop('-', var('B'), lit('a')), null),
                stmt('C', binop('-', binop('&', var('A'), var('B')), lit('c')), cond(var('A'), binop('&', var('A'), var('B')))),
                stmt('B', lit('a', 'b'), cond(lit('a'), lit('a', 'b'))),
                stmt('A', binop('-', var('A'), var('B')), null));
        assertTrue(HulkMaster.isValidHulkNode(prog1));

        // A := {a} | {b} subset {a, b}
        // B := {b}
        // A := B | {a} subset {a, b}
        // C := (B&{c})
        // C := ((B&C)-{a}) | B subset A
        HulkProg prog2 = prog(
                stmt('A', lit('a'), cond(lit('b'), lit('a', 'b'))),
                stmt('B', lit('b'), null),
                stmt('A', var('B'), cond(lit('a'), lit('a', 'b'))),
                stmt('C', binop('&', var('B'), lit('c')), null),
                stmt('C', binop('-', binop('&', var('B'), var('C')), lit('a')), cond(var('B'), var('A'))));
        assertFalse(HulkMaster.isValidHulkNode(prog2));
    }

    @Test
    public void test05_processTrivial() {
        // A := (B+C) | B subset {}
        // B := {}
        HulkProg prog = prog(
                stmt('A', binop('+', var('B'), var('C')), cond(var('B'), lit())),
                stmt('B', lit(), null));

        assertEquals(prog, HulkMaster.processEmptyLiterals(prog));
    }

    @Test
    public void test06_processPlus() {
        // A := ((A+B)+{})
        // B := {b} | {b} subset ({}+{})
        HulkProg prog2 = prog(
                stmt('A', binop('+', binop('+', var('A'), var('B')), lit()), null),
                stmt('B', lit('b'), cond(lit('b'), binop('+', lit(), lit()))));

        HulkProg prog2expected = prog(
                stmt('A', binop('+', var('A'), var('B')), null),
                stmt('B', lit('b'), cond(lit('b'), lit())));

        assertEquals(prog2expected, HulkMaster.processEmptyLiterals(prog2));


        // A := (({}+{})+{})
        HulkProg prog3 = prog(
                stmt('A', binop('+', binop('+', lit(), lit()), lit()), null));

        HulkProg prog3expected = prog(
                stmt('A', lit(), null));

        assertEquals(prog3expected, HulkMaster.processEmptyLiterals(prog3));


        // A := (((A&B)+{})-C)
        // B := ({}+{}) | {a} subset (({a, b}-C)+{})
        HulkProg prog4 = prog(
                stmt('A', binop('-', binop('+', binop('&', var('A'), var('B')), lit()), var('C')), null),
                stmt('B', binop('+', lit(), lit()), cond(lit('a'), binop('+', binop('-', lit('a', 'b'), var('C')), lit()))));

        HulkProg prog4expected = prog(
                stmt('A', binop('-', binop('&', var('A'), var('B')), var('C')), null), stmt('B', lit(), cond(lit('a'), binop('-', lit('a', 'b'), var('C')))));
        assertEquals(prog4expected, HulkMaster.processEmptyLiterals(prog4));
    }

    @Test
    public void test07_processIntersection() {
        // A := ({}&B) | (({a}-B)+C) subset ({a}&{})
        HulkProg
                prog1 = prog(stmt('A', binop('&', lit(), var('B')),
                cond(binop('+', binop('-', lit('a'), var('B')), var('C')), binop('&', lit('a'), lit()))));
        
        HulkProg prog1expected = prog(stmt('A', lit(),
                cond(binop('+', binop('-', lit('a'), var('B')), var('C')), lit())));
        
        assertEquals(prog1expected, HulkMaster.processEmptyLiterals(prog1));


        // A := (((A+B)-C)&{}) | {a} subset ((B&C)&{a, b})
        HulkProg prog2 = prog(
                stmt('A', binop('&', binop('-', binop('+', var('A'), var('B')), var('C')), lit()),
                cond(lit('a'), binop('&', binop('&', var('B'), var('C')), lit('a', 'b')))));

        HulkProg prog2expected = prog(
                stmt('A', lit(), cond(lit('a'), binop('&', binop('&', var('B'), var('C')), lit('a', 'b')))));

        assertEquals(prog2expected, HulkMaster.processEmptyLiterals(prog2));
    }

    @Test
    public void test08_processMinus() {
        // A := (B-{}) | (({a}-{})+C) subset ((A&B)-{})
        HulkProg prog1 = prog(
                stmt('A', binop('-', var('B'), lit()),
                cond(binop('+', binop('-', lit('a'), lit()), var('C')),
                        binop('-', binop('&', var('A'), var('B')), lit()))));

        HulkProg prog1expected = prog(
                stmt('A', var('B'),
                cond(binop('+', lit('a'), var('C')),
                        binop('&', var('A'), var('B')))));
        assertEquals(prog1expected, HulkMaster.processEmptyLiterals(prog1));


        // A := (({}-{a})-{}) | {a, b} subset ({}-{})
        HulkProg prog2 = prog(
                stmt('A', binop('-', binop('-', lit(), lit('a')), lit()),
                        cond(lit('a', 'b'), binop('-', lit(), lit()))));

        HulkProg prog2expected = prog(stmt('A', lit(), cond(lit('a', 'b'), lit())));
        assertEquals(prog2expected, HulkMaster.processEmptyLiterals(prog2));
    }

    @Test
    public void test09_processCondition() {
        // A := ({}&{a}) | ((A&B)&{}) subset {}
        // B := {} | ({}&{a}) subset (({}&{})&{})
        HulkProg prog1 = prog(
                stmt('A', binop('&', lit(), lit('a')), cond(binop('&', binop('&', var('A'), var('B')), lit()), lit())),
                stmt('B', lit(), cond(binop('&', lit(), lit('a')), binop('&', binop('&', lit(), lit()), lit()))));
        HulkProg prog1expected = prog(
                stmt('A', lit(), null),
                stmt('B', lit(), null));
        assertEquals(prog1expected, HulkMaster.processEmptyLiterals(prog1));

        // A := (((A+B)-{})-{}) | {} subset (((A+{a})-{})&B)
        // B := {} | (({}-{})-{}) subset ({}-{a, b, c})
        HulkProg prog2 = prog(
                stmt('A', binop('-', binop('-', binop('+', var('A'), var('B')), lit()), lit()),
                        cond(lit(), binop('&', binop('-', binop('+', var('A'), lit('a')), lit()), var('B')))),
                stmt('B', lit(),
                        cond(binop('-', binop('-', lit(), lit()), lit()), binop('-', lit(), lit('a', 'b', 'c')))));

        HulkProg prog2expected = prog(
                stmt('A', binop('+', var('A'), var('B')), null),
                stmt('B', lit(), null));
        assertEquals(prog2expected, HulkMaster.processEmptyLiterals(prog2));
    }

    @Test
    public void test10_processMixed() {
        // A := (((A+B)-{})&({a}+{})) | (({}&A)-C) subset {}
        // B := ((A-{a})&(({}+B)&{}))
        // C := ((B+A)&{}) | (({a}+{})-({}&{})) subset ({a}+{b})
        HulkProg prog1 = prog(
                stmt('A', binop('&', binop('-', binop('+', var('A'), var('B')), lit()), binop('+', lit('a'), lit())), cond(binop('-', binop('&', lit(), var('A')), var('C')), lit())),
                stmt('B', binop('&', binop('-', var('A'), lit('a')), binop('&', binop('+', lit(), var('B')), lit())), null),
                stmt('C', binop('&', binop('+', var('B'), var('A')), lit()), cond(binop('-', binop('+', lit('a'), lit()), binop('&', lit(), lit())), binop('+', lit('a'), lit('b')))));

        HulkProg prog1expected = prog(
                stmt('A', binop('&', binop('+', var('A'), var('B')), lit('a')), null),
                stmt('B', lit(), null),
                stmt('C', lit(), cond(lit('a'), binop('+', lit('a'), lit('b')))));

        assertEquals(prog1expected, HulkMaster.processEmptyLiterals(prog1));


        // A := (A+({a}&({}-B)))
        // B := ((({a}+{})-(A&{}))+(C-({}-{a})))
        HulkProg prog2 = prog(
                stmt('A', binop('+', var('A'), binop('&', lit('a'), binop('-', lit(), var('B')))), null),
                stmt('B', binop('+', binop('-', binop('+', lit('a'), lit()), binop('&', var('A'), lit())), binop('-', var('C'), binop('-', lit(), lit('a')))), null));

        HulkProg prog2expected = prog(
                stmt('A', var('A'), null),
                stmt('B', binop('+', lit('a'), var('C')), null));

        assertEquals(prog2expected, HulkMaster.processEmptyLiterals(prog2));
    }
}
