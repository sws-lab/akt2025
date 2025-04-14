package toylangs.bolog;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import toylangs.bolog.ast.BologImp;
import toylangs.bolog.ast.BologNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static toylangs.bolog.ast.BologNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BologMasterTest {

    @Test
    public void test01_basic() {
        checkModel(setOf("X"), imp(var("X")));
        checkModel(setOf("X", "X"), imp(var("X")), imp(var("X"), var("Y")));
        checkModel(setOf("X", "Y"), imp(var("X")), imp(var("Y"), var("X")));
    }

    @Test
    public void test02_more() {
        checkModel(setOf("X"), imp(var("X"), tv(true)));
        checkModel(setOf(), imp(var("X"), var("X")));
        checkModel(setOf("X", "Y", "Z"),
                imp(var("X"), var("Y")),
                imp(var("Y")), imp(var("Z"), var("X"), var("Y")),
                imp(var("Z"), var("P")));
        checkModel(setOf("Z"),
                imp(var("X"), var("Y")),
                imp(var("Y"), var("X")),
                imp(var("Z"), var("X"), var("Y")),
                imp(var("Z")));
    }

    @Test
    public void test03_sanity() {
        Set<BologImp> imps = new HashSet<>();
        for (char i = 'A'; i < 'Y'; i++) {
            imps.add((BologImp) imp(var(Character.toString(i)), var(Character.toString(i))));
        }
        assertEquals(setOf(), BologMaster.leastModel(imps));

        Set<String> chars = new HashSet<>();
        for (char i = 'A'; i < 'Y'; i++) {
            imps.add((BologImp) imp(var(Character.toString(i))));
            chars.add(Character.toString(i));
        }
        assertEquals(chars, BologMaster.leastModel(imps));
    }


    private static void checkModel(Set<String> model, BologNode... imp) {
        Set<BologNode> nodes = new HashSet<>(Arrays.asList(imp));
        Set<BologImp> nodecast = new HashSet<>();
        for (BologNode node : nodes) nodecast.add((BologImp) node);
        assertEquals(model, BologMaster.leastModel(nodecast));
    }

    @SafeVarargs
    private static <T> Set<T> setOf(T... elems) {
        return new HashSet<>(Arrays.asList(elems));
    }
}
