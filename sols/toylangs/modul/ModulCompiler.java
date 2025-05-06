package toylangs.modul;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.modul.ast.*;

import java.util.List;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;

public class ModulCompiler {

    public static CMaProgram compile(ModulProg prog, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        int m = prog.getModulus();

        new Visitor(pw, m, variables).visit(prog);

        return pw.toProgram();
    }

    // Eraldi klass, et ModulMaster saaks taaskasutada.
    static class Visitor extends ModulAstVisitor<Void> {
        protected final CMaProgramWriter pw;
        protected final int m;
        protected final List<String> variables;

        public Visitor(CMaProgramWriter pw, int m, List<String> variables) {
            this.pw = pw;
            this.m = m;
            this.variables = variables;
        }

        /**
         * Normaliseerib väärtuse poollõiku [0, m).
         */
        protected void normalize() {
            pw.visit(LOADC, m);
            pw.visit(MOD);
            pw.visit(LOADC, m);
            pw.visit(ADD);
            pw.visit(LOADC, m);
            pw.visit(MOD);
        }

        @Override
        protected Void visit(ModulNum num) {
            pw.visit(LOADC, num.getValue());
            normalize();
            return null;
        }

        @Override
        protected Void visit(ModulVar var) {
            int variableIndex = variables.indexOf(var.getName());
            if (variableIndex < 0)
                throw new ModulException();
            else {
                pw.visit(LOADA, variableIndex);
                normalize();
                return null;
            }
        }

        @Override
        protected Void visit(ModulNeg neg) {
            visit(neg.getExpr());
            pw.visit(NEG);
            normalize();
            return null;
        }

        @Override
        protected Void visit(ModulAdd add) {
            visit(add.getLeft());
            visit(add.getRight());
            pw.visit(ADD);
            normalize();
            return null;
        }

        @Override
        protected Void visit(ModulMul mul) {
            visit(mul.getLeft());
            visit(mul.getRight());
            pw.visit(MUL);
            normalize();
            return null;
        }

        @Override
        protected Void visit(ModulPow pow) {
            int power = pow.getPower();
            if (power == 0) {
                pw.visit(LOADC, 1);
            }
            else {
                visit(pow.getBase());
                for (int i = 0; i < power - 1; i++) {
                    pw.visit(DUP);
                }
                for (int i = 0; i < power - 1; i++) {
                    pw.visit(MUL);
                    normalize();
                }
            }
            return null;
        }

        @Override
        protected Void visit(ModulProg prog) {
            visit(prog.getExpr());
            return null;
        }
    }
}
