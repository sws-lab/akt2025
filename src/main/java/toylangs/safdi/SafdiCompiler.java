package toylangs.safdi;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.safdi.ast.*;

import java.util.List;

public class SafdiCompiler {

    public static CMaProgram compile(SafdiNode node, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
