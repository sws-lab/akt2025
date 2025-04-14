package week7.demos;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static week7.demos.ArithAst.eval;
import static week7.demos.ArithAst.makeAst;

public class ArithAstTest {

    @Test
    public void testEvaluate()  {
        assertEquals(12, eval("5 + 10 - 3"));
        assertEquals(2, eval("5 - 2 - 1"));
        assertEquals(13, eval("5 + 2 *4"));
    }

    @Test
    public void testAst() {
        assertEquals("-(+(5,10),3)", makeAst("5 + 10 - 3").toString());
        assertEquals("-(-(5,2),1)", makeAst("5 - 2 - 1").toString());
        assertEquals("+(5,*(2,4))", makeAst("5 + 2 *4").toString());
    }
}
