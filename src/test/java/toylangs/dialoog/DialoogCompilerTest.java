package toylangs.dialoog;

import cma.CMaInterpreter;
import cma.CMaStack;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.dialoog.ast.DialoogNode;

import static org.junit.Assert.assertEquals;
import static toylangs.dialoog.ast.DialoogNode.*;
import static toylangs.dialoog.DialoogCompiler.compile;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DialoogCompilerTest {

    @Test
    public void test01_novars() {
        checkCompile(1, prog(decls(), il(1)));
        checkCompile(1, prog(decls(), bl(true)));
        
        checkCompile(3, prog(decls(), add(il(1), il(2))));
        checkCompile(1, prog(decls(), or(bl(false), bl(true))));
        checkCompile(1, prog(decls(), not(bl(false))));
        checkCompile(-1, prog(decls(), neg(il(1))));
    }

    @Test
    public void test02_vars() {
        checkCompile(2, prog(decls(iv("y")), var("y")), 2);
        checkCompile(0, prog(decls(bv("b")), var("b")), 0);
        
        
        checkCompile(5, prog(decls(iv("x")), add(var("x"), il(2))), 3);
        checkCompile(10, prog(decls(iv("x")), add(var("x"), il(2))), 8);
        checkCompile(0, prog(decls(bv("x")), or(bl(false), var("x"))),0);
        checkCompile(1, prog(decls(bv("x")), or(bl(false), var("x"))),1);

        checkCompile(4, prog(decls(iv("x"), iv("y"), iv("z")), add(var("x"), var("z"))), 1, 2, 3); // kasutatud muutujad pole juba algselt stacki peal õiges kohas
    }

    @Test
    public void test03_ops() {
        checkCompile(-1, prog(decls(iv("x")), neg(var("x"))), 1);
        checkCompile(7, prog(decls(iv("x"), iv("z")), add(var("x"), var("z"))), 1, 6);
        checkCompile(2, prog(decls(iv("x"), iv("y"), iv("z")), sub(div(var("z"), var("y")), var("x"))), 1, 2, 6);

        checkCompile(1, prog(decls(bv("a"), bv("b")), and(var("a"), not(or(var("b"), bl(false))))), 1, 0);
        checkCompile(0, prog(decls(bv("a"), bv("b")), and(var("a"), not(or(var("b"), bl(true))))), 1, 0);

        checkCompile(1, prog(decls(iv("y")), eq(var("y"), il(2))), 2);
        checkCompile(0, prog(decls(bv("b")), eq(var("b"), bl(true))), 0);
        checkCompile(0, prog(decls(bv("a"), iv("z")), eq(var("a"), eq(var("z"), il(5)))), 1, 6);
        checkCompile(1, prog(decls(bv("a"), iv("z")), eq(var("a"), eq(var("z"), il(6)))), 1, 6);
    }

    @Test
    public void test04_ifte() {
        checkCompile(1, prog(decls(iv("x"), iv("y")), ifte(bl(true), var("x"), var("y"))), 1, 2);
        checkCompile(2, prog(decls(iv("x"), iv("y")), ifte(bl(false), var("x"), var("y"))), 1, 2);
        checkCompile(2, prog(decls(iv("x"), iv("y"), iv("z")), ifte(eq(var("x"), var("z")), var("z"), var("y"))), 1, 2, 6);

        checkCompile(0, prog(decls(bv("a")), ifte(var("a"), bl(false), bl(true))), 1);
        checkCompile(1, prog(decls(bv("b")), ifte(var("b"), bl(false), bl(true))), 0);

        checkCompile(20, prog(decls(), ifte(bl(true), ifte(bl(false), il(10), il(20)), il(30))));
    }


    private void checkCompile(int expected, DialoogNode ast, int... stack) {
        CMaStack initialStack = new CMaStack(stack);
        CMaStack finalStack = CMaInterpreter.run(compile(ast), initialStack);
        assertEquals(expected, finalStack.peek());
    }
}
