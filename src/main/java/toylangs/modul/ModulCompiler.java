package toylangs.modul;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.modul.ast.*;

import java.util.List;

public class ModulCompiler {

    public static CMaProgram compile(ModulProg prog, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
