package toylangs.dialoog;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.dialoog.ast.*;

public class DialoogCompiler {

    public static CMaProgram compile(DialoogNode prog) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
