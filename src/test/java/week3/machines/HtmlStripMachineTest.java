package week3.machines;

import org.junit.Assert;
import org.junit.Test;

public class HtmlStripMachineTest {

    private static String cleanUp(String s) {
        StringBuilder sb = new StringBuilder();
        HtmlStripMachine machine = new HtmlStripMachine();
        for (char c : s.toCharArray()) sb.append(machine.process(c));
        return sb.toString();
    }

    @Test
    public void testRemoveHtmlMarkup() {
        check("foo", "<b>foo</b>");
    }

    @Test
    public void testRemoveHtmlMarkupWithGT() {
        check("f>5", "<b>f>5</b>");
    }

    @Test
    public void testRemoveHtmlMarkupWithQuotes() {
        check("foo", "<a href='>'>foo</a>");
    }

    @Test
    public void testRemoveHtmlMarkupWithQuotesAndGe() {
        check("f>5", "<a href='>'>f>5</a>");
    }

    @Test
    public void testRemoveHtmlMarkupWithJustQuotes() {
        check("'foo'", "'<b>foo</b>'");
        check("'foo'", "'foo'");
        check("''", "''");
    }

    @Test
    public void testRemoveHtmlMarkupWithDoubleQuotes() {
        check("foo", "<a href=\">\">foo</a>");
        check("\"foo\"", "\"<b>foo</b>\"");
    }

    @Test
    public void testRemoveHtmlMarkupWithMixedQuotes() {
        check("foo", "<a href=\"'kala'>'ma<ja'\">foo</a>");
        check("\"foo\"", "\"<b name='ka>la\"'>foo</b>\"");
    }


    public void check(String expected, String input) {
        Assert.assertEquals(input, expected, cleanUp(input));
    }
}
