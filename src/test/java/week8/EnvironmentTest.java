package week8;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.JVM)
public class EnvironmentTest {

    private Environment<Integer> env;

    @Before
    public void setUp() {
        env = new Environment<>();
    }

    @Test
    public void test_undeclaredVariable() {
        assertNull(env.get("x"));
    }

    @Test
    public void test_unassignedVariable() {
        env.declare("x"); // var x
        assertNull(env.get("x"));
    }

    @Test
    public void test_assignedVariable() {
        env.declare("x");          // var x
        env.assign("x", 42); // x = 42
        assertEquals((Integer) 42, env.get("x"));
    }

    @Test
    public void test_declareAssignedVariable() {
        env.declareAssign("x", 42); // var x = 42
        assertEquals((Integer) 42, env.get("x"));
    }

    @Test
    public void test_twiceAssignedVariable() {
        env.declareAssign("x", 42); // var x = 42
        env.assign("x", 43);        // x = 43
        assertEquals((Integer) 43, env.get("x"));
    }

    @Test
    public void test_getOuterBlockVariable() {
        env.declareAssign("x", 42); // var x = 42
        env.enterBlock();                         // {
        assertEquals((Integer) 42, env.get("x"));
    }

    @Test
    public void test_assignOuterBlockVariable() {
        env.declareAssign("x", 42); // var x = 42
        env.enterBlock();                         // {
        env.assign("x", 43);        //   x = 43
        env.exitBlock();                          // }
        assertEquals((Integer) 43, env.get("x"));
    }

    @Test
    public void test_declareInnerBlockVariable() {
        env.enterBlock();                         // {
        env.declareAssign("x", 42); //   var x = 42
        env.exitBlock();                          // }
        assertNull(env.get("x"));
    }

    @Test
    public void test_redeclareOuterBlockVariable() {
        env.declareAssign("x", 42); // var x = 42
        env.enterBlock();                         // {
        env.declareAssign("x", 43); //   var x = 43
        assertEquals((Integer) 43, env.get("x"));
        env.exitBlock();                          // }
        assertEquals((Integer) 42, env.get("x"));
    }

    @Test
    public void test_getNestedOuterBlockVariable() {
        env.declareAssign("x", 42); // var x = 42
        env.enterBlock();                         // {
        env.enterBlock();                         //   {
        assertEquals((Integer) 42, env.get("x"));
    }
}
