package toylangs.parm;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.parm.ast.*;

import java.util.Arrays;
import java.util.List;

import static cma.instruction.CMaBasicInstruction.Code.ADD;
import static cma.instruction.CMaBasicInstruction.Code.POP;
import static cma.instruction.CMaIntInstruction.Code.*;
import static toylangs.parm.ast.ParmNode.lit;
import static toylangs.parm.ast.ParmNode.plus;

public class ParmCompiler {

    public static final List<String> VARS = Arrays.asList("X", "Y", "Z", "A", "B", "C", "D"); // kasuta VARS.indexOf

    public static CMaProgram compile(ParmNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();
        new ParmCompilerVisitor(pw).visit(node);
        return pw.toProgram();
    }

    // See on meil eraldi klass, et meistriosas saaks taas kasutada.
    public static class ParmCompilerVisitor extends ParmAstVisitor<Void> {

        private final CMaProgramWriter pw;

        public ParmCompilerVisitor(CMaProgramWriter pw) {
            this.pw = pw;
        }


        @Override
        protected Void visit(ParmLit lit) {
            pw.visit(LOADC, lit.getValue());
            return null;
        }

        @Override
        protected Void visit(ParmVar var) {
            pw.visit(LOADA, VARS.indexOf(var.getName()));
            return null;
        }

        @Override
        protected Void visit(ParmPlus plus) {
            visit(plus.getLeftExpr());
            visit(plus.getRightExpr());
            pw.visit(ADD);
            return null;
        }

        @Override
        protected Void visit(ParmUpdate up) {
            visit(up.getValue());
            pw.visit(STOREA, VARS.indexOf(up.getVariableName()));
            // Siin ei ole POP!
            return null;
        }

        @Override
        protected Void visit(ParmCompose comp) {
            visit(comp.getFst());
            pw.visit(POP);
            visit(comp.getSnd());
            return null;
        }
    }

    public static void main(String[] args) {
        CMaProgram cMaProgram = compile(plus(lit(10), lit(12)));
        System.out.println(cMaProgram.toString());
    }
}
