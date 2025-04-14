package toylangs.sholog;

import toylangs.sholog.ast.ShologNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static toylangs.sholog.ast.ShologNode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShologAstTest {

    @Test
    public void test01_literals() {
        legal("T", lit(true));
        legal("F", lit(false));
        legal(" T", lit(true));
        legal(" F  ", lit(false));

        illegal("1");
        illegal("0");
        illegal("TT");
        illegal("T F");
    }

    @Test
    public void test02_variables() {
        legal("a", var("a"));
        legal("t", var("t"));
        legal("f", var("f"));
        legal("foo", var("foo"));
        legal("  bar ", var("bar"));
        legal("true", var("true"));
        legal("ei", var("ei"));

        illegal("A");
        illegal("FOO");
        illegal("aT");
        illegal("Fb");
        illegal("a2");
        illegal("a b");
    }

    @Test
    public void test03_errors() {
        legal("E1", error(1));
        legal("E7", error(7));
        legal("E128", error(128));
        legal(" E256 ", error(256));
        legal("E007", error(7));

        illegal("E");
        illegal("E 1");
        illegal("E 128");
        illegal("E12 8");
        illegal("E+1");
        illegal("E-1");
        illegal("e1");
        illegal("err1");
        illegal("error1");
    }

    @Test
    public void test04_operators() {
        legal("x /\\ y", eand(var("x"), var("y")));
        legal("x \\/ y", eor(var("x"), var("y")));
        legal("x + y", xor(var("x"), var("y")));
        legal("x && y", land(var("x"), var("y")));
        legal("x || y", lor(var("x"), var("y")));

        legal("~x", xor(var("x"), lit(true)));

        illegal("x &&");
        illegal("|| y");
        illegal("x // y");
        illegal("x \\ y");
        illegal("x \\\\ y");
        illegal("x ++ y");
        illegal("x & & y");
        illegal("x & y");
        illegal("x | y");
        illegal("x JA y");
        illegal("x voi y");
        illegal("x \\ / y");
        illegal("x /\\ y z");
        illegal("x && || y");
        illegal("x~");
        illegal("~");
        illegal("||");
        illegal("-x");
        illegal("x - y");
    }

    @Test
    public void test05_parenthesis() {
        legal("(T)", lit(true));
        legal("(foo)", var("foo"));
        legal("(x && y)", land(var("x"), var("y")));
        legal("(((x && y)))", land(var("x"), var("y")));
        legal("x && (y)", land(var("x"), var("y")));

        illegal("(x && y");
        illegal("((x && y)))");
        illegal("x (&& y)");
        illegal("x &(& y)");
        illegal("()");
    }

    @Test
    public void test06_priority_associativity() {
        legal("x \\/ y /\\ z", eor(var("x"), eand(var("y"), var("z"))));
        legal("x || y /\\ z", lor(var("x"), eand(var("y"), var("z"))));
        legal("x \\/ y && z", eor(var("x"), land(var("y"), var("z"))));
        legal("x || y && z", lor(var("x"), land(var("y"), var("z"))));

        legal("~x /\\ y", eand(xor(var("x"), lit(true)), var("y")));
        legal("~x \\/ y", eor(xor(var("x"), lit(true)), var("y")));
        legal("~x && y", land(xor(var("x"), lit(true)), var("y")));
        legal("~x || y", lor(xor(var("x"), lit(true)), var("y")));

        legal("x /\\ y /\\ z", eand(eand(var("x"), var("y")), var("z")));
        legal("x \\/ y \\/ z", eor(eor(var("x"), var("y")), var("z")));
        legal("x && y && z", land(land(var("x"), var("y")), var("z")));
        legal("x || y || z", lor(lor(var("x"), var("y")), var("z")));

        legal("x /\\ y && z", land(eand(var("x"), var("y")), var("z")));
        legal("x && y /\\ z", eand(land(var("x"), var("y")), var("z")));
        legal("x \\/ y || z", lor(eor(var("x"), var("y")), var("z")));
        legal("x || y \\/ z", eor(lor(var("x"), var("y")), var("z")));

        legal("~~x", xor(xor(var("x"), lit(true)), lit(true)));
        legal("~ ~ x", xor(xor(var("x"), lit(true)), lit(true)));
    }

    @Test
    public void test07_priority_associativity_xor() {
        fail("See test avalikustatakse pärast eksamit! Xor prioriteet ja assotsiatiivsus!");
    }

    @Test
    public void test08_combinations() {
        fail("See test avalikustatakse pärast eksamit! Lihtsalt natuke keerulisemad avaldised!");
    }


    private void legal(String input, ShologNode expectedAst) {
        ShologNode actualAst = ShologAst.makeShologAst(input);
        assertEquals(expectedAst, actualAst);
    }

    private void illegal(String input) {
        try {
            ShologAst.makeShologAst(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }
}
