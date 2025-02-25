package week3.mealy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MachineDefsTest {

    @Test
    public void testRemoveHtmlMarkup1() {
        MealyMachine m = MachineDefs.getHtmlMachine();
        check(m, "foo", "<b>foo</b>");
        check(m, "f>5", "<b>f>5</b>");
        check(m, "foo", "<a href='>'>foo</a>");
        check(m, "f>5", "<a href='>'>f>5</a>");
        check(m, "'foo'", "'<b>foo</b>'");
        check(m, "'foo'", "'foo'");
        check(m, "''", "''");
    }

    @Test
    public void testRemoveHtmlMarkup2() {
        MealyMachine m = MachineDefs.getHtmlMachine();
        check(m, "foo", "<a href=\">\">foo</a>");
        check(m, "\"foo\"", "\"<b>foo</b>\"");
        check(m, "foo", "<a href=\"'kala'>'ma<ja'\">foo</a>");
        check(m, "\"foo\"", "\"<b name='ka>la\"'>foo</b>\"");
    }

    @Test
    public void testXoxo() {
        MealyMachine m = MachineDefs.getXoxoMachine();
        check(m, "x", "x");
        check(m, "xoxo", "xxxx");
        check(m, "kala", "kala");
        check(m, "xeroo", "xerox");
    }

    @Test
    public void testDepth() {
        MealyMachine m = MachineDefs.getDepthMachine();
        check(m, "0", "$");
        check(m, "1", "($)");
        check(m, "5", "((((($");
        check(m, "3", "((($((");
        check(m, "2", "(()($((");
        check(m, "0", "(((((())))))$");
    }

    @Test
    public void testBinaryMachine3() {
        MealyMachine m = MachineDefs.getBinaryMachine(3);
        check(m, "0", "0$");    // 0
        check(m, "1", "1$");    // 1
        check(m, "2", "101$");  // 5
        check(m, "0", "110$");  // 6
        check(m, "0", "1111$"); // 15
        check(m, "2", "101101101$");
        check(m, "0", "100000010$");
    }

    @Test
    public void testBinaryMachine5() {
        MealyMachine m = MachineDefs.getBinaryMachine(5);
        check(m, "0", "0$");    // 0
        check(m, "1", "1$");    // 1
        check(m, "0", "101$");  // 5
        check(m, "1", "110$");  // 6
        check(m, "0", "1111$"); // 15
        check(m, "0", "101101101$");
        check(m, "4", "111101110$");
    }

    @Test
    public void testTokenizer() {
        MealyMachine m = MachineDefs.getTokenizer('|');
        check(m, "kala|+|maja", "kala+maja");
        check(m, "1|+|+|5", "1++5");
        m = MachineDefs.getTokenizer(',');
        check(m, "1,+,2", "1+2");
    }

    @Test
    public void testFormatter1() {
        MealyMachine formatter = MachineDefs.getFormatter();
        check(formatter, "One Two Three Four",  "One Two  Three   Four");
    }

    @Test
    public void testFormatter2() {
        MealyMachine formatter = MachineDefs.getFormatter();
        check(formatter, "Tere, Maailm!", "Tere   ,Maailm!");
    }

    @Test
    public void testFormatter3() {
        MealyMachine formatter = MachineDefs.getFormatter();
        check(formatter, """
                        This text (all of it) has occasional lapses... in
                        punctuation (sometimes, pretty bad; sometimes, not so).

                        (Ha!) Is this: fun!?! Or what?""",
                """
                        This     text (all of it   )has occasional lapses .. .in
                          punctuation( sometimes,pretty bad ; sometimes ,not so).

                        ( Ha ! )Is this  :fun ! ? !  Or   what  ?""");
    }


    public void check(MealyMachine machine, String expected, String input) {
        assertEquals(input, expected, machine.run(input));
    }
}
