package toylangs.vhile;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.vhile.ast.*;

import java.util.List;

public class VhileCompiler {

    public static CMaProgram compile(VhileStmt stmt, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
