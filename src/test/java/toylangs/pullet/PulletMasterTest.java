package toylangs.pullet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static toylangs.pullet.ast.PulletNode.*;
import static toylangs.pullet.PulletMaster.isLive;
import static toylangs.pullet.PulletMaster.optimize;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PulletMasterTest {

    @Test
    public void test01_sem1() {
        assertTrue(isLive("x", var("x")));
        assertFalse(isLive("x", num(0)));
        assertFalse(isLive("x", var("y")));
        assertTrue(isLive("kala", diff(var("x"), var("kala"))));
        assertTrue(isLive("kala", diff(var("kala"), var("y"))));
        assertFalse(isLive("kala", diff(var("x"), var("y"))));
    }

    @Test
    public void test02_sem2() {
        assertTrue(isLive("x", let("y", num(10), var("x"))));
        assertTrue(isLive("x", let("y", var("x"), var("y"))));
        assertFalse(isLive("z", let("y", num(10), var("x"))));
        assertFalse(isLive("z", let("y", var("x"), var("y"))));
    }


    @Test
    public void test03_sem3() {
        assertTrue(isLive("x", let("y", num(10), var("x"))));
        assertTrue(isLive("x", let("y", var("x"), var("y"))));
        assertTrue(isLive("x", let("y", diff(var("x"),num(1)),var("y"))));
        assertFalse(isLive("z", let("y", num(10), var("x"))));
        assertFalse(isLive("y", let("y", var("x"), var("y"))));
        assertFalse(isLive("z", let("x",num(5),let("y",var("y"),let("z",var("x"),var("z"))))));
    }


    @Test
    public void test04_opt1() {
        assertEquals(var("y"), optimize(let("x", num(5), var("y"))));
        assertEquals(let("x", num(5), var("x")), optimize(let("x", num(5), var("x"))));
    }

    @Test
    public void test05_opt2() {
        assertEquals(let("x",num(5),let("z",var("x"),var("z"))),
                optimize(let("x",num(5),let("y",num(10),let("z",var("x"),var("z"))))));
        assertEquals(let("a", var("y"), var("a")),
                optimize(let("a", let("x", num(5), var("y")), var("a"))));
    }

    @Test
    public void test06_opt3() {
        assertEquals(var("z"), optimize(let("x",num(10),let("y",var("x"),var("z")))));
        assertEquals(var("y"), optimize(let("a", let("a", num(10), var("a")), var("y"))));
        assertEquals(let("a", var("a"), diff(var("b"), var("a"))),
                optimize(let("a", let("b", var("x"), var("a")), diff(var("b"), var("a")))));
    }
}
