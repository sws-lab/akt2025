package toylangs.bolog;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.bolog.ast.*;

import java.util.Arrays;
import java.util.List;

import static cma.CMaUtils.bool2int;
import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;

public class BologCompiler {

    private static final List<String> VARS = Arrays.asList("P", "Q", "R", "S", "T", "U", "V"); // kasuta VARS.indexOf

    public static CMaProgram compile(BologNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();
        new BologAstVisitor<Void>() {
            @Override
            protected Void visit(BologLit tv) {
                pw.visit(LOADC, bool2int(tv.isTrue()));
                return null;
            }

            @Override
            protected Void visit(BologVar var) {
                pw.visit(LOADA, VARS.indexOf(var.getName()));
                return null;
            }

            @Override
            protected Void visit(BologNand nand) {
                visit(nand.getLeftExpr());
                visit(nand.getRightExpr());
                pw.visit(AND);
                pw.visit(NOT);
                return null;
            }

            @Override
            protected Void visit(BologImp imp) {
                // Idee: (A -> B === !A || B); kõigepealt aga A = TRUE && A1 && A2 && ...
                pw.visit(LOADC, 1);
                for (BologNode assumption : imp.getAssumptions()) {
                    visit(assumption);
                    pw.visit(AND);
                }
                pw.visit(NOT);
                visit(imp.getConclusion());
                pw.visit(OR);
                return null;
            }
        }.visit(node);
        return pw.toProgram();
    }
}
