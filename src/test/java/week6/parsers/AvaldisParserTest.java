package week6.parsers;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week6.parsers.astdemo.AvaldisAst;
import week6.parsers.astdemo.AvaldisParser;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AvaldisParserTest {

    private final TestParser<AvaldisParser> tutParser = new TestParser<>(AvaldisParser.class);
    private final TestParser<AvaldisAst> tutAst = new TestParser<>(AvaldisAst.class);

    @Test
    public void test01_Recognizer() {
        tutParser.accepts("x*x+x");
        tutParser.rejects("x+)");
    }

    @Test
    public void test02_Parse() {
        tutParser.accepts("x*x", "S(T(F(x),Q(*,F(x),Q(ε))),R(ε))");
    }

    @Test
    public void test03_Expected() {
        tutParser.rejects("x+-x", 2, "(x", "+*$)");
    }

    @Test
    public void test04_Ast1() {
        tutAst.accepts("x*x", "*(x,x)");
    }

    @Test
    public void test05_Ast2() {
        tutAst.accepts("x+x*x", "+(x,*(x,x))");
        tutAst.accepts("(x+x)*x", "*(+(x,x),x)");
        tutAst.accepts("x*x+x", "+(*(x,x),x)");
        tutAst.accepts("x*(x+x)", "*(x,+(x,x))");
    }

    @Test
    public void test06_Ast3() {
        tutAst.accepts("x+x+x", "+(+(x,x),x)");
        tutAst.accepts("x*x*x", "*(*(x,x),x)");
    }

}
