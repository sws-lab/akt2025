package week6.kalaparser;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;

import static org.junit.Assert.*;
import static week6.kalaparser.KalaNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KalaParserTest {

    private final HashMap<String, Integer> env = new HashMap<>();

    @Before
    public void setUp() {
        env.put("kala", 2);
        env.put("koer", -5);
        env.put("x", 3);
        env.put("y", 7);
        env.put("z", -9);
    }

    @Test
    public void test01_AST() {
        checkAst(mkIdent("kala"), 2);
        KalaNode node = mkList(mkIdent("x"), mkNull(), mkIdent("y"));
        checkAst(node, 10);
        checkAst(mkList(node, mkIdent("z")), 1);
    }

    private void checkAst(KalaNode node, int expected) {
        assertEquals(node.toString(), expected, node.sum(env));
    }

    @Test
    public void test02_parse() {
        checkAccept("()", true);
        checkAccept("(test, kala, null)", true);
        checkAccept("(test, (null, (),  null), null)", true);
        checkAccept("test", false);
    }

    private void checkAccept(String input, boolean accept) {
        try {
            KalaParser.parse(input);
            assertTrue(input + " ei kuulu tegelikult keelde", accept);
        } catch (KalaParseException e) {
            assertFalse(input + " peaks kuuluma keelde", accept);
        }
    }

    @Test
    public void test03_sum() {
        checkEval("(kala, (x,y ) , null, (), (kala,()) )", 14);
    }

    private void checkEval(String input, int expected) {
        KalaNode node = KalaParser.parse(input);
        assertEquals(input, expected, node.sum(env));
    }


}
