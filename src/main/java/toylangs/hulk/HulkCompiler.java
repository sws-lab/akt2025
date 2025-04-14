package toylangs.hulk;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaUtils;
import toylangs.hulk.ast.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class HulkCompiler {

    private static final List<Character> SET_VARIABLES = Arrays.asList('X', 'A', 'B', 'C', 'D', 'G', 'H', 'V'); // kasuta SET_VARIABLES.indexOf

    // kasuta hulga literaali teisendamiseks arvuks (bitset)
    private static int set2int(Set<Character> set) {
        List<Character> ELEM_VARIABLES = Arrays.asList('x', 'y', 'z', 'a', 'b', 'c', 'u', 'v');
        int result = 0;
        if (set != null) {
            for (int i = 0; i < ELEM_VARIABLES.size(); i++) {
                result |= CMaUtils.bool2int(set.contains(ELEM_VARIABLES.get(i))) << i;
            }
        }
        return result;
    }

    public static CMaProgram compile(HulkNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
