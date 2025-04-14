package toylangs.bolog;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.bolog.ast.*;

import java.util.Arrays;
import java.util.List;

public class BologCompiler {

    private static final List<String> VARS = Arrays.asList("P", "Q", "R", "S", "T", "U", "V"); // kasuta VARS.indexOf

    public static CMaProgram compile(BologNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();
        return pw.toProgram();
    }
}
