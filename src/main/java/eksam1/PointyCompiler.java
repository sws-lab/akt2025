package eksam1;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import eksam1.ast.*;

import java.util.List;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.*;

public class PointyCompiler {

    public static CMaProgram compile(PointyNode node, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
