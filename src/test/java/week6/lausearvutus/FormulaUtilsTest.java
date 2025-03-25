package week6.lausearvutus;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static week6.lausearvutus.FormulaUtils.moveNegations;
import static week6.lausearvutus.FormulaUtils.parseFormula;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormulaUtilsTest {

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    // Seda saab põhjalikumalt moodle'is testida, kus võrreldakse meie lahendusega.
    // Siin on ainult mõned näited, et oleks täpsemalt aru saada, mida teha tuleb.

    @Test
    public void test01_parse() {
        assertEquals("((F&G)&H)", parseFormula("F&G&H").toString());
    }


    @Test(expected = RuntimeException.class)
    public void test02_noparse() {
        parseFormula("A→B→C");
    }

    @Test
    public void test03_equal() {
        assertEquals(parseFormula("F&G&H"), parseFormula("(F&G)&H"));
    }


    @Test
    public void test04_eval() {
        Map<Character, Boolean> env = new HashMap<>();
        env.put('F', true);
        env.put('G', false);
        env.put('H', true);
        assertFalse(parseFormula("F&G&H").evaluate(env));
        assertTrue(parseFormula("F&¬G&H").evaluate(env));
    }

    @Test
    public void test05_moveneg() {
        assertEquals("(¬A&(B∨¬C))", moveNegations(parseFormula("¬(A∨¬B&C)")).toString());
    }



}
