package week3.machines;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MiniAktkEvalTest {

    private Map<Character, Integer> environment;

    @Before
    public void setUp() {
        environment = new HashMap<>();
        environment.put('x', 5);
        environment.put('y', -2);
        environment.put('z', 10);
    }


    @Test
    public void testTokenize() {
        checkTokenize("2+2",      "2", "+", "2");
        checkTokenize("2 -  8",   "2", "-", "8");
        checkTokenize("2 -- 8",   "2", "-", "-", "8");
        checkTokenize("2 - - 8",   "2", "-", "-", "8");
        checkTokenize("2-2+8",    "2", "-", "2", "+", "8");
    }

    private void checkTokenize(String input, String... strings) {
        Assert.assertEquals(Arrays.asList(strings), MiniAktkMachines.tokenize(input));
    }


    @Test
    public void testCompute() {
        checkCompute(4, "2+2");
        checkCompute(0, "2+2-4");
        checkCompute(8, "2+2--4");
    }

    @Test
    public void testComputeWithVars() {
        checkCompute(3, "x+y");
        checkCompute(10, "z");
    }

    @Test
    public void testComputeLong1() {
        checkCompute(12, "5 + - - + - + + - 7");
        checkCompute(-2, "5 + - - - - + + - 7");
    }

    @Test
    public void testComputeLong2() {
        checkCompute(10, "2-2+10");
    }

    private void checkCompute(int result, String input) {
        assertEquals(result, MiniAktkMachines.compute(MiniAktkMachines.tokenize(input), environment));
    }
}
