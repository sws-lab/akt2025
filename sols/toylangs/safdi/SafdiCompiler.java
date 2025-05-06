package toylangs.safdi;

import cma.CMaLabel;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.safdi.ast.*;

import java.util.List;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.LOADA;
import static cma.instruction.CMaIntInstruction.Code.LOADC;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;

public class SafdiCompiler {

    public static CMaProgram compile(SafdiNode node, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        new SafdiAstVisitor<Void>() {
            @Override
            protected Void visit(SafdiNum num) {
                pw.visit(LOADC, num.getValue());
                return null;
            }

            @Override
            protected Void visit(SafdiVar var) {
                int variableIndex = variables.indexOf(var.getName());
                if (variableIndex < 0)
                    visitThrow();
                else
                    pw.visit(LOADA, variableIndex);
                return null;
            }

            /**
             * Viska CMa-s erind kokkulepitud viisil.
             */
            private void visitThrow() {
                pw.visit(HALT);
            }

            @Override
            protected Void visit(SafdiNeg neg) {
                visit(neg.getExpr());
                pw.visit(NEG);
                return null;
            }

            @Override
            protected Void visit(SafdiAdd add) {
                visit(add.getLeft());
                visit(add.getRight());
                pw.visit(ADD);
                return null;
            }

            @Override
            protected Void visit(SafdiMul mul) {
                visit(mul.getLeft());
                visit(mul.getRight());
                pw.visit(MUL);
                return null;
            }

            @Override
            protected Void visit(SafdiDiv div) {
                // jagaja kontroll
                visit(div.getRight());
                CMaLabel zeroDiv = new CMaLabel();
                pw.visit(JUMPZ, zeroDiv);

                // tavaline jagamine
                visit(div.getLeft());
                visit(div.getRight()); // ei saa DUP kasutada, sest DIV järjekord vastupidine
                pw.visit(DIV);
                CMaLabel end = new CMaLabel();
                pw.visit(JUMP, end);

                // nulliga jagamine
                pw.visit(zeroDiv);
                if (div.getRecover() != null)
                    visit(div.getRecover());
                else
                    visitThrow();

                pw.visit(end);
                return null;
            }

        }.visit(node);

        return pw.toProgram();
    }
}
