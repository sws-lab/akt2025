package toylangs.dialoog;

import cma.CMaLabel;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.dialoog.ast.*;

import java.util.ArrayList;
import java.util.List;

import static cma.CMaUtils.bool2int;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;

public class DialoogCompiler {

    public static CMaProgram compile(DialoogNode prog) {
        CMaProgramWriter pw = new CMaProgramWriter();

        new DialoogAstVisitor<Void>() {
            private final List<String> vars = new ArrayList<>();

            @Override
            protected Void visit(DialoogLitInt num) {
                pw.visit(LOADC, num.getValue());
                return null;
            }

            @Override
            protected Void visit(DialoogLitBool tv) {
                pw.visit(LOADC, bool2int(tv.getValue()));
                return null;
            }

            @Override
            protected Void visit(DialoogVar var) {
                pw.visit(LOADA, vars.indexOf(var.getName()));
                return null;
            }

            @Override
            protected Void visit(DialoogUnary unary) {
                visit(unary.getExpr());
                pw.visit(unary.getOp().toCMa());
                return null;
            }

            @Override
            protected Void visit(DialoogBinary binary) {
                visit(binary.getLeftExpr());
                visit(binary.getRightExpr());
                pw.visit(binary.getOp().toCMa());
                return null;
            }

            @Override
            protected Void visit(DialoogTernary ifte) {
                CMaLabel _false = new CMaLabel();
                CMaLabel _done = new CMaLabel();

                visit(ifte.getGuardExpr());
                pw.visit(JUMPZ, _false);

                visit(ifte.getTrueExpr());
                pw.visit(JUMP, _done);

                pw.visit(_false);
                visit(ifte.getFalseExpr());

                pw.visit(_done);
                return null;
            }

            @Override
            protected Void visit(DialoogDecl decl) {
                vars.add(decl.getName());
                return null;
            }
        }.visit(prog);

        return pw.toProgram();
    }
}
