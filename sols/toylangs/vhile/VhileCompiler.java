package toylangs.vhile;

import cma.CMaLabel;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.vhile.ast.*;

import java.util.LinkedList;
import java.util.List;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;

public class VhileCompiler {

    public static CMaProgram compile(VhileStmt stmt, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        new VhileAstVisitor<Void>() {
            private final LinkedList<CMaLabel> loopEnds = new LinkedList<>();

            @Override
            protected Void visit(VhileNum num) {
                pw.visit(LOADC, num.getValue());
                return null;
            }

            @Override
            protected Void visit(VhileVar var) {
                int variableIndex = variables.indexOf(var.getName());
                if (variableIndex < 0)
                    throw new VhileException();
                else {
                    pw.visit(LOADA, variableIndex);
                    return null;
                }
            }

            @Override
            protected Void visit(VhileBinOp binOp) {
                visit(binOp.getLeft());
                visit(binOp.getRight());
                switch (binOp.getOp()) {
                    case Add -> pw.visit(ADD);
                    case Mul -> pw.visit(MUL);
                    case Eq -> pw.visit(EQ);
                    case Neq -> pw.visit(NEQ);
                    default -> throw new UnsupportedOperationException("unknown op");
                }
                return null;
            }

            @Override
            protected Void visit(VhileAssign assign) {
                int variableIndex = variables.indexOf(assign.getName());
                if (variableIndex < 0)
                    throw new VhileException();
                else {
                    visit(assign.getExpr());
                    pw.visit(STOREA, variableIndex);
                    pw.visit(POP);
                    return null;
                }
            }

            @Override
            protected Void visit(VhileBlock block) {
                for (VhileStmt stmt : block.getStmts()) {
                    visit(stmt);
                }
                return null;
            }

            @Override
            protected Void visit(VhileLoop loop) {
                CMaLabel _while = new CMaLabel();
                CMaLabel _end = new CMaLabel();
                loopEnds.addFirst(_end);

                pw.visit(_while);
                visit(loop.getCondition());
                pw.visit(JUMPZ, _end);

                visit(loop.getBody());
                pw.visit(JUMP, _while);

                pw.visit(_end);
                loopEnds.removeFirst();
                return null;
            }

            @Override
            protected Void visit(VhileEscape escape) {
                int level = escape.getLevel() - 1;
                if (level < loopEnds.size())
                    pw.visit(JUMP, loopEnds.get(level));
                else
                    pw.visit(HALT); // liiga suur escape level, lõpetab kompileerimise ja kogu programmi töö erindita
                return null;
            }
        }.visit(stmt);

        return pw.toProgram();
    }
}
