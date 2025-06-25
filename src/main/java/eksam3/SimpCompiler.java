package eksam3;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import eksam3.ast.*;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.*;

public class SimpCompiler {

    public static CMaProgram compile(SimpProg prog) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }

}
