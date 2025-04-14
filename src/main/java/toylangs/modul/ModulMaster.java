package toylangs.modul;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.modul.ast.*;

import java.util.List;
import java.util.Map;

public class ModulMaster {

    public static int eval(ModulProg prog, Map<String, Integer> env) {
        throw new UnsupportedOperationException();
    }

    public static CMaProgram compile(ModulProg prog, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
